package com.alvin.framework.bigdata.external.consuming.task;

import com.alvin.framework.bigdata.external.consuming.model.ScrollResult;
import com.alvin.framework.bigdata.external.consuming.service.Consumer;
import com.alvin.framework.bigdata.external.consuming.service.Scroller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * datetime 2019/5/13 15:39
 *
 * @author sin5
 */
public class StandardExternalConsumingTask<T> implements ExternalConsumingTask<T> {

    private LinkedBlockingQueue<T> queue;
    private Scroller<T> scroller;
    private Consumer<T> consumer;

    public StandardExternalConsumingTask(int queueCapacity, Scroller<T> scroller, Consumer<T> consumer) {
        queue = new LinkedBlockingQueue<>(queueCapacity);
        this.scroller = scroller;
        this.consumer = consumer;
    }

    public void start(String initScrollCursor, int scrollSize, final int batchConsumingSize) {
        AtomicBoolean moreInQueue = new AtomicBoolean(true);
        new Thread(() -> consume(moreInQueue, batchConsumingSize)).start();
        boolean goon = true;
        while (goon) {
            ScrollResult<T> result = scroller.scroll(initScrollCursor, scrollSize);
            initScrollCursor = result.getIdCursor();
            int size = result.getData().size();
            if (size == 0) {
                break;
            }
            for (int i = 0; i < size; i++) {
                T t = result.getData().get(i);
                if (scroller.stopScrollingAt(t)) {
                    goon = false;
                    break;
                }
                if (scroller.addForConsuming(t)) {
                    try {
                        queue.put(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        while (true) {
            if (!queue.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        moreInQueue.set(false);
    }

    private void consume(AtomicBoolean moreInQueue, int size) {
        List<T> list = new ArrayList<>();
        while (moreInQueue.get()) {
            while (list.size() < size && moreInQueue.get()) {
                T t = queue.poll();
                if (t == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    list.add(t);
                }
            }
            consumer.batchConsume(list);
            list.clear();
        }
        consumer.onEnd();
    }
}
