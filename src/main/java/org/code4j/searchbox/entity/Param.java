package org.code4j.searchbox.entity;

import java.util.Date;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class Param {

    private String key;

    private Object value;

    private Float boost;

    public Param(String key, Object value) {
        this.key = key;
        this.value = value;
    }
    public Param(String key, Object value, Float boost) {
        this.key = key;
        this.value = value;
        this.boost = boost;
    }

    public Object getValue(){
        return value;
    }

    public String getKey(){
        return key;
    }

    public Float getBoost(){
        return this.boost;
    }

    public Param boost(Float boost){
        this.boost = boost;
        return this;
    }
}
