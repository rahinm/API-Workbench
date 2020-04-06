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

package net.dollmar.web.apiworkbench.filters;

import spark.FilterImpl;
import spark.Request;
import spark.Response;
import spark.utils.SparkUtils;

import static spark.Spark.halt;

import javax.xml.bind.DatatypeConverter;

import net.dollmar.web.apiworkbench.data.Users;

public class BasicAuthenticationFilter extends FilterImpl
{
	private static final String AUTH_HEADER = "Authorization";
	
	private static final String BASIC_AUTHENTICATION_TYPE = "Basic";

	private static final int NUMBER_OF_AUTHENTICATION_FIELDS = 2;

	private static final String ACCEPT_ALL_TYPES = "*";


	public BasicAuthenticationFilter()
	{
		this(SparkUtils.ALL_PATHS);
	}

	public BasicAuthenticationFilter(final String path)
	{
		super(path, ACCEPT_ALL_TYPES);
	}

	@Override
	public void handle(final Request request, final Response response)
	{
		String authHeaderValue = request.headers(AUTH_HEADER);
		final String encodedHeader = (authHeaderValue == null)
				? null
				: ((authHeaderValue.startsWith(BASIC_AUTHENTICATION_TYPE))
						?  authHeaderValue.substring(BASIC_AUTHENTICATION_TYPE.length())
						: null);

		if (notAuthenticatedWith(credentialsFrom(encodedHeader))) {
			response.header("WWW-Authenticate", BASIC_AUTHENTICATION_TYPE);
			halt(401);
		}
	}

	private String[] credentialsFrom(final String encodedHeader)
	{
		return (encodedHeader != null)
				? decodeHeader(encodedHeader).split(":")
				: null;
	}

	private String decodeHeader(final String encodedHeader)
	{
		return new String(DatatypeConverter.parseBase64Binary(encodedHeader));
	}

	private boolean notAuthenticatedWith(final String[] credentials)
	{
		return !authenticatedWith(credentials);
	}

	private boolean authenticatedWith(final String[] credentials)
	{
		return (credentials != null && credentials.length == NUMBER_OF_AUTHENTICATION_FIELDS) 
			? new Users().authenticateUser(credentials[0], credentials[1])
			: false;		
	}
}
