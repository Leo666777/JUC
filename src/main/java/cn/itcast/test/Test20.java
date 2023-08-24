package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import cn.itcast.pattern.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: JUC_study
 * @description:
 * @author: HP
 * @create: 2023-08-24 14:42
 **/
@Slf4j(topic = "c.Test20")
public class Test20 {
    // 线程1 等待线程2的下载结果
    public static void main(String[] args) {
//        GuardedObject guardedObject = new GuardedObject();
//        new Thread(() -> {
//            //等待结果
//            log.debug("等待结果");
//            List<String> list = (List<String>) guardedObject.get(2000);
//            log.debug("结果大小：{}", list.size());
//        }, "t1").start();
//
//        new Thread(() -> {
//            log.debug("执行下载");
//            try {
//                List<String> list = Downloader.download();
//                guardedObject.complete(list);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }, "t2").start();
//    }
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id,"内容"+id).start();
        }
    }
}

@Slf4j(topic = "c.Postman")
class Postman extends Thread {
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }


    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        guardedObject.complete(mail);
        log.debug("送信 id:{}，内容：{}", guardedObject.getId(), mail);
    }
}

@Slf4j(topic = "c.People")
class People extends Thread {
    @Override
    public void run() {
        // 收件
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.debug("收信前 id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收信后 id:{}，内容:{}", guardedObject.getId(), mail);
    }
}

class Mailboxes {
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;

    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static GuardedObject createGuardedObject() {
        GuardedObject guardedObject = new GuardedObject(generateId());
        boxes.put(guardedObject.getId(), guardedObject);
        return guardedObject;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

}

class GuardedObject {

    private int id;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // 结果
    private Object response;

    // 获取结果
    //timeout 表示要等多久
    public Object get(long timeout) {
        synchronized (this) {
            // 没有结果
            // 记录开始时间
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                try {
                    long waitTime = timeout - passedTime;
                    // 经历时间超过了timeout最大等待时间
                    if (waitTime <= 0) {
                        break;
                    }
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            // 给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}
