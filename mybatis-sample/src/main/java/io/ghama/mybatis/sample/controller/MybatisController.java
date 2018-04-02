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

package io.ghama.mybatis.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.ghama.mybatis.sample.domain.User;
import io.ghama.mybatis.sample.service.UserService;

@RestController
public class MybatisController {

	@Autowired
	private UserService mybatisService;

	@RequestMapping(path = "/mybatis")
	public List<User> listUser() {
		return mybatisService.listUser(null);
	}

	@RequestMapping(path = "/add")
	public List<User> addUser(@RequestParam(value = "userId") String userId, @RequestParam(value = "name") String name, @RequestParam(value = "email") String email) {
		User user = new User();
		user.setId(userId);
		user.setName(name);
		user.setEmail(email);
		mybatisService.addUser(user);

		return mybatisService.listUser(user.getName());
	}

}
