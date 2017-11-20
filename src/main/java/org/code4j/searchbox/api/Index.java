package org.code4j.searchbox.api;

import org.code4j.searchbox.core.IndexProcessor;
import org.code4j.searchbox.core.index.BulkResult;
import org.code4j.searchbox.core.index.IndexEntity;
import org.code4j.searchbox.core.index.IndexHelper;
import org.code4j.searchbox.core.index.IndexResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class Index<T> {

    private static Logger logger = LoggerFactory.getLogger(Index.class.getName());

    private IndexProcessor<T> processor;

    public Index() {
        this.processor = new IndexProcessor();
    }

    public IndexResult index(String index, T entity){
        try {
            IndexEntity indexEntity = processor.handle(entity);
            return IndexHelper.index(index,indexEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IndexResult update(String index,T entity){
        try {
            IndexEntity indexEntity = processor.handle(entity);
            return IndexHelper.update(index,indexEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IndexResult upsert(String index,T entity){
        try {
            IndexEntity indexEntity = processor.handle(entity);
            return IndexHelper.upsert(index,indexEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BulkResult bulkIndex(String index, List<T> entities){
        try {
            return IndexHelper.bulkIndex(index,processor.handleBulk(entities));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BulkResult bulkUpdate(String index, List<T> entities){
        try {
            return IndexHelper.bulkUpdate(index,processor.handleBulk(entities));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
