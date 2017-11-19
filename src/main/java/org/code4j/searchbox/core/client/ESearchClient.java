package org.code4j.searchbox.core.client;

import org.apache.commons.lang3.StringUtils;
import org.code4j.searchbox.conf.AppConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.plugin.deletebyquery.DeleteByQueryPlugin;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * @author xingtianyu(code4j) Created on 2017-11-18.
 */
public class ESearchClient {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ESearchClient.class.getName());
    private static final String CLUSTER_NAME = "cluster.name";
    private static final String ES_SERVICES = "es.services";
    private static Client client;

    public static Client getClient(){
        try {
            String clusterName = AppConfig.getProperty(CLUSTER_NAME);
            String services = AppConfig.getProperty(ES_SERVICES);
            logger.debug("es.services:{}" , services);
            logger.debug("clusterName:{}" , clusterName);
            Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName)
                    .put("client.transport.sniff", true).put("client.transport.ignore_cluster_name", true)
                    .put("client.transport.ping_timeout", "1s").put("client.transport.nodes_sampler_interval", "1s")
                    .build();
            // add delete-by-query plugin
            TransportClient c = TransportClient.builder().settings(settings).addPlugin(DeleteByQueryPlugin.class)
                    .build();
            String[] servicesArray;
            if (StringUtils.isNotBlank(services)) {
                servicesArray = services.split(",");
                for (String service : servicesArray) {
                    String[] serviceInfo = service.split(":");
                    if (serviceInfo.length > 1) {
                        c = c.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serviceInfo[0]),
                                Integer.valueOf(serviceInfo[1])));
                    }
                }
                client = c;
                logger.info("connect to es cluster success.");
            } else {
                logger.error(" has no services info.");
                return null;
            }

        } catch (Exception e) {
            logger.error("create es client failed.", e);
            return null;
        }
        return client;
    }

}
