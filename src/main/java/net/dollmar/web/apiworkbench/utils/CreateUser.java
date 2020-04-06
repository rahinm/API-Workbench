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

package net.dollmar.web.apiworkbench.utils;

import java.io.Console;
import java.util.Arrays;

import net.dollmar.web.apiworkbench.data.Users;

public class CreateUser {


	public static void main(String[] args) {

		System.out.println("\n*** DepCon: You are about to create a new user ***\n");

		Console console = System.console();
		if (console != null) {
			Users u = new Users();

			String userName = console.readLine("Enter user name: ");
			if (u.existUser(userName)) {
				String choice = console.readLine(String.format("   *** User '%s' already exist. Overwirte [Y/N]: ", userName));
				if (!choice.startsWith("Y") && !choice.startsWith("y")) {
					return;
				}
			}
			char[] password = console.readPassword("Enter password: ");

			try {
				u.createUser(userName, password);

				Arrays.fill(password, ' ');
			}
			catch (Exception e) {
				System.err.println(String.format("ERROR: Failed to create new user [Reason: %s]", e.getMessage()));
			}
		}
		else {
			System.err.println("ERROR: No Console available.");
		}
	}
}