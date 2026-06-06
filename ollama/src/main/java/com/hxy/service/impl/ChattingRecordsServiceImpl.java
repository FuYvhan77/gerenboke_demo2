package com.hxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxy.enums.ChatTypeEnum;
import com.hxy.pojo.ChattingRecords;
import com.hxy.service.ChattingRecordsService;
import com.hxy.mapper.ChattingRecordsMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author zhangyadong
* @description 针对表【chatting_records】的数据库操作Service实现
* @createDate 2026-04-17 13:49:09
*/
@Service
public class ChattingRecordsServiceImpl extends ServiceImpl<ChattingRecordsMapper, ChattingRecords>
    implements ChattingRecordsService{

    @Override
    public void add(String userId, String string, ChatTypeEnum chatTypeEnum) {
        ChattingRecords chattingRecords=new ChattingRecords();
        chattingRecords.setUserName(userId);
        chattingRecords.setContent(string);
        chattingRecords.setChatType(chatTypeEnum.type);
        chattingRecords.setChatTime(new Date());
        this.save(chattingRecords);
    }
}




