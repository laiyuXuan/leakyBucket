package io.github.laiyuXuan.bucket.impl;

import io.github.laiyuXuan.bucket.LeakyBucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by laiyuXuan on 2017/1/19.
 */
@Component
public class JoinPointLeakyBucket extends Thread implements LeakyBucket<ProceedingJoinPoint> {

	private static Logger logger = LoggerFactory.getLogger(JoinPointLeakyBucket.class);

	private  ArrayBlockingQueue queue;

	public boolean push(ProceedingJoinPoint joinPoint){
		return queue.offer(joinPoint);
	}

	public ProceedingJoinPoint pop(){
		return (ProceedingJoinPoint)queue.poll();
	}

	public void setCapacity(int capacity){
		queue = new ArrayBlockingQueue(capacity);
	}

	@Override
	public void run(){
		if (queue == null) {
			return;
		}
		if (queue.isEmpty()) {
			return;
		}
		try {
			pop().proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			throw new RuntimeException(throwable.getMessage());
		}
	}
}