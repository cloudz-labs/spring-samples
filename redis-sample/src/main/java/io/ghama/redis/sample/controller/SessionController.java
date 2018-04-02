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

package io.ghama.redis.sample.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

	private static Logger logger = LoggerFactory.getLogger(SessionController.class);

	@RequestMapping(value = "/session")
	public String login(HttpServletRequest request, HttpSession session) {

		if (session.getAttribute("username") == null) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (username == null || password == null) {
				 return "Need username and password. Usage /session?username={username}&password={password}";
			} else {
				session.setAttribute("username", username);
				session.setAttribute("password", password);
			}
		}
		
		logger.info("username:" + session.getAttribute("username") + ", password="
				+ session.getAttribute("password"));
		return "username:" + session.getAttribute("username") + ", password="
				+ session.getAttribute("password");
	}
}