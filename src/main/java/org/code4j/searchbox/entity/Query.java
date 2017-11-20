package org.code4j.searchbox.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class Query {

    private List<KvPair> match;

    private List<KvPair> matchPhase;

    private List<KvPair> must;

    private List<KvPair> mustNot;

    private List<KvPair> should;

    private String queryString;

}
