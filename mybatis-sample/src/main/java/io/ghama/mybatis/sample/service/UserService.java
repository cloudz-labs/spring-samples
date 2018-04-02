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

package io.ghama.mybatis.sample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.ghama.mybatis.sample.dao.UserDAO;
import io.ghama.mybatis.sample.domain.User;

@Service
public class UserService {

	@Autowired
	private UserDAO userDao;

	public List<User> listUser(String name) {
		return userDao.listUser(name);
	}

	public void addUser(User user) {
		userDao.addUser(user);
	}
}
