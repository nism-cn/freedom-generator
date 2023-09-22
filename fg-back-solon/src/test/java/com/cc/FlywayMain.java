package com.cc;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class FlywayMain {
    public static void main(String[] args) {
        FluentConfiguration configure = Flyway.configure();
    }
}
