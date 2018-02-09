package com.lightconf.common.sync;


import com.lightconf.common.model.BaseMsg;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 自定义Future,使用CountDownLatch实现<br></>
 * 阻塞的目的就是等待服务端的响应.
 * @author whfstudio@163.com
 * @date 2017/11/27
 */
public class SyncWriteFuture  implements WriteFuture<BaseMsg>  {

    private CountDownLatch latch = new CountDownLatch(1);
    private final long begin = System.currentTimeMillis();
    private long timeout;
    private BaseMsg response;
    private final String requestId;
    private boolean writeResult;
    private Throwable cause;
    private boolean isTimeout = false;

    public SyncWriteFuture(String requestId) {
        this.requestId = requestId;
    }

    public SyncWriteFuture(String requestId, long timeout) {
        this.requestId = requestId;
        this.timeout = timeout;
        writeResult = true;
        isTimeout = false;
    }


    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public boolean isWriteSuccess() {
        return writeResult;
    }

    @Override
    public void setWriteResult(boolean result) {
        this.writeResult = result;
    }

    @Override
    public String requestId() {
        return requestId;
    }

    @Override
    public BaseMsg response() {
        return response;
    }

    @Override
    public void setResponse(BaseMsg response) {
        this.response = response;
        latch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public BaseMsg get() throws InterruptedException, ExecutionException {
        latch.wait();
        return response;
    }

    @Override
    public BaseMsg get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return response;
        }
        return null;
    }

    /**
     * 同步写入，如果正常情况下，应该不会有什么问题，但是如异常发生了，可能导致的一个结果就是writeRecords会越来越大，<br></>
     * 所以需要有一个Timer去定时清理，为此我们需要为每个SyncWriteFuture设置timeout时间，方便做清理工作。
     * @return
     */
    @Override
    public boolean isTimeout() {
        if (isTimeout) {
            return isTimeout;
        }
        return System.currentTimeMillis() - begin > timeout;
    }
}
