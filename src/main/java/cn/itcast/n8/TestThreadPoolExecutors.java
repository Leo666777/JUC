package cn.itcast.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: JUC_study
 * @description:
 * @author: HP
 * @create: 2023-10-13 15:56
 **/
@Slf4j(topic = "c.TestThreadPoolExecutors")
public class TestThreadPoolExecutors {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger t=new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable,"mypool_t"+t.getAndIncrement());
            }
        });

        pool.execute(()->{
            log.debug("1");
        });

        pool.execute(()->{
            log.debug("2");
        });

        pool.execute(()->{
            log.debug("3");
        });
    }
}
