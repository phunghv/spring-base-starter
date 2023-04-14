package com.phunghv.base.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBConfigData {

    String host = "localhost";
    String key = "default";
    int port = 3306;
    String username;
    String password;
    String databaseName;
}
