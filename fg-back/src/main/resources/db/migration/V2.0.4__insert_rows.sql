INSERT INTO F_CONFIG (ID, `TYPE`, NAME, `KEY`, VAL, REMARK)
VALUES
    (1, 'OUT_PATH', 'java默认', '.*\\.java$', '/src/main/$${sets.pkg?replace(".","/")}/$${table.upCamel}.java', '请用正则表达式'),
    (2, 'OUT_PATH', 'controller', '.*controller\\.java$', '/src/main/$${sets.pkg?replace(".","/")}/controller/$${table.upCamel}Controller.java', '控制层'),
    (3, 'OUT_PATH', 'service', '.*service\\.java$', '/src/main/$${sets.pkg?replace(".","/")}/service/$${table.upCamel}Service.java', '业务层'),
    (4, 'OUT_PATH', 'serviceImpl', '.*serviceImpl\\.java$', '/src/main/$${sets.pkg?replace(".","/")}/service/impl/$${table.upCamel}ServiceImpl.java', '业务层实现'),
    (5, 'OUT_PATH', 'mapper', '.*mapper\\.java$', '/src/main/$${sets.pkg?replace(".","/")}/mapper/$${table.upCamel}Mapper.java', '数据层'),
    (6, 'OUT_PATH', 'mapper.xml', '.*mapper\\.xml$', '/src/main/resources/mapper/$${table.upCamel}Mapper.xml', '数据层配置'),
    (7, 'OUT_PATH', 'domain', '.*domain\\.java$', '/src/main/$${sets.pkg?replace(".","/")}/domain/$${table.upCamel}.java', '实体'),
    (8, 'OUT_PATH', 'js', '.*api\\.js$', '/ui/apis/$${table.camel}Api.js', '接口'),
    (9, 'OUT_PATH', 'vue', '.*\\.vue$', '/ui/views/$${table.camel}/index.vue', 'vue页面');

-- 处理 flyway $${xx} 异常
UPDATE F_CONFIG SET VAL = REPLACE(VAL, '$$', '$');

