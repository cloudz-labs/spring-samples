/*
 * Copyright (c) 2016 SK holdings Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.ghama.objectstorage.sample.config;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Profile({"dev", "stg", "prod"})
public class SwiftConfiguration {

	@Value("${services.objectstorage.username}")
	private String userName;

	@Value("${services.objectstorage.password}")
	private String password;

	@Value("${services.objectstorage.auth_url}")
	private String authUrl;

	@Bean
	public AccountConfig accountConfig() {
//		// non-springboot
//		Gson gson = new Gson();
//		String vcap_services = System.getenv("VCAP_SERVICES");
//		JsonObject jsonObj = gson.fromJson(vcap_services, JsonElement.class).getAsJsonObject();
//		
//		JsonArray storage_service = jsonObj.getAsJsonArray("user-provided");
//		jsonObj = storage_service.get(0).getAsJsonObject().getAsJsonObject("credentials");
//		String userName = jsonObj.get("username").getAsString();
//		String password = jsonObj.get("password").getAsString();
//		String authUrl = jsonObj.get("auth_url").getAsString();
		
		AccountConfig config = new AccountConfig();
		config.setUsername(userName);
		config.setPassword(password);
		config.setAuthUrl(authUrl);
		config.setAuthenticationMethod(AuthenticationMethod.TEMPAUTH);
		return config;
	}

	@Bean
	public AccountFactory accountFactory() {
		return new AccountFactory(accountConfig());
	}

	@Bean
	public Account account() {
		return accountFactory().createAccount();
	}
	
}
