package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.MinioUploadDto;
import com.macro.mall.service.impl.HuaweiCloudOBSServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 华为云Obs相关操作接口
 */
@Controller
@Api(tags = "ObsController", description = "Obs管理")
@RequestMapping("/huaweicloudobs")
public class ObsController {

    @Autowired
    private HuaweiCloudOBSServiceImpl obsService;

    @ApiOperation("华为obs上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult upload(@RequestPart("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 设置存储对象名称
        String afterObjectName = sdf.format(new Date()) + "/" + filename;
        try {
            String fullUrl = obsService.store(file.getInputStream(), file.getSize(), file.getContentType(), afterObjectName);
            MinioUploadDto minioUploadDto = new MinioUploadDto();
            minioUploadDto.setName(filename);
            minioUploadDto.setUrl(fullUrl);
            return CommonResult.success(minioUploadDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @ApiOperation("文件删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("objectName") String objectName) {
        try {
            obsService.delete(objectName);
            return CommonResult.success(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }
}
