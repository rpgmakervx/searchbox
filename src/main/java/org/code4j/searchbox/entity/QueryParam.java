package org.code4j.searchbox.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class QueryParam {

    private final List<Param> mustClauses = new ArrayList<>();

    private final List<Param> mustNotClauses = new ArrayList<>();

    private final List<Param> filterClauses = new ArrayList<>();

    private final List<Param> shouldClauses = new ArrayList<>();



}
