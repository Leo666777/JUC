package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @program: JUC_study
 * @description:
 * @author: HP
 * @create: 2023-05-25 16:18
 **/
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask task=new FutureTask<>(() -> {
            log.debug("running...");
            Thread.sleep(1000);
            return 100;
        });

        Thread thread=new Thread(task,"t1");
        thread.start();

        log.debug("{}",task.get());
    }
}
