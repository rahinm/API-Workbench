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


public class WorkbenchException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4042229320393117083L;

	private int code;
	
	public WorkbenchException(String msg) {
		super(msg);
	}
	
	public WorkbenchException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public WorkbenchException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public WorkbenchException(int code, String msg, Throwable cause) {
		super(msg, cause);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}