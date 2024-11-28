package com.macro.mall.service;

import java.io.InputStream;

/**
 * 华为云OBS存储上传管理Service
 */
public interface HuaweiCloudOBSService {

    /**
     * 获取文件路径
     *
     * @param keyName 文件名
     */
    String generateUrl(String keyName);

    /**
     * 存储指定文件对象
     *
     * @param inputStream 文件输入流
     * @param contentLength 文件长度
     * @param contentType 文件类型
     * @param keyName 文件名
     * @return obj full URL
     */
    String store(InputStream inputStream, long contentLength, String contentType, String keyName);

    /**
     * 删除指定文件对象
     *
     * @param keyName 文件名
     */
    void delete(String keyName);
}
