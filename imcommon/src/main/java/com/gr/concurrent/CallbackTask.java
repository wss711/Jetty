package com.gr.concurrent;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-10 22:30
 */
public interface CallbackTask<R> {

    R execute() throws Exception;

    void onBack(R r);

    void onException(Throwable t);

}
