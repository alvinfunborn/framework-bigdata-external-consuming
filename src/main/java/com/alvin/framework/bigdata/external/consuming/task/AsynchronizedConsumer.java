package com.alvin.framework.bigdata.external.consuming.task;

import com.alvin.framework.bigdata.external.consuming.service.Consumer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * datetime 2019/5/13 16:58
 *
 * @author sin5
 */
public class AsynchronizedConsumer<T> implements Consumer<T> {

    private ExecutorService executorService;
    private Consumer<T> consumer;

    public AsynchronizedConsumer(Consumer<T> consumer, int corePoolSize, int maximumPollSize, int workQueueSize) {
        this.consumer = consumer;
        this.executorService = new ThreadPoolExecutor(corePoolSize, maximumPollSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(workQueueSize), (r, executor) -> {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void batchConsume(List<T> list) {
        list.forEach(t -> executorService.execute(() -> consumer.batchConsume(Collections.singletonList(t))));
    }

    @Override
    public void onEnd() {
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                consumer.onEnd();
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
