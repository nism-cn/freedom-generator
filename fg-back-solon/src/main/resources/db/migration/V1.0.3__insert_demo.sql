insert into FG_DATABASE_INFO (ID, NAME, JDBC_URL, USERNAME, PASSWORD, REMARK)
values (1, '演示数据源(需修改)', 'jdbc:mysql://127.0.0.1:3306/db', 'root', '123456', '注! 请根据实际情况修改数据库连接,用户和密码');

insert into FG_PROJECT (ID, NAME)
values (1, '演示项目');

insert into FG_PROJECT_SETTING (ID, PROJECT_ID, DB_INFO_ID, TEMP_PATH, ROOT_PACKAGE, AUTHOR, DICT_SQL)
values (1, 1, 1, '/演示模板', 'org.nism.demo', 'author' , 'SELECT [字典名称] AS NAME, [字典类型] AS val FROM [字典表]');