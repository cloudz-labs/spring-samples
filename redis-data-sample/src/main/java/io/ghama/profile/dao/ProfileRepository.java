package io.ghama.profile.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.ghama.profile.vo.Profile;

@Repository
public class ProfileRepository {
	
	private final static String PROFILE_PREFIX = "profile:";
	
	private static Logger logger = LoggerFactory.getLogger(ProfileRepository.class);
	
	@Autowired
	@Qualifier("profileRedisTemplate")
	private RedisTemplate<String, Profile> profileRedisTemplate;
	
	@Value(value = "classpath:json")
	private Resource resourceFolder;
	
	private ValueOperations<String, Profile> opsForValue;
	
	public ProfileRepository(RedisTemplate<String, Profile> profileRedisTemplate){
		this.opsForValue = profileRedisTemplate.opsForValue();
	}
	
    public List<Profile> findAll() {
    	Set<String> keys = profileRedisTemplate.keys(PROFILE_PREFIX + "*");
		return opsForValue.multiGet(keys);
	}
    
    public Profile findByUsername(String id) {
		return opsForValue.get(PROFILE_PREFIX + id);
	}
    
    public void save(Profile profile){
    	opsForValue.set(PROFILE_PREFIX + profile.getId(), profile);
    }
    
    public void delete(String key){
    	profileRedisTemplate.delete(PROFILE_PREFIX + key);
    }
    
    public void loadData() throws IOException{
		File jsonFolder = resourceFolder.getFile();
		Gson gson = new Gson();
		
		TypeToken<List<Profile>>profileToken = new TypeToken<List<Profile>>(){};
		List<Profile> profiles = gson.fromJson(new FileReader(new File(jsonFolder, "profile.json")), profileToken.getType());
		profiles.forEach(profile -> {
			profileRedisTemplate.opsForValue().set(PROFILE_PREFIX + profile.getId(), profile);
			logger.info("{}", profile);
		});
	}
    
}
