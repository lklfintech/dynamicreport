宝莲灯通用报表系统（Dynamic Report），内置表单相关组件和图形模板，方便报表开发者通过配置快速完成报表需求开发。

# 主要功能
* 数据源配置：支持mysql、hive；oracle正在研发中；
* 报表查询SQL配置：定义报表展示的查询语句；
* 报表查询参数配置：定义报表查询的参数；
* 报表表单组件配置：定义报表展示中查询条件及显示方式，如文本框、下拉框、日期框；
* 报表表格组件配置：定义报表展示的方式，如表格、图形，以及组件中的维度和指标定义；
* 报表图形组件配置：将定义好的报表组件按需要进行组合，组件可在不同报表中重复使用，形成最终报表

# 准备开始
以mysql数据库为例：
* 下载工程
* 创建mysql实例  
*       create database dynamicreport default charset utf8mb4 COLLATE utf8mb4_general_ci;  
* 执行脚本dynamicreport.sql，脚本路径：dynamicreport.parent\dynamicreport.feature.provider\src\main\resources
* 启动工程  
  1）编译器中启动，如eclipse或IDEA，打开dynamicreport.feature.provider\src\main\java\com\lakala\dynamicreport\Application.java，右键->run Application  
  2）服务器启动，首先，使用maven，通过dynamicreport.parent/pom.xml打包；然后拷贝至服务器中任何目录解压；然后进入dynamicreport.parent\dynamicreport.feature.provider\bin目录，使用脚本start.sh启动 
  
