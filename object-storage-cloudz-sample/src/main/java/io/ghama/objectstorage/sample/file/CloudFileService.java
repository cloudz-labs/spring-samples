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

package io.ghama.objectstorage.sample.file;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

//@Profile({"dev", "stg", "prod"})
@Service("fileService")
public class CloudFileService implements IFileService {

	private static Logger logger = LoggerFactory.getLogger(CloudFileService.class);

	@Autowired
	private Account account;

	@Override
	public void upload(String fileName, InputStream inputStream) throws Exception {
		if (account == null) {
			throw new Exception("No Acount");
		}

		Container container = account.getContainer("images");
		if (!container.exists()) {
			container.create();
			container.makePublic();
		}

		StoredObject object = container.getObject(fileName);
		object.uploadObject(inputStream);
		logger.debug("saved file path :" + object.getPublicURL());
	}

	@Override
	public Map<String, Object> download(String fileName) throws Exception {
		if (account == null) {
			throw new Exception("No Acount");
		}

		Container container = account.getContainer("images");
		if (!container.exists()) {
			throw new Exception("File not found");
		}

		StoredObject object = container.getObject(fileName);
		
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("input", object.downloadObjectAsInputStream());
		info.put("length", (int) object.getContentLength());
		return info;
	}

	@Override
	public Map<String, String> files() throws Exception {
		if (account == null) {
			throw new Exception("No Acount");
		}

		Container container = account.getContainer("images");
		if (!container.exists()) {
			throw new Exception("Container not found");
		}

		Map<String, String> names = new HashMap<String, String>();
		Collection<StoredObject> lists = container.list();
//		lists.forEach((StoredObject object) -> {
//			names.put(object.getName(), object.getPublicURL());
//		});
		for (StoredObject object : lists) {
			names.put(object.getName(), object.getPublicURL());
		}
		
		return names;
	}

}
