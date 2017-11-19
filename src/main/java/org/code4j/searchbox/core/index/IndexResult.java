package org.code4j.searchbox.core.index;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class IndexResult {

    private String id;

    private String index;

    private String type;

    private Long version;

    private Boolean created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    public static IndexResult buildResult(IndexResponse indexResponse){
        IndexResult result = new IndexResult();
        result.id = indexResponse.getId();
        result.index = indexResponse.getIndex();
        result.type = indexResponse.getType();
        result.version = indexResponse.getVersion();
        result.created = indexResponse.isCreated();
        return result;
    }


    public static IndexResult buildResult(UpdateResponse updateResponse){
        IndexResult result = new IndexResult();
        result.index = updateResponse.getIndex();
        result.version = updateResponse.getVersion();
        result.created = updateResponse.isCreated();
        result.type = updateResponse.getType();
        result.id = updateResponse.getId();
        return result;
    }
}
