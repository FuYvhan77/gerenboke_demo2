package com.hxy.service;

import com.hxy.enums.ChatTypeEnum;
import com.hxy.pojo.ChattingRecords;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhangyadong
* @description 针对表【chatting_records】的数据库操作Service
* @createDate 2026-04-17 13:49:09
*/
public interface ChattingRecordsService extends IService<ChattingRecords> {

    void add(String userId, String string, ChatTypeEnum chatTypeEnum);
}
