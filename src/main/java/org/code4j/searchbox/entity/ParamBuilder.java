package org.code4j.searchbox.entity;

/**
 * @author xingtianyu(code4j) Created on 2017-11-20.
 */
public class ParamBuilder {

    public TermParam term(String key,String value){
        return new TermParam(key,value);
    }

    public TermsParam terms(String key,Object... value){
        return new TermsParam(key,value);
    }
}
