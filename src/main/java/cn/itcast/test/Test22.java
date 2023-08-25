package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: JUC_study
 * @description:
 * @author: HP
 * @create: 2023-08-25 15:15
 **/
@Slf4j(topic = "c.Test22")
public class Test22 {
    private static ReentrantLock lock=new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
            log.debug("enter main");
            m1();
        }finally {
            lock.unlock();
        }
    }
    public static void m1(){
        lock.lock();
        try {
            log.debug("enter m1");
            m2();
        }finally {
            lock.unlock();
        }
    }

    public static void m2(){
        lock.lock();
        try {
            log.debug("enter m2");
        }finally {
            lock.unlock();
        }
    }
}
