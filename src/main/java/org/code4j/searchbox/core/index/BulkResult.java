package org.code4j.searchbox.core.index;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class BulkResult {

    private Long count;

    private Boolean hasFailure;

    private String failureMsg;

    private List<IndexResult> failures;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<IndexResult> getFailures() {
        return failures;
    }

    public void setFailure(List<IndexResult> failures) {
        this.failures = failures;
    }

    public void addFailureId(IndexResult t){
        if (failures == null){
            this.failures = new ArrayList<>();
        }
        this.failures.add(t);
    }

    public static BulkResult buildResult(BulkResponse bulkResponse){
        BulkResult result = new BulkResult();
        result.count = bulkResponse.getTookInMillis();
        result.failureMsg = bulkResponse.buildFailureMessage();
        result.hasFailure = bulkResponse.hasFailures();
        BulkItemResponse[] itemResponses = bulkResponse.getItems();
        for (BulkItemResponse response:itemResponses){
            if (response.isFailed()) {
                IndexResponse resp = response.getResponse();
                result.addFailureId(IndexResult.buildResult(resp));
            }
        }
        return result;
    }
}
