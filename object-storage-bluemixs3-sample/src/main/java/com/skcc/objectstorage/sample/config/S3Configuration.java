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

import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.s3.S3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

@Configuration
public class S3Configuration {

	@Value("${services.objectstorage.access_key}")
	private String accessKey;

	@Value("${services.objectstorage.secret_key}")
	private String secretKey;

	@Value("${services.objectstorage.auth_url}")
	private String authUrl;

	@Bean
	public S3Client s3Client() {
        String provider = "s3";
        
        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());
        S3Client scClient = ContextBuilder.newBuilder(provider)
	              .endpoint(authUrl)
	              .credentials(accessKey, secretKey)
	              .modules(modules)
	              .buildApi(S3Client.class);
        
        return scClient;
	}
}
