package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * @program: JUC_study
 * @description:
 * @author: HP
 * @create: 2023-08-24 16:25
 **/
@Slf4j(topic = "c.Test21")
public class Test21 {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = 1;
            new Thread(() -> {
                queue.put(new Message(id, "值" + id));
            }, "生产者" + i).start();
        }
        new Thread(() -> {
            while (true) {
                sleep(1);
                Message message = queue.take();
            }

        }, "消费者").start();
    }
}

// 消息队列类，Java线程之间通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    // 消息队列集合
    private LinkedList<Message> list = new LinkedList<>();
    // 队列容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // 获取消息
    public Message take() {
        synchronized (list) {
            // 检查队列是否为空
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        // 从队列头部获取消息返回
        Message message = list.removeFirst();
        log.debug("已消费消息：{}", message);
        list.notifyAll();
        return message;
        }
    }

    // 存入消息
    public void put(Message message) {
        synchronized (list) {
            // 检查队列是否已满
            while (list.size() == capacity) {
                try {
                    log.debug("队列已满，生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 将消息加入队列尾部
            list.addLast(message);
            log.debug("已生产消息：{}", message);
            list.notifyAll();
        }
    }
}

@Slf4j(topic = "c.Message")
final class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
