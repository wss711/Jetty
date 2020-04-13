package com.gr.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-10 22:34
 */
@Slf4j
public class FutureTaskScheduler extends Thread {

    // 任务队列，单生产，多消费
    private ConcurrentLinkedQueue<ExecuteTask> executeTaskQueue = new ConcurrentLinkedQueue<ExecuteTask>();
    // 线程休眠时间
    private long sleepTime = 200;
    // 线程池（10）
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    // 线程实例
    private static FutureTaskScheduler instance = new FutureTaskScheduler();
    private FutureTaskScheduler(){
        this.start();
    }

    /** 添加任务 */
    public static void add(ExecuteTask executeTask){
        instance.executeTaskQueue.add(executeTask);
    }

    @Override
    public void run(){
        while(true){

            handleTask();

            threadSleep(sleepTime);

        }
    }

    /** 线程休眠时间 */
    private void threadSleep(long time){

        try {
            sleep(time);
        } catch (InterruptedException e) {
            log.error(String.valueOf(e));
        }

    }

    /** 处理任务队列，检查其中是否有任务 */
    private void handleTask(){

        try {
            ExecuteTask executeTask;
            while (executeTaskQueue.peek() != null){
                executeTask = executeTaskQueue.poll();
                handleTask(executeTask);
            }
        }catch (Exception e){
            log.error(String.valueOf(e));
        }
    }

    /** 执行任务操作 */
    private void handleTask(ExecuteTask executeTask){
        pool.execute(new ExecuteRunnable(executeTask));
    }

    class ExecuteRunnable implements Runnable {

        ExecuteTask executeTask;

        public ExecuteRunnable(ExecuteTask executeTask) {
            this.executeTask = executeTask;
        }

        public void run(){
            executeTask.excute();
        }
    }

}
