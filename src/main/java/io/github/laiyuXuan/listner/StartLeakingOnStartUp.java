package io.github.laiyuXuan.listner;

import io.github.laiyuXuan.bucket.impl.JoinPointLeakyBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.StandardEnvironment;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by laiyuXuan on 2017/1/20.
 * start the leaking thread after starting up the application.
 */
public class StartLeakingOnStartUp implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger logger = LoggerFactory.getLogger(StartLeakingOnStartUp.class);

	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		JoinPointLeakyBucket leakyBucket = contextRefreshedEvent.getApplicationContext().getBean(JoinPointLeakyBucket.class);
		if (leakyBucket == null) {
			logger.error("Fail to start leaky bucket thread! Couldn't find class LeakyBucket.");
			return;
		}
		StandardEnvironment environment = (StandardEnvironment)contextRefreshedEvent.getApplicationContext().getBean("environment");
		int rate = getRate(environment.getProperty("leakyBucket.rate"));
		if (rate == 0) {
			return;
		}
		leakyBucket.setCapacity(rate);
		Executors.newSingleThreadScheduledExecutor()
				 .schedule(leakyBucket, 1/rate, TimeUnit.MILLISECONDS);
	}

	private int getRate(String rateStr){
		if (rateStr == null || rateStr.equals("")) {
			logger.error("Fail to set leaky bucket rate! LeakyBucket.rate hasn't been specified.");
			return 0;
		}
		try {
			return Integer.parseInt(rateStr);
		} catch (NumberFormatException numberFormatException){
			logger.error("Fail to set leaky bucket rate! The leakyBucket.rate should be an Integer.");
			return 0;
		}
	}
}
