package net.dollmar.web.apiworkbench.utils;

import net.dollmar.web.apiworkbench.entity.ApiDef;

/*
Copyright 2020 Mohammad A. Rahin                                                                                                          

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

public class EntityBuilder {

  public static ApiDef buildApiDef(String apiName, String apiDesc, String version, String fileName) {
    ApiDef apiDef = new ApiDef();
    
    apiDef.setApiName(apiName);
    apiDef.setApiDesc(apiDesc);
    apiDef.setVersion(version);
    apiDef.setFileName(fileName);
    
    return apiDef;
  }  
}
