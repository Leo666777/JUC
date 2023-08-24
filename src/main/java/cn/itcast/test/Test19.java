package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: JUC_study
 * @description:
 * @author: HP
 * @create: 2023-08-21 14:51
 **/
@Slf4j(topic = "c.Test19")
public class Test19 {
    static final Object lock=new Object();
    public static void main(String[] args) {
        new Thread(()->{
            synchronized (lock){
                log.debug("获得锁");
                try {
//                    Thread.sleep(20000);
                    lock.wait(20000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"t1").start();

        Sleeper.sleep(1);
        synchronized (lock){
            log.debug("主线程获得锁");
        }
    }
}
