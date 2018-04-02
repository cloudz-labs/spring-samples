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

package com.skcc.mongodb.sample.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skcc.mongodb.sample.domain.Customer;
import com.skcc.mongodb.sample.repository.CustomerRepository;

@RestController
public class MongoDbController {
	
	private static Logger logger = LoggerFactory.getLogger(MongoDbController.class);

	@Autowired
	private CustomerRepository repository;

	@RequestMapping(path = "/mongodb")
	public List<Customer> listUser(Model model) {
		repository.deleteAll();
		
		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		logger.info("Customers found with findAll():");
		logger.info("-------------------------------");
		for (Customer customer : repository.findAll()) {
			logger.info(customer.toString());
		}
		logger.info("");

		// fetch an individual customer
		logger.info("Customer found with findByFirstName('Alice'):");
		logger.info("--------------------------------");
		logger.info(repository.findByFirstName("Alice").toString());

		logger.info("Customers found with findByLastName('Smith'):");
		logger.info("--------------------------------");
		for (Customer customer : repository.findByLastName("Smith")) {
			logger.info(customer.toString());
		}
		
		return repository.findByLastName("Smith");
	}
}
