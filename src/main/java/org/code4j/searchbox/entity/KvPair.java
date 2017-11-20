package org.code4j.searchbox.entity;

import java.util.Date;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class KvPair {

    private String key;

    private Object value;

    public KvPair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getValue(){
        return value;
    }

    public String getKey(){
        return key;
    }
}
