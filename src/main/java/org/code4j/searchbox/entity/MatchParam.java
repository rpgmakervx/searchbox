package org.code4j.searchbox.entity;

import org.code4j.searchbox.entity.enums.MatchType;
import org.code4j.searchbox.entity.enums.OperatorType;

/**
 * @author xingtianyu(code4j) Created on 2017-11-20.
 */
public class MatchParam extends Param {

    private Integer slop;

    private String minShouldMatch;

    private MatchType mType;

    private OperatorType opType;

    public MatchParam(String key, Object value) {
        super(key, value);
        mType = MatchType.BOOLEAN;
        opType = OperatorType.OR;
    }

    public MatchParam(String key, Object value, Float boost) {
        super(key, value, boost);
        mType = MatchType.BOOLEAN;
        opType = OperatorType.OR;
    }
}
