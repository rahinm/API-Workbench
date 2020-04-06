/*
Copyright 2017 Mohammad A. Rahin                                                                                                          

Licensed under the Apache License, Version 2.0 (the "License");                                                                           
you may not use this file except in compliance with the License.                                                                          
You may obtain a copy of the License at                                                                                                   
    http://www.apache.org/licenses/LICENSE-2.0                                                                                            
Unless required by applicable law or agreed to in writing, software                                                                       
distributed under the License is distributed on an "AS IS" BASIS,                                                                         
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                                                                  
See the License for the specific language governing permissions and                                                                       
limitations under the License.       
*/

package net.dollmar.web.apiworkbench;


import static spark.Spark.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.util.Properties;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dollmar.web.apiworkbench.dao.ApiDefDao;
import net.dollmar.web.apiworkbench.filters.BasicAuthenticationFilter;
import net.dollmar.web.apiworkbench.filters.Filters;
import net.dollmar.web.apiworkbench.pages.APIDefsViewPage;
import net.dollmar.web.apiworkbench.utils.EntityBuilder;
import net.dollmar.web.apiworkbench.utils.Utils;
import net.dollmar.web.apiworkbench.utils.WorkbenchException;
import spark.utils.IOUtils;



public class Main {

	public static final int DEFAULT_PORT = 10080;
	public static final String SVC_NAME = "APIWorkbench";
	public static final String SVC_VERSION = "v1";

	public static final String HOME_PATH = String.format("/%s/%s", Main.SVC_NAME, "home");
	public static final String EDIT_PATH = String.format("/%s/%s", Main.SVC_NAME, "edit");
	public static final String TEST_PATH = String.format("/%s/%s", Main.SVC_NAME, "test");
  public static final String APIS_PATH = String.format("/%s/%s", Main.SVC_NAME, "apis");
	public static final String IMPORT_PATH = String.format("/%s/%s", Main.SVC_NAME, "import");

	private static final String API_DIR = "data/apis";
	private static final String CFG_FILE = "config/APIWorkbench.properties";

	private static Logger logger = LoggerFactory.getLogger(Main.class);


	private static void configure() {
		File configDir = new File(CFG_FILE).getParentFile();
		if (!configDir.exists()) {
			configDir.mkdirs();
		}		
		Properties props = new Properties();
		try (InputStream input = new FileInputStream(CFG_FILE)) {
			props.load(input);
			for (String p : props.stringPropertyNames()) {
				System.setProperty(p, props.getProperty(p));
			}
		}
		catch (IOException e) {
			System.err.println(String.format("WARN: Failed to load configuration data [Reason: %s]", e.getMessage()));
		}		
	}

	public static void main(String[] args) {
		configure();
		String hostName = "localhost";
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		}
		catch (UnknownHostException e) {
			// DO nothing
		}
		String scheme = "http";
		int serverPort = Integer.getInteger("depcon.listener.port", DEFAULT_PORT);
		port(serverPort);
		if (Boolean.getBoolean("depcon.network.security")) {
			// disable insecure algorithms
			Security.setProperty("jdk.tls.disabledAlgorithms",
					"SSLv3, TLSv1, TLSv1.1, RC4, MD5withRSA, DH keySize < 1024, EC keySize < 224, DES40_CBC, RC4_40, 3DES_EDE_CBC");
			
			String keyStoreName = System.getProperty("apiworkbench.keystore.filename");
			String keyStorePassword = System.getProperty("apiworkbench.keystore.password");

			if (Utils.isEmptyString(keyStoreName) || Utils.isEmptyString(keyStorePassword)) {
				System.err.println("ERROR: Keystore name or password is not set.");
				return;
			}
			scheme = "https";
			secure(keyStoreName, keyStorePassword, null, null);
		}

		String baseUrl = String.format("%s://%s:%d/APIWorkbench/", scheme, hostName, serverPort);
		
		// root location for static pages (e.g. API Docs)
		staticFiles.location("/static");

		logger.info(String.format("Starting server on port: %d", serverPort ));
		logger.info("Application available at ==> " + baseUrl); 

		// routes

		before(new BasicAuthenticationFilter("*"));

    
    get(APIS_PATH, (req, resp) -> {
      resp.status(200);
      return new APIDefsViewPage().render(req.queryMap().toMap());
    });

    get(APIS_PATH + "/:apiFilename", (req, resp) -> {
      String apiFilename = req.params(":apiFilename");
      File apiFile = new File(API_DIR, apiFilename);
      if (!apiFile.exists()) {
        resp.status(404);
        return String.format("Error: API file '%s' does not exist", apiFilename);  
      }
      String content = new String(Files.readAllBytes(Paths.get(API_DIR, apiFilename)));
      resp.type("application/text");
      resp.status(200);
      
      return content;
    });   

		post(IMPORT_PATH, (req, resp) -> {
			req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(API_DIR));

			Part apiNamePart = req.raw().getPart("api_name");
			Part apiDescPart = req.raw().getPart("api_description");
			Part apiVersionPart = req.raw().getPart("api_version");
			Part apiFilePart = req.raw().getPart("api_file");

			String apiName = Utils.partToString(apiNamePart);
			String apiDesc = Utils.partToString(apiDescPart);
			String apiVersion = Utils.partToString(apiVersionPart);

			if (Utils.isEmptyString(apiName) 
					|| Utils.isEmptyString(apiVersion) 
					|| Utils.isEmptyString(apiFilePart.getSubmittedFileName())) {
				resp.status(400);
				return Utils.buildStyledHtmlPage("Error", "Missing input parameter");
			}
			File tmpDir = new File(API_DIR);
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}
			File importFile = new File(API_DIR, Utils.buildVersionedFilename(apiName, apiFilePart.getSubmittedFileName(), apiVersion));
			try (InputStream inputStream = apiFilePart.getInputStream()) {
				OutputStream outputStream = new FileOutputStream(importFile);
				IOUtils.copy(inputStream, outputStream);
				outputStream.close();
			}
			
			// persist a new API definition item in the database
			ApiDefDao dao = new ApiDefDao();
			
			if (dao.getApiDef(apiName, apiVersion) != null) {
			  throw new WorkbenchException(String.format("API defition for '%s:%s' has already been imported", apiName, apiVersion));
			}
			dao.saveApiDef(EntityBuilder.buildApiDef(apiName, apiDesc, apiVersion, importFile.getName()));
			
			return Utils.buildStyledHtmlPage("Success", "File successfully uploaded and imported into database");
		});

		//Set up after-filters (called after each get/post)
		after("*", Filters.addGzipHeader);		
	}
}
