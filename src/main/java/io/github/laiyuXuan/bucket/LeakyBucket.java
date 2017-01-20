package io.github.laiyuXuan.bucket;

/**
 * Created by laiyuXuan on 2017/1/20.
 */
public interface LeakyBucket<T> {

	boolean push(T t);

	T pop();
}
