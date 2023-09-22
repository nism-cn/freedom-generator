-- db built-in
INSERT INTO FG_TYPE (ID, MOLD, NAME, VAL)
VALUES (1001, 'DB', 'built-in', 'char111'),
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
       (2006, 'JAVA', 'built-in', 'Date'),
       (2007, 'JAVA', 'built-in', 'Boolean');

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
VALUES (20001, 'DB', 'JAVA', 1001, 2001), -- char -> String
       (20002, 'DB', 'JAVA', 1002, 2001), -- varchar -> String
       (20003, 'DB', 'JAVA', 1003, 2001), -- nvarchar -> String
       (20004, 'DB', 'JAVA', 1004, 2001), -- varchar2 -> String
       (20005, 'DB', 'JAVA', 1005, 2001), -- tinytext -> String
       (20006, 'DB', 'JAVA', 1006, 2001), -- text -> String
       (20007, 'DB', 'JAVA', 1007, 2001), -- mediumtext -> String
       (20008, 'DB', 'JAVA', 1008, 2001), -- longtext -> String
       (20009, 'DB', 'JAVA', 1009, 2006), -- datetime -> Date
       (20010, 'DB', 'JAVA', 1010, 2006), -- time -> Date
       (20011, 'DB', 'JAVA', 1011, 2006), -- date -> Date
       (20012, 'DB', 'JAVA', 1012, 2006), -- timestamp -> Date
       (20013, 'DB', 'JAVA', 1013, 2002), -- tinyint -> Integer
       (20014, 'DB', 'JAVA', 1014, 2002), -- smallint -> Integer
       (20015, 'DB', 'JAVA', 1015, 2002), -- mediumint -> Integer
       (20016, 'DB', 'JAVA', 1016, 2002), -- int -> Integer
       (20017, 'DB', 'JAVA', 1017, 2002), -- bit -> Integer
       (20018, 'DB', 'JAVA', 1018, 2002), -- bigint -> Integer
       (20019, 'DB', 'JAVA', 1019, 2004), -- float -> Double
       (20020, 'DB', 'JAVA', 1020, 2004), -- double -> Double
       (20021, 'DB', 'JAVA', 1021, 2005), -- decimal -> BigDecimal
       (20022, 'DB', 'JAVA', 1022, 2001), -- blob -> String
       (20023, 'DB', 'JAVA', 1023, 2004), -- integer -> Integer
       (20024, 'DB', 'JAVA', 1024, 2007); -- boolean -> Boolean

-- DB map html
INSERT INTO FG_TYPE_MAP (ID, TYPE_MOLD, MAP_MOLD, TYPE_ID, MAP_ID)
VALUES (30001, 'DB', 'HTML', 1001, 3001), -- char -> input
       (30002, 'DB', 'HTML', 1002, 3001), -- varchar -> input
       (30003, 'DB', 'HTML', 1003, 3001), -- nvarchar -> input
       (30004, 'DB', 'HTML', 1004, 3001), -- varchar2 -> input
       (30005, 'DB', 'HTML', 1005, 3001), -- tinytext -> input
       (30006, 'DB', 'HTML', 1006, 3002), -- text -> textarea
       (30007, 'DB', 'HTML', 1007, 3002), -- mediumtext -> textarea
       (30008, 'DB', 'HTML', 1008, 3002), -- longtext -> textarea
       (30009, 'DB', 'HTML', 1009, 3006), -- datetime -> date
       (30010, 'DB', 'HTML', 1010, 3006), -- time -> date
       (30011, 'DB', 'HTML', 1011, 3006), -- date -> date
       (30012, 'DB', 'HTML', 1012, 3006), -- timestamp -> date
       (30013, 'DB', 'HTML', 1013, 3001), -- tinyint -> input
       (30014, 'DB', 'HTML', 1014, 3001), -- smallint -> input
       (30015, 'DB', 'HTML', 1015, 3001), -- mediumint -> input
       (30016, 'DB', 'HTML', 1016, 3001), -- int -> input
       (30017, 'DB', 'HTML', 1017, 3001), -- bit -> input
       (30018, 'DB', 'HTML', 1018, 3001), -- bigint -> input
       (30019, 'DB', 'HTML', 1019, 3001), -- float -> input
       (30020, 'DB', 'HTML', 1020, 3001), -- double -> input
       (30021, 'DB', 'HTML', 1021, 3001), -- decimal -> input
       (30022, 'DB', 'HTML', 1022, 3002), -- blob -> textarea
       (30023, 'DB', 'HTML', 1023, 3001), -- integer -> input
       (30024, 'DB', 'HTML', 1024, 3001); -- boolean -> input