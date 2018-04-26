package com.robert.redis.redic.strategy;

public class HashShardingStrategy implements ShardingStrategy {
	public <T> int key2node(T key, int nodeCount) {
		int hashCode = key.hashCode();
		System.out.println(key+":"+nodeCount+":"+hashCode);
		return hashCode % nodeCount;
	}
}
