package com.liuzhenhui.commons.utils.redis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 缓存配置
 * @author rui.wang
 * @since 2013年10月25日
 */
@Configuration
@ComponentScan(basePackages = "com.lefu.commons.cache", excludeFilters = { @Filter(Controller.class), @Filter(Configuration.class) })
@PropertySource("classpath:/redis.properties")
public class CacheConfig {
	@Value("${redis.host}")
	private String hostName;
	// @Value("${redis.port}")
	// private int port;
	@Value("${redis.master.name}")
	private String masterName;
	@Value("${redis.password}")
	private String password;
	@Value("${redis.dbIndex}")
	private int dbIndex;
	@Value("${redis.connection.timeout}")
	private int connTimeout;
	@Value("${redis.pool.maxTotal}")
	private int maxTotal;
	@Value("${redis.pool.minIdle}")
	private int minIdle;
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	@Value("${redis.pool.maxWaitMillis}")
	private long maxWaitMillis;
	@Value("${redis.pool.blockWhenExhausted}")
	private boolean blockWhenExhausted;
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${redis.pool.testOnReturn}")
	private boolean testOnReturn;
	@Value("${redis.pool.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${redis.pool.minEvictableIdleTimeMillis}")
	private long minEvictableIdleTimeMillis;
	@Value("${redis.pool.timeBetweenEvictionRunsMillis}")
	private long timeBetweenEvictionRunsMillis;
	@Value("${redis.pool.numTestsPerEvictionRun}")
	private int numTestsPerEvictionRun;
	@Value("${redis.pool.softMinEvictableIdleTimeMillis}")
	private long softMinEvictableIdleTimeMillis;
	@Value("${redis.pool.lifo}")
	private boolean lifo;

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		jedisPoolConfig.setTestOnReturn(testOnReturn);
		jedisPoolConfig.setTestWhileIdle(testWhileIdle);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
		jedisPoolConfig.setLifo(lifo);
		return jedisPoolConfig;
	}

	@Bean
	public JedisSentinelPool jedisPool() {
		Set<String> sentinels = new HashSet<String>();
		sentinels.addAll(Arrays.asList(hostName.split(";")));

		JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig(), connTimeout, password, dbIndex);
		return jedisSentinelPool;
	}
}
