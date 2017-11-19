package org.code4j.searchbox.core.index;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.update.UpdateResponse;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class DeleteResult extends IndexResult {

    private Boolean found;

    public Boolean isFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }



    public static DeleteResult buildResult(DeleteResponse deleteResponse){
        DeleteResult result = new DeleteResult();
        result.setId(deleteResponse.getId());
        result.setFound(deleteResponse.isFound());
        result.setIndex(deleteResponse.getIndex());
        result.setType(deleteResponse.getType());
        result.setVersion(deleteResponse.getVersion());
        return result;
    }
}
