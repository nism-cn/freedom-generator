-- db built-in
INSERT INTO FG_TYPE (ID, MOLD, NAME, VAL)
VALUES (1001, 'DB', 'built-in', 'char'),
       (1002, 'DB', 'built-in', 'varchar'),
       (1003, 'DB', 'built-in', 'nvarchar'),
       (1004, 'DB', 'built-in', 'varchar2'),
       (1005, 'DB', 'built-in', 'tinytext'),
       (1006, 'DB', 'built-in', 'text'),
       (1007, 'DB', 'built-in', 'mediumtext'),
       (1008, 'DB', 'built-in', 'longtext'),
       (1009, 'DB', 'built-in', 'datetime'),
       (1010, 'DB', 'built-in', 'time'),
       (1011, 'DB', 'built-in', 'date'),
       (1012, 'DB', 'built-in', 'timestamp'),
       (1013, 'DB', 'built-in', 'tinyint'),
       (1014, 'DB', 'built-in', 'smallint'),
       (1015, 'DB', 'built-in', 'mediumint'),
       (1016, 'DB', 'built-in', 'int'),
       (1017, 'DB', 'built-in', 'bit'),
       (1018, 'DB', 'built-in', 'bigint'),
       (1019, 'DB', 'built-in', 'float'),
       (1020, 'DB', 'built-in', 'double'),
       (1021, 'DB', 'built-in', 'decimal'),
       (1022, 'DB', 'built-in', 'blob'),
       (1023, 'DB', 'built-in', 'integer'),
       (1024, 'DB', 'built-in', 'boolean');

-- java built-in
INSERT INTO FG_TYPE (ID, MOLD, NAME, VAL)
VALUES (2001, 'JAVA', 'built-in', 'String'),
       (2002, 'JAVA', 'built-in', 'Integer'),
       (2003, 'JAVA', 'built-in', 'Long'),
       (2004, 'JAVA', 'built-in', 'Double'),
       (2005, 'JAVA', 'built-in', 'BigDecimal'),
       (2006, 'JAVA', 'built-in', 'Date');

-- html built-in
INSERT INTO FG_TYPE (ID, MOLD, NAME, VAL)
VALUES (3001, 'HTML', 'built-in', 'input'),
       (3002, 'HTML', 'built-in', 'textarea'),
       (3003, 'HTML', 'built-in', 'select'),
       (3004, 'HTML', 'built-in', 'radio'),
       (3005, 'HTML', 'built-in', 'checkbox'),
       (3006, 'HTML', 'built-in', 'date');

-- sql built-in
INSERT INTO FG_TYPE (ID, MOLD, NAME, VAL)
VALUES (4001, 'SQL', 'built-in', 'eq'),
       (4002, 'SQL', 'built-in', 'ne'),
       (4003, 'SQL', 'built-in', 'gt'),
       (4004, 'SQL', 'built-in', 'ge'),
       (4005, 'SQL', 'built-in', 'lt'),
       (4006, 'SQL', 'built-in', 'le'),
       (4007, 'SQL', 'built-in', 'like'),
       (4008, 'SQL', 'built-in', 'likeLeft'),
       (4009, 'SQL', 'built-in', 'likeRight'),
       (4010, 'SQL', 'built-in', 'notLike'),
       (4011, 'SQL', 'built-in', 'notLikeLeft'),
       (4012, 'SQL', 'built-in', 'notLikeRight'),
       (4013, 'SQL', 'built-in', 'in'),
       (4014, 'SQL', 'built-in', 'notIn');

-- DB map java
INSERT INTO FG_TYPE_MAP (ID, TYPE_MOLD, MAP_MOLD, TYPE_ID, MAP_ID)
VALUES (10001, 'DB', 'JAVA', 1001, 2001),
       (10002, 'DB', 'JAVA', 1002, 2001),
       (10003, 'DB', 'JAVA', 1003, 2001),
       (10004, 'DB', 'JAVA', 1004, 2001),
       (10005, 'DB', 'JAVA', 1005, 2001),
       (10006, 'DB', 'JAVA', 1006, 2001);

-- DB map html
INSERT INTO FG_TYPE_MAP (ID, TYPE_MOLD, MAP_MOLD, TYPE_ID, MAP_ID)
VALUES (30001, 'DB', 'HTML', 1001, 3001),
       (30002, 'DB', 'HTML', 1002, 3001),
       (30003, 'DB', 'HTML', 1003, 3001),
       (30004, 'DB', 'HTML', 1004, 3001),
       (30005, 'DB', 'HTML', 1005, 3001),
       (30006, 'DB', 'HTML', 1006, 3001);