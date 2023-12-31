CREATE TABLE FG_DATABASE_INFO
(
    ID       BIGINT       NOT NULL COMMENT '主键',
    NAME     VARCHAR(255) NOT NULL COMMENT '名称',
    JDBC_URL VARCHAR(255) NOT NULL COMMENT '路径',
    USERNAME VARCHAR(100) COMMENT '用户',
    PASSWORD VARCHAR(100) COMMENT '密码',
    REMARK   VARCHAR(2000) COMMENT '描述',
    CONSTRAINT FG_DB_INFO_PK PRIMARY KEY (ID),
    CONSTRAINT FG_DB_INFO_UN UNIQUE (NAME)
);