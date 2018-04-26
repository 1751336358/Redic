package com.robert.redis.redic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisPool;

import com.robert.redis.redic.strategy.RoundRobinSelectStrategy;
import com.robert.redis.redic.strategy.SelectStrategy;
/**
 * 一个RedicNode代表一个缓存分片
 * 包括一个Master和多个Slave
 */
public class RedicNode {
	public static final String NODE_SEPARATOR = ",";

	public static final String HOST_PORT_SEPARATOR = ":";

	private JedisPool master;
	private List<JedisPool> slaves;

	public SelectStrategy selectStrategy;

	public RedicNode(JedisPool master, List<JedisPool> slaves) {
		this.master = master;
		this.slaves = slaves;
		this.selectStrategy = new RoundRobinSelectStrategy();	//从库负载均衡策略
	}

	public RedicNode(JedisPool master, List<JedisPool> slaves,
			SelectStrategy selectStrategy) {
		this.master = master;
		this.slaves = slaves;
		this.selectStrategy = selectStrategy;
	}
	/**
	 * 初始化Master和Slave的RedisPool
	 * @param masterConnStr
	 * @param slavesConnStrs
	 */
	public RedicNode(String masterConnStr, List<String> slavesConnStrs) {
		String[] masterHostPortArray = masterConnStr.split(HOST_PORT_SEPARATOR);	//IP:Port
		this.master = new JedisPool(new GenericObjectPoolConfig(),
				masterHostPortArray[0], Integer.valueOf(masterHostPortArray[1]));

		this.slaves = new ArrayList<JedisPool>();
		for (String slaveConnStr : slavesConnStrs) {
			String[] slaveHostPortArray = slaveConnStr
					.split(HOST_PORT_SEPARATOR);
			this.slaves.add(new JedisPool(new GenericObjectPoolConfig(),
					slaveHostPortArray[0], Integer
							.valueOf(slaveHostPortArray[1])));
		}

		this.selectStrategy = new RoundRobinSelectStrategy();
	}

	public RedicNode(String masterConnStr, List<String> slavesConnStrs,
			SelectStrategy selectStrategy) {
		this(masterConnStr, slavesConnStrs);
		this.selectStrategy = selectStrategy;
	}

	public JedisPool getMaster() {
		return master;
	}

	public void setMaster(JedisPool master) {
		this.master = master;
	}

	public List<JedisPool> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<JedisPool> slaves) {
		this.slaves = slaves;
	}

	public JedisPool getRoundRobinSlaveRedicNode() {
		int nodeIndex = selectStrategy.select(slaves.size());
		return slaves.get(nodeIndex);	//返回某个Slave的JedisPool
	}
}
