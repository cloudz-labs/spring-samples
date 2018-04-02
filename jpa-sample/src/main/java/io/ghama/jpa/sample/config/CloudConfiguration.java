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

package io.ghama.jpa.sample.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "stg", "prod"})
public class CloudConfiguration extends AbstractCloudConfig {

	@Value("${services.datasource.name}")
	private String datasourceName;

	@Value("${services.datasource.initial-size}")
	private int minPoolSize;

	@Value("${services.datasource.maximum-pool-size}")
	private int maxPoolSize;

	@Value("${services.datasource.max-wait-time}")
	private int maxWaitTime;

	/**
	 * configure datasource.
	 * @return dataSource object
	 */
	@Bean
	public DataSource dataSource() {
		PoolConfig poolConfig = new PoolConfig(minPoolSize, maxPoolSize, maxWaitTime);
		DataSourceConfig dbConfig = new DataSourceConfig(poolConfig, null);
		return connectionFactory().dataSource(datasourceName, dbConfig);
	}

}