package org.code4j.searchbox.entity;

/**
 * @author xingtianyu(code4j) Created on 2017-11-20.
 */
public class TermsParam extends Param {

    public TermsParam(String key, Object... value) {
        super(key, value);
    }

    public TermsParam(Float boost,String key, Object... value) {
        super(key, value, boost);
    }
}
