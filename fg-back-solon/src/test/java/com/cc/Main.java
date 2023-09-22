package com.cc;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.GlobalDbConfig;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.GlobalDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.setting.Setting;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
//        Setting s = new Setting();
//        s.setByGroup("url", "group_db1", "jdbc:mysql://192.168.20.140:3306/sys");
//        s.setByGroup("username", "group_db1", "root");
//        s.setByGroup("password", "group_db1", "123456");
//
//        s.setByGroup("url", "group_db2", "jdbc:mysql://192.168.20.140:3306/ry-vue");
//        s.setByGroup("username", "group_db2", "root");
//        s.setByGroup("password", "group_db2", "123456");


        DSFactory ssss = DSFactory.setCurrentDSFactory(new HikariDSFactory());
        System.out.println(ssss);
        GlobalDSFactory.set(ssss);

        System.out.println(GlobalDSFactory.get());

        DSFactory factory = GlobalDSFactory.get();

        Setting dbSetting = GlobalDbConfig.createDbSetting();
        System.out.println(dbSetting);


//        dbSetting.setByGroup("username", "group_db2", "r");
//        dbSetting.store();
        System.out.println(dbSetting.load());

        DataSource group_db1 = DSFactory.get("group_db2");
        System.out.println(Db.use(group_db1).get(new Entity("sys_menu")));

//        DSFactory f1 = DSFactory.setCurrentDSFactory(new SimpleDSFactory(s.getSetting("group_db1")));
//        DSFactory f2 = DSFactory.setCurrentDSFactory(new SimpleDSFactory(s.getSetting("group_db2")));
//        DSFactory f3 = DSFactory.create(s.getSetting("group_db1"));
//        DSFactory f4 = DSFactory.create(s.getSetting("group_db2"));


//        DataSource dataSource = f1.getDataSource();
//        System.out.println(dataSource.toString());
//
//
//        dataSource = f2.getDataSource();
//        System.out.println(dataSource.toString());

        HikariDSFactory sbb = (HikariDSFactory) DSFactory.setCurrentDSFactory(new HikariDSFactory());

        GlobalDSFactory.set(sbb);

        System.out.println("555555555555555555555555555555555555555555555");
        System.out.println(GlobalDSFactory.get());
        System.out.println(sbb.getSetting());
        System.out.println("555555555555555555555555555555555555555555555");

        DataSource dataSource1 = GlobalDSFactory.get().getDataSource("group_db1");
        System.out.println(dataSource1);
        System.out.println(dataSource1);

    }

}
