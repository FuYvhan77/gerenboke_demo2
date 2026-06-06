package com.hxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName chatting_records
 */
@TableName(value ="chatting_records")
@Data
public class ChattingRecords {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private String chatType;

    /**
     * 
     */
    private Date chatTime;

    /**
     * 
     */
    private String userName;
}