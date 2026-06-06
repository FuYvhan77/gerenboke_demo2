package com.hxy.service;

import com.hxy.dto.TagListDto;
import com.hxy.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hxy.result.ResponseResult;

import java.util.List;

/**
* @author zhangyadong
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2026-04-15 09:04:10
*/
public interface TagService extends IService<Tag> {


    ResponseResult listTag(TagListDto tagListDto);

    void add(Tag tag);

    void delByid(List<Long> ids);

    void updateby(Tag tag);

    ResponseResult listAllTag();
}
