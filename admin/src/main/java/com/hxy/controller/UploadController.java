package com.hxy.controller;


import com.hxy.result.ResponseResult;
import com.hxy.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("img") MultipartFile multipartFile){
        ResponseResult responseResult = uploadService.uploadImg(multipartFile);
        return responseResult;
    }
}
