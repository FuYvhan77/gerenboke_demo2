package com.hxy.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.hxy.result.ResponseResult;
import com.hxy.service.UploadService;

import com.hxy.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName UploadServiceImpl
 * @date 2026-04-09 14:43
 */

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private OSSUtils ossUtils;


    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        String url=null;
        try {
             url = ossUtils.uploadFile(img);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

        return ResponseResult.okResult(url);
    }
}
