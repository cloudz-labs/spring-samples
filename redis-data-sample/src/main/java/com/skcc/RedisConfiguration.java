package com.skcc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.skcc.profile.vo.Profile;

@Configuration
public class RedisConfiguration {

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setEnableTransactionSupport(true);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
		return template;
	}
	
	@Bean
	@Qualifier("profileRedisTemplate")
	public RedisTemplate<String, Profile> profileRedisTemplate() {
		RedisTemplate<String, Profile> template = new RedisTemplate<String, Profile>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setEnableTransactionSupport(true);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Profile.class));
		template.afterPropertiesSet();
		return template;
	}
}
