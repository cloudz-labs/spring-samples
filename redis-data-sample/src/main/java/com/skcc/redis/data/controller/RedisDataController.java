package com.skcc.redis.data.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisDataController {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisDataController.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@GetMapping("/clean")
	public String cleanRedisDatas(){
		redisTemplate.delete(redisTemplate.keys("*"));
		return "All data has been cleaned";
	}
	
	@GetMapping("/keys")
	public Set<String> getKeys(){
		Set<String> keys = redisTemplate.keys("*");
		keys.forEach(key -> {
			logger.info(key);
		});
		return keys;
	}
}
