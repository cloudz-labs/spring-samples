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

package io.ghama.jpa.sample.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.ghama.jpa.sample.domain.User;
import io.ghama.jpa.sample.repository.UserRepository;

@RestController
public class JpaController {
	
	private static Logger logger = LoggerFactory.getLogger(JpaController.class);

	@Autowired
	private UserRepository repository;
	
	
	@RequestMapping(path = "/jpa")
	public List<User> listUser() {
		repository.deleteAll();
		
		repository.save(new User("Alice", "alice@email.com"));
		repository.save(new User("Bob", "bob@email.com"));

		logger.info("User found with findAll():");
		logger.info("-------------------------------");
		for (User customer : repository.findAll()) {
			logger.info(customer.toString());
		}
		logger.info("");

		logger.info("User found with findByEmail('Alice'):");
		logger.info("--------------------------------");
		logger.info(repository.findByEmail("alice@email.com").toString());

		return repository.findByEmail("alice@email.com");
	}
	
}
