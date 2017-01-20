package io.github.laiyuXuan.aspect;

import io.github.laiyuXuan.Exception.OverFlowException;
import io.github.laiyuXuan.bucket.impl.JoinPointLeakyBucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by laiyuXuan on 2017/1/19.
 */
@Aspect
@Component
public class AnnotationLeakyBucketHandler {

	@Autowired
	private JoinPointLeakyBucket leakyBucket;

	@Pointcut("@annotation(io.github.laiyuXuan.annotation.LeakyBucket)")
	public void leakyBucket(){
	}

	@Around("leakyBucket()")
	public void aroundLeakyBucket(ProceedingJoinPoint proceedingJoinPoint){
		boolean push = leakyBucket.push(proceedingJoinPoint);
		if (!push) {
			throw new OverFlowException("take it easy and calm down.");
		}
	}
}
