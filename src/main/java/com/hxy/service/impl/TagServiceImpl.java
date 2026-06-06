package com.hxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.dto.TagListDto;
import com.hxy.enums.AppHttpCodeEnum;
import com.hxy.exception.SystemException;
import com.hxy.pojo.Tag;
import com.hxy.result.ResponseResult;
import com.hxy.service.TagService;
import com.hxy.mapper.TagMapper;
import com.hxy.utils.UserContextHolder;
import com.hxy.vo.PageVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author zhangyadong
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2026-04-15 09:04:10
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public ResponseResult listTag(TagListDto tagListDto) {
        //分页  模糊查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(tagListDto.getName()!=null && !"".equals(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.like(tagListDto.getRemark()!=null && !"".equals(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        //分页
        Page<Tag> page = new Page<>(tagListDto.getPageNum(),tagListDto.getPageSize());
        Page<Tag> page1 = page(page, queryWrapper);


        PageVo pageVo = new PageVo(page1.getTotal(),page1.getRecords());


        return ResponseResult.okResult(pageVo);
    }

    @Override
    public void add(Tag tag) {
        //添加
        //效验
        if (tag.getName()==null|| tag.getRemark()==null){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        tag.setCreateBy(UserContextHolder.getUser().getId());
        tag.setUpdateBy(UserContextHolder.getUser().getId());
        tag.setCreateTime(new Date());
        tag.setUpdateTime(new Date());


        save(tag);
    }

    @Override
    public void delByid(List<Long> ids) {
        removeBatchByIds(ids);
    }

    @Override
    public void updateby(Tag tag) {
        tag.setUpdateTime(new Date());
        tag.setUpdateBy(UserContextHolder.getUser().getId());
        updateById(tag);
    }

    @Override
    public ResponseResult listAllTag() {
        return ResponseResult.okResult(list());
    }
}




