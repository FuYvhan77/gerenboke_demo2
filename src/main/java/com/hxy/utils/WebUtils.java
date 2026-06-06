package com.hxy.utils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Web 工具类
 * 用于拦截器向前端响应 JSON 数据
 */
public class WebUtils {

    /**
     * 将字符串渲染到客户端
     * @param response 响应对象
     * @param string 待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(string);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void setDownLoadHeader(String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置响应内容类型为Excel（xlsx格式）
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置字符编码为UTF-8，避免中文乱码
        response.setCharacterEncoding("utf-8");
        // 对文件名进行URL编码，解决中文文件名下载乱码问题
        String fname = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        // 设置下载头，指定文件名称和附件形式下载
        response.setHeader("Content-disposition", "attachment; filename=" + fname);
    }
}