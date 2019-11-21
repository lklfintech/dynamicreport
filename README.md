Dynamics Report, built-in form-related components and graphics templates, makes it easy for report developers to quickly complete report requirements development through configuration.

# Key features
* Data source configuration: support for mysql, hive;
* Report SQL configuration: define the query statement of the report presentation;
* Report parameter configuration: define the parameters of the report query;
* Report form component configuration: define the query conditions and display in the report presentation, such as text boxes, drop-down boxes, date boxes;
* Report table Component Configuration: Define how reports are presented, such as definitions of dimensions and metrics in components;
* Report graphics component configuration: the defined report component will will be combined as needed, and the component can be reused in different reports to form the final report

# Ready to start
Take the mysql database as an example:  
* Download the project
* Create mysql instances  
*       create database dynamicreport default charset utf8mb4 COLLATE utf8mb4_general_ci;  
* Execute the script dynamicreport.sql, script path: dynamicreport.parent.dynamicreport.feature.the.provider.src-main.resources
* Start-up work  
  1) Start in the compiler, such as eclipse or IDEA, open dynamicreport.feature.the.src-main-java-com-lakala-dynamicreport-application.java, right-click-and-application run  
  2) Start in server. First, packaged with dynamicreport.parent/pom.xml with maven; then copied to any directory in the server and unzip tar.gz file; then go to dynamicreport.parent.dynamicreport.dynamicreport.feature.the.provider/bin directory, start with the script start.sh
  3) Url: http://localhost:8080/dynamicreport   username & password: admin