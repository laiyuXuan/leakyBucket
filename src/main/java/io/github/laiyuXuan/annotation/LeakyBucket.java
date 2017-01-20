package io.github.laiyuXuan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by laiyuXuan on 2017/1/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LeakyBucket {

	/**
	 * the bucket leaks at this rate.
	 */
	int rate();
}
