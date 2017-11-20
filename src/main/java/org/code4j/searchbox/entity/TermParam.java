package org.code4j.searchbox.entity;

/**
 * @author xingtianyu(code4j) Created on 2017-11-20.
 */
public class TermParam extends Param {

    public TermParam(String key, Object value) {
        super(key, value);
    }

    public TermParam(String key, Object value, Float boost) {
        super(key, value, boost);
    }
}
