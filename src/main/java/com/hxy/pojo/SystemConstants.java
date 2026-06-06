package com.hxy.pojo;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    //删除标志
    public static final String DELETE_FLAG_NO = "0";
    public static final String DELETE_FLAG = "1";


    //热门文章查询范围
    public static final int HOT_ARTICLE_LIMIT = 5;


    //评论的根评论
    public static final int ROOT_COMMENT = -1;

    /** 菜单类型：菜单 */
    public static final String MENU = "C";
    /** 菜单类型：按钮 */
    public static final String BUTTON = "F";
    /** 状态：正常 */
    public static final int STATUS_NORMAL = 0;
}