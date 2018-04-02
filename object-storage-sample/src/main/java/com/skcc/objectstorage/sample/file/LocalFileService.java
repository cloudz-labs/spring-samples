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

package com.skcc.objectstorage.sample.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Profile("default")
@Service("fileService")
public class LocalFileService implements IFileService {
	
	private static Logger logger = LoggerFactory.getLogger(LocalFileService.class);

	@Value("${file.save.dir}")
	private String fileSaveDir;

	@Override
	public void upload(String fileName, InputStream inputStream) throws Exception {
		FileOutputStream fos = null;
		
		try {
			File dir = new File(fileSaveDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			fos = new FileOutputStream(new File(dir, fileName));
			FileCopyUtils.copy(inputStream, fos);
		
		} catch (Exception e) {
			throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	@Override
	public Map<String, Object> download(String fileName) throws Exception {
		File file = new File(fileSaveDir, fileName);
		if (!file.exists()) {
			throw new Exception("No File");
		}
		
		InputStream is = new FileInputStream(file);
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("input", is);
		info.put("length", is.available());
		return info;
	}

	@Override
	public Map<String, String> files() throws Exception {
		File dir = new File(fileSaveDir);
		if (!dir.exists() || !dir.isDirectory()) {
			throw new Exception("No File");
		}
		
		Map<String, String> names = new HashMap<String, String>();
		File[] lists = dir.listFiles();
		for (File file : lists) {
			names.put(file.getName(), file.getPath());
		}
		
		return names;
	}

}
