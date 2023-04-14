package com.phunghv.base.persistent.primary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "database_mappings")
@Getter
@Setter
public class DatabaseMapping extends PrimaryBaseEntity {

    @Column(name = "db_key")
    String dbKey;
    @Column(name = "db_name")
    String dbName;

    String host;
    Integer port;
    String user;
    String password;

    Boolean isDefault = false;
    Boolean active = true;
}
