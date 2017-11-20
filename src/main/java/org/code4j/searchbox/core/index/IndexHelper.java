package org.code4j.searchbox.core.index;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.code4j.searchbox.core.client.ESearchClient;
import org.elasticsearch.action.ActionWriteResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.VersionType;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xingtianyu(code4j) Created on 2017-11-18.
 */
public class IndexHelper {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(IndexHelper.class.getName());
    private static Client client = ESearchClient.getClient();

    private static final int DEFAULT_RETRY_TIMES = 10;

    private static ResponseBuilder builder = new ResponseBuilder();

    public static IndexResult index(String index,IndexEntity entity){
        IndexResponse indexResponse = builder.indexResponse(index,entity);
        String id = null;
        if (indexResponse != null) {
            ActionWriteResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo != null && shardInfo.getSuccessful() > 0) {
                id = indexResponse.getId();
            }
        }
        logger.info("INDEX info to token={} type={} data={} result={}", index, entity.getType(), entity.getSource(), id);
        return IndexResult.buildResult(indexResponse);
    }

    public static IndexResult update(String index, IndexEntity entity){
        UpdateResponse updateResponse = builder.updateResponse(index,entity);
        String id = null;
        if (updateResponse != null) {
            ActionWriteResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            if (shardInfo != null) {
                id = updateResponse.getId();
            }
        }
        logger.info("UPDATE info to token={} type={} data={} result={}", index, entity.getType(), entity.getSource(), id);
        return IndexResult.buildResult(updateResponse);
    }

    public static IndexResult upsert(String index, IndexEntity entity){
        try {
            UpdateResponse updateResponse = builder.upsertResponse(index,entity);
            String id = null;
            if (updateResponse != null) {
                ActionWriteResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
                if (shardInfo != null) {
                    id = updateResponse.getId();
                }
            }
            logger.info("UPSERT info to token={} type={} data={} result={}", index, entity.getType(), entity, id);
            return IndexResult.buildResult(updateResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BulkResult bulkIndex(String index,List<IndexEntity> entities){
        BulkResponse bulkResponse  = builder.prepareBulk(index,entities,BulkMode.INDEX);
        return BulkResult.buildResult(bulkResponse);
    }

    public static BulkResult bulkUpdate(String index,List<IndexEntity> entities){
        BulkResponse bulkResponse  = builder.prepareBulk(index,entities,BulkMode.UPDATE);
        return BulkResult.buildResult(bulkResponse);
    }

    public static BulkResult bulkUpsert(String index,List<IndexEntity> entities){
        BulkResponse bulkResponse  = builder.prepareBulk(index,entities,BulkMode.UPSERT);
        return BulkResult.buildResult(bulkResponse);
    }

    public static DeleteResult delete(String index, String type, String id){
        DeleteResponse deleteResponse = builder.deleteResponse(index,type,id);
        return DeleteResult.buildResult(deleteResponse);
    }


    static class ResponseBuilder{

        private void checkLegal(String indice, IndexEntity entity) {
            Preconditions.checkNotNull(client, "client must be provided");
            Preconditions.checkNotNull(entity, "IndexData must be provided");
            Preconditions.checkNotNull(entity.getSource(), "being indexed data must be provided");
            Preconditions.checkNotNull(indice, "indice must be provided");
            Preconditions.checkNotNull(entity.getType(), "type must be provided");
        }

        private void checkLegal(String indice, String type, String id) {
            Preconditions.checkNotNull(client, "client must be provided");
            Preconditions.checkNotNull(id, "id must be provided");
            Preconditions.checkNotNull(indice, "indice must be provided");
            Preconditions.checkNotNull(type, "type must be provided");
        }

        private IndexRequestBuilder prepareIndex(String index, IndexEntity entity){
            IndexRequestBuilder builder = client.prepareIndex(index,entity.getType());
            if (!Strings.isNullOrEmpty(entity.getId())){
                builder.setId(entity.getId());
            }
            if (!Strings.isNullOrEmpty(entity.getRouting())){
                builder.setRouting(entity.getRouting());
            }
            if (entity.getVersion() != null){
                builder.setVersion(entity.getVersion());
                builder.setVersionType(VersionType.EXTERNAL);
            }
            builder.setSource(entity.getSource());
            return builder;
        }

        private UpdateRequestBuilder prepareUpdate(String index,IndexEntity entity){
            Preconditions.checkNotNull(entity.getId(), "id must be provided");
            UpdateRequestBuilder builder = client.prepareUpdate(index, entity.getType(), entity.getId());
            builder.setDoc(entity.getSource());
            if (StringUtils.isNotBlank(entity.getRouting())) {
                builder.setRouting(entity.getRouting());
            }
            if (entity.getVersion() != null){
                builder.setVersion(entity.getVersion());
                builder.setVersionType(VersionType.EXTERNAL);
            }
            builder.setRetryOnConflict(DEFAULT_RETRY_TIMES);
            return builder;
        }

        private DeleteRequestBuilder prepareDelete(String index,String type,String id){
            return client.prepareDelete(index,type,id);
        }

        private UpdateRequest prepareUpsert(String index, IndexEntity entity){
            // the unique id of data must be provided
            Preconditions.checkNotNull(entity.getId(), "data id must be provided");
            IndexRequest indexRequest = new IndexRequest(index, entity.getType(), entity.getId()).source(entity.getSource());
            UpdateRequest updateRequest = new UpdateRequest(index, entity.getType(), entity.getId()).doc(entity.getSource());
            // 避免version conflict
            updateRequest.retryOnConflict(5);
            if (StringUtils.isNotBlank(entity.getRouting())) {
                indexRequest.routing(entity.getRouting());
                updateRequest.routing(entity.getRouting());
            }
            if (entity.getVersion() != null){
                indexRequest.version(entity.getVersion());
                indexRequest.versionType(VersionType.EXTERNAL);
                updateRequest.version(entity.getVersion());
                updateRequest.versionType(VersionType.EXTERNAL);
            }
            updateRequest.upsert(indexRequest);
            return updateRequest;
        }

        private BulkResponse prepareBulk(String index, List<IndexEntity> list, BulkMode mode) {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (IndexEntity entity : list) {
                switch (mode){
                    case INDEX:
                        bulkRequest.add(prepareIndex(index,entity));
                        break;
                    case UPDATE:
                        bulkRequest.add(prepareUpdate(index,entity));
                        break;
                    case UPSERT:
                        bulkRequest.add(prepareUpdate(index,entity));
                        break;
                }
                logger.debug("build bulk{} Request info data list token={} data={}", mode, index, entity.getSource());
            }
            return bulkRequest.get();
        }

        public IndexResponse indexResponse(String index, IndexEntity entity){
            checkLegal(index,entity);
            return prepareIndex(index,entity).get();
        }

        public UpdateResponse updateResponse(String index, IndexEntity entity){
            checkLegal(index,entity);
            return prepareUpdate(index,entity).get();
        }

        public UpdateResponse upsertResponse(String index, IndexEntity entity) throws Exception{
            checkLegal(index, entity);
            return client.update(prepareUpsert(index, entity)).get();
        }

        public DeleteResponse deleteResponse(String index, String type,String id){
            checkLegal(index, type, id);
            return prepareDelete(index, type, id).get();
        }


    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));
    }
}
