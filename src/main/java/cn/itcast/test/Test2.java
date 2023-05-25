package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: JUC_study
 * @description:
 * @author: HP
 * @create: 2023-05-25 15:55
 **/
@Slf4j(topic = "c.Test2")
public class Test2 {
    public static void main(String[] args) {
        Runnable r= () -> log.debug("running");

        Thread t=new Thread(r,"t2");
        t.start();
    }
}
