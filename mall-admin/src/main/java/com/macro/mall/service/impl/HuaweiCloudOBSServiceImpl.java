package com.macro.mall.service.impl;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.controller.MinioController;
import com.macro.mall.dto.MinioUploadDto;
import com.macro.mall.service.HuaweiCloudOBSService;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.obs.services.model.AccessControlList;
import java.io.InputStream;
import com.obs.services.model.PutObjectResult;

/**
 * 华为云OBS存储管理Service实现类
 */
@Service
public class HuaweiCloudOBSServiceImpl implements HuaweiCloudOBSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuaweiCloudOBSServiceImpl.class);
    @Value("${huaweicloud.obs.endPoint}")
    private String endPoint;

    @Value("${huaweicloud.obs.ak}")
    private String ak;

    @Value("${huaweicloud.obs.sk}")
    private String sk;

    @Value("${huaweicloud.obs.bucketName}")
    private String bucketName;

    @Value("${huaweicloud.obs.parentDir}")
    private String parentDir;

    private String getBaseUrl() {
        return "https://" + bucketName + "." + endPoint + "/";
    }

    private ObsClient getObsClient() {
        return new ObsClient(ak, sk, endPoint);
    }

    private void destroy(ObsClient obsClient) {
        try {
            obsClient.close();
        } catch (ObsException obsException) {
            LOGGER.error("function = destroy, obs exception", obsException);
        } catch (Exception e) {
            LOGGER.error("function = destroy, exception", e);
        }
    }

    @Override
    public String generateUrl(String keyName) {
        return getBaseUrl() + keyName;
    }

    @Override
    public String store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);
        ObsClient obsClient = this.getObsClient();
        String fullKeyName = parentDir + keyName;
        try {
            // 上传对象时指定预定义访问策略
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(fullKeyName);
            request.setInput(inputStream);
            request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
            PutObjectResult rest = obsClient.putObject(request);
            LOGGER.debug( fullKeyName + " 文件上传成功!");
            return  this.generateUrl(fullKeyName);
        } catch (Exception e) {
            LOGGER.error("function = store, exception", e);
            throw e;
        } finally {
            destroy(obsClient);
        }
    }

    @Override
    public void delete(String keyName) {
        ObsClient obsClient = getObsClient();
        try {
            obsClient.deleteObject(bucketName, keyName);
        } catch (Exception e) {
            LOGGER.error("function = delete, exception", e);
        } finally {
            destroy(obsClient);
        }
    }
}
