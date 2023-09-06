package org.nism.fg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动类
 *
 * @author inism
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FgApp {

    public static void main(String[] args) {
        SpringApplication.run(FgApp.class, args);
    }

}
