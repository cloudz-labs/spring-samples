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

package com.skcc.rabbitmq.sample.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.skcc.rabbitmq.sample.config.RabbitConfiguration;

@Component
@EnableScheduling
public class Sender {
	
	private static Logger logger = LoggerFactory.getLogger(Sender.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Scheduled(fixedDelay = 50000)
	public void send() {
		logger.info("Sending message...");
		rabbitTemplate.convertAndSend(RabbitConfiguration.queueName, "Hello from RabbitMQ!");
	}

}
