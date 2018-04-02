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

package com.skcc.objectstorage.sample.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.jclouds.s3.S3Client;
import org.jclouds.s3.domain.BucketMetadata;
import org.jclouds.s3.domain.ListBucketResponse;
import org.jclouds.s3.domain.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

	private static Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Value("${services.objectstorage.bucket_name}")
	private String bucketName;

	@Autowired
	private S3Client s3Client;
	
	/**
	 * upload file.
	 * @param multipartFile File object
	 * @return file name
	 * @throws Exception if account is null
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<String, Object> upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		String fileName = System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename();
		logger.debug("saved file name :" + fileName);
		
		S3Object object = s3Client.newS3Object();
		object.getMetadata().setKey(fileName);
		object.setPayload(multipartFile.getInputStream());
		object.getPayload().getContentMetadata().setContentLength(multipartFile.getSize());
		s3Client.putObject(bucketName, object);

		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("thumb_img_path", fileName);
		return responseMap;
	}

	/**
	 * download file.
	 * @param fileName download file name
	 * @param response HttpServletResponse
	 * @throws Exception if account is null
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam("fileName") String fileName, HttpServletResponse response)
			throws Exception {
		S3Object s3Object = s3Client.getObject(bucketName, fileName);
		
		response.setContentType("application/octet-stream");
		response.setContentLength(s3Object.getMetadata().getContentMetadata().getContentLength().intValue());
		response.setHeader("Content-Disposition",
				"attachment; fileName=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		FileCopyUtils.copy(s3Object.getPayload().openStream(), response.getOutputStream());
	}

	/**
	 * list files.
	 * @param multipartFile File object
	 * @return file name
	 * @throws Exception if account is null
	 */
	@RequestMapping(value = "/files", method = RequestMethod.GET)
	public Map<String, String> list() throws Exception {
		Map<String, String> list = new HashMap<String, String>();
        ListBucketResponse response = s3Client.listBucket(bucketName);
        response.forEach(o -> {
        	list.put(o.getKey(), o.getUri().toString());
        	logger.info("bucket: " + o);
        });
        
        return list;
	}
	
	/**
	 * list buckets.
	 * @param multipartFile File object
	 * @return file name
	 * @throws Exception if account is null
	 */
	@RequestMapping(value = "/buckets", method = RequestMethod.GET)
	public Set<BucketMetadata> buckets() throws Exception {
        Set<BucketMetadata> list = s3Client.listOwnedBuckets();
        list.forEach(b -> {
        	logger.info("bucket: " + b);
        });
        
        return list;
	}
}
