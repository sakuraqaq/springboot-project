package com.sakura.service.workflow.converter.util;

import javax.validation.constraints.NotNull;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 当线程池满了的时候，线程池阻塞队列, 而不是抛出异常或抛弃其他任务.
 * @author: bi
 * @date: 2021/11/2 10:21
 */
public class LimitQueue<E> extends LinkedBlockingQueue<E> {

    public LimitQueue(int maxSize){
        super(maxSize);
    }

    @Override
    public boolean offer(@NotNull E e) {
        //return super.offer(e);
        try{
            put(e);
            return true;
        }catch (InterruptedException ie){
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
