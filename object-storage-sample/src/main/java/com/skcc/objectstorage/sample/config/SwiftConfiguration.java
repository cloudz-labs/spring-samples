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

package com.skcc.objectstorage.sample.config;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "stg", "prod"})
public class SwiftConfiguration {

	@Value("${services.objectstorage.username}")
	private String userName;

	@Value("${services.objectstorage.password}")
	private String password;

	@Value("${services.objectstorage.auth_url}")
	private String authUrl;

	@Value("${services.objectstorage.tenantid}")
	private String tenantId;

	@Value("${services.objectstorage.tenantname}")
	private String tenantName;

	/**
	 * configure AccountConfig.
	 * @return AccountConfig
	 */
	@Bean
	public AccountConfig accountConfig() {
		AccountConfig config = new AccountConfig();
		config.setUsername(userName);
		config.setPassword(password);
		config.setAuthUrl(authUrl + "/tokens");
		config.setTenantId(tenantId);
		config.setTenantName(tenantName);
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
