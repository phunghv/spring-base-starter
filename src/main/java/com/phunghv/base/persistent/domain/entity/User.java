package com.phunghv.base.persistent.domain.entity;

import com.phunghv.base.persistent.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    String name;
}
