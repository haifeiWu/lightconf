package com.lightconf.common.sync;


import com.lightconf.common.util.BaseMsg;

import java.util.concurrent.Future;

/**
 * Future的实现的阻塞可以通过CountDownLatch来实现.
 * @author whfstudio@163.com
 * @date 2017/11/27
 */
public interface WriteFuture<T> extends Future<T> {

    Throwable cause();

    void setCause(Throwable cause);

    boolean isWriteSuccess();

    void setWriteResult(boolean result);

    String requestId();

    T response();

    void setResponse(BaseMsg response);

    boolean isTimeout();


}
