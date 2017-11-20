package org.code4j.searchbox.core.search;

import org.elasticsearch.index.query.*;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class SearchHelper {

    public static void main(String[] args) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder termQuery = QueryBuilders.termQuery("","");
//        QueryBuilders.termsQuery()
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("name","邢天宇").minimumShouldMatch("100%");
        boolQueryBuilder.should(matchQueryBuilder);
        System.out.println(boolQueryBuilder.toString());
    }
}
