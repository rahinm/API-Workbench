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

package net.dollmar.web.apiworkbench.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

public class Users {
	
	private static final String USERS_FILE = "config/users.dat";
	private static final String FIELD_SEP = "/";
	
	
	
	private Properties users = new Properties();
	private File usersFile = new File(USERS_FILE);

	public Users() {
		File configDir = usersFile.getParentFile();
		if (!configDir.exists()) {
			configDir.mkdirs();
		}
	}
	
	private void readUsers() {
		if (users != null) {
			users.clear();
		}
		try (InputStream input = new FileInputStream(usersFile)) {
			users.load(input);
		}
		catch (IOException e) {
			System.err.println(String.format("WARN: Failed to load users data [Reason: %s]", e.getMessage()));
		}
	}

	private String calculateHash(String data) throws NoSuchAlgorithmException {
		Objects.requireNonNull(data);
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] encodedHash = md.digest(data.getBytes(StandardCharsets.UTF_8));
		
		return DatatypeConverter.printHexBinary(encodedHash);
	}
	
	public boolean existUser(final String userName) {
		Objects.requireNonNull(userName);
		readUsers();
		
		return users.getProperty(userName) != null;
	}
	
	
	public boolean authenticateUser(final String userName, final String password) {
		// With the next call we'd reload list of users 
		if (!existUser(userName)) {
			return false;
		}
		String[] parts = users.getProperty(userName).split(FIELD_SEP);
		
		try {
			return parts[1].equalsIgnoreCase(calculateHash(parts[0] + FIELD_SEP + userName + FIELD_SEP + password));
		}
		catch (NoSuchAlgorithmException e) {
			return false;
		}
	}
	
	
	
	public void createUser(final String userName, final char[] password) throws Exception {
		Objects.requireNonNull(userName);
		Objects.requireNonNull(password);
		
		byte bytes[] = new byte[10];
		new SecureRandom().nextBytes(bytes);
		String salt = DatatypeConverter.printHexBinary(bytes);
		
		String hash = calculateHash(salt + FIELD_SEP + userName + FIELD_SEP + new String(password));

		readUsers();
		users.put(userName, salt + FIELD_SEP + hash);
			
		try (OutputStream output = new FileOutputStream(usersFile)) {
			users.store(output, "DepCon users list ==> *** DO NOT EDIT BY HAND ***");
		} 
	}
}
