API Workbench
=============

API Workbench is a a simple web-based application supporting your REST API development activities.

API Workbench embeds swagger-editor and swagger-ui tools to provide you creating, editing and testing
your APIs. You can also use API Workbench to store and manage your API definitions.

API Workbench is built using the SparkJava micro-framework and JPA/Hibernate. It uses Apache Derby as an 
embedded database.

Running
-------
You have a single fat jar file `api-workbench.jar`. Run this application using the command below,

`java -jar api-workbench.jar`

When run the application will create a directory `data` where the embedded database files will be saved.
 
Configuration
-------------
You may set the following API Workbench specific Java properties in a file `API Workbench.properties` in 
the `config` directory relative to where `api-workbench.jar` resides.

| Property Name                      | Description                                 | Note                              |
|------------------------------------|-------------------------------------------- |-----------------------------------|
| apiworkbench.listener.port         | listener port number                        | Optional [default: 10080]         |
| apiworkbench.network.security      | set to true to enable TLS transport         | Optional [default: false]         |
| apiworkbench.keystore.filename     | Java keystore file name for TLS support     | Conditional [when TLS is enabled] |
| apiworkbench.keystore.password     | Password for the Java keystore file         | Conditional [when TLS is enabled] |

Users Authentication
--------------------
API Workbench enforces HTTP Basic Authorization to authenticate users. You can use the below command to create users.
(The below assumes you are running the command in the directory where `api-workbench.jar` is present).

`java -cp api-workbench.jar net.dollmar.web.apiworkbench.utils.CreateUser`

Answer few questions and user identities will be created in a file `config/users.dat`.

License
-------
This application is free to use by anyone without any restrcition and is released as a open source software under 
Apache License Version 2.0. Please browse to https://www.apache.org/licenses/LICENSE-2.0 for details of the provisions 
of this license.



