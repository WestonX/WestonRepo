package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

/**
 * 如何标识配置类
 * 需要配置bean注解  
 * @author Administrator
 *
 */
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	/**
	 * 需求:将jedisCluster对象交给spring容器管理
	 * 步骤:
	 * 		1.节点IP地址和端口信息
	 * 		2.编辑配置类注入信息
	 * 		3.通过@Bean注解实例化jedisCluster对象
	 * 		4.利用依赖注入 注入工具对象完成任务
	 */
	
	@Value("${redis.nodes}")
	private String redisNodes; //node,node,node
	
	@Bean	
	public JedisCluster getJedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		//node="ip:端口"
		String[] node = redisNodes.split(",");
		for (String nodeInfo : node) {
			String host = nodeInfo.split(":")[0];
			int port = Integer.parseInt(nodeInfo.split(":")[1]);
			nodes.add(new HostAndPort(host, port));		}
		return new JedisCluster(nodes);
	}
	
	
	
	
	
	
	
	/*
	 * @Value("${redis.masterName}") private String masterName;
	 * 
	 * @Value("${redis.sentinelNodes}") private String sentinelNodes;
	 * 
	 * //3.实现redis整合哨兵
	 * 
	 * @Bean public JedisSentinelPool getPool() { Set<String> sentinels = new
	 * HashSet<>(); String[] nodes = sentinelNodes.split(","); for (String sNode :
	 * nodes) { sentinels.add(sNode); } return new JedisSentinelPool(masterName,
	 * sentinels); }
	 */
	
	
	/*
	 * //2.redis分片机制测试
	 * 
	 * @Value("${redis.nodes}") private String nodes; //ip:端口,ip:端口...
	 * 
	 * @Bean public ShardedJedis getShards() { List<JedisShardInfo>shards = new
	 * ArrayList<JedisShardInfo>();
	 * 
	 * //将nodes中的数据进行分组 {IP:端口} String[] node = nodes.split(","); for (String
	 * nodeArgs : node) { //{ip,端口} String[] args = nodeArgs.split(":"); String
	 * nodeIP = args[0]; int nodePort = Integer.parseInt(args[1]); JedisShardInfo
	 * info = new JedisShardInfo(nodeIP, nodePort); shards.add(info); } return new
	 * ShardedJedis(shards); }
	 */
	
	
	
	
	/*
	 * //1.redis单台测试
	 * 
	 * @Value("${redis.host}") private String host;
	 * 
	 * @Value("${redis.port}") private int port;
	 * 
	 * @Bean //执行方法获取实例化对象 public Jedis getJedis() {
	 * 
	 * return new Jedis(host, port); }
	 */
}







