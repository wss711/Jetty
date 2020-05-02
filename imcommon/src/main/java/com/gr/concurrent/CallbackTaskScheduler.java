package com.gr.concurrent;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-10 22:33
 */
@Slf4j
public class CallbackTaskScheduler extends Thread {

    // 回调任务队列
    private ConcurrentLinkedQueue<CallbackTask> executeTaskQueue = new ConcurrentLinkedQueue<>();

    // 线程休眠时间
    private long sleepTime = 200;

    //
    private ExecutorService jPool = Executors.newCachedThreadPool();

    ListeningExecutorService gPool = MoreExecutors.listeningDecorator(jPool);

    private static CallbackTaskScheduler instance = new CallbackTaskScheduler();
    private CallbackTaskScheduler(){
        this.start();
    }

    /** 添加任务 */
    public static <R> void add(CallbackTask<R> executeTask){
        instance.executeTaskQueue.add(executeTask);
    }

    @Override
    public void run(){
        while(true){

            handleTask();

            threadSleep(sleepTime);

        }
    }

    /** 休眠时间 */
    public void threadSleep(long time){
        try {
            sleep(time);
        } catch (InterruptedException e) {
            log.error(String.valueOf(e));
        }
    }

    /** 处理任务队列，检查其中是否有任务 */
    public void handleTask(){
        try {
            CallbackTask executeTask = null;
            while(executeTaskQueue.peek() != null){
                executeTask = executeTaskQueue.poll();
                handleTask(executeTask);
            }
        }catch (Exception e){
            log.error(String.valueOf(e));
        }
    }

    /** 执行任务操作 */
    private <R> void handleTask(CallbackTask<R> executeTask){

        ListenableFuture<R> future = gPool.submit(() -> {

            R r = executeTask.execute();

            return r;
        });

        Futures.addCallback(future, new FutureCallback<R>() {
            public void onSuccess(R r) {
                executeTask.onBack(r);
            }

            public void onFailure(Throwable t) {
                executeTask.onException(t);
            }
        });
    }
}
