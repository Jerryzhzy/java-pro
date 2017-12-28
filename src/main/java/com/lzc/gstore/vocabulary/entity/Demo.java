package com.lzc.gstore.vocabulary.entity;

import com.lzc.core.datasource.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by ziyu.zhang on 2017/6/30.
 */
@Entity
public class Demo extends IdEntity implements Serializable {

    private String name;

    @Column(name="NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
