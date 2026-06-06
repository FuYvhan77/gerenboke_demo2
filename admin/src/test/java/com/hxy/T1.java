package com.hxy;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.hxy.pojo.Stu;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhangyadong
 * @version 1.0
 * @ClassName T1
 * @date 2026-04-15 14:35
 */

@SpringBootTest

public class T1 {

    @Test
    public void test1(){
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        String fileName ="C:\\Users\\zhangyadong\\Desktop\\stu.xlsx";
        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
        EasyExcel.read(fileName, Stu.class, new PageReadListener<Stu>(dataList -> {
            for (Stu demoData : dataList) {
                System.out.println(demoData);
            }
        })).sheet().doRead();
    }
}
