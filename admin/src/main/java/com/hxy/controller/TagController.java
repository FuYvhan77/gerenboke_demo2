package com.hxy.controller;


import com.hxy.dto.TagListDto;
import com.hxy.pojo.Tag;
import com.hxy.result.ResponseResult;
import com.hxy.service.TagService;
import com.hxy.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(TagListDto tagListDto) {
        return tagService.listTag(tagListDto);
    }


    @PostMapping
    public ResponseResult add(@RequestBody Tag tag) {
        tagService.add(tag);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable("id") List<Long> ids) {
        tagService.delByid(ids);
        return ResponseResult.okResult();
    }


    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable("id") Long id) {
        return ResponseResult.okResult(tagService.getById(id));
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Tag tag) {
        tagService.updateby(tag);
        return ResponseResult.okResult();
    }


    @GetMapping("/listAllTag")
    public ResponseResult listAllTag() {
        return tagService.listAllTag();
    }
}





