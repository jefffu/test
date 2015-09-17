package org.jfu.test.elasticsearch.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "search", name = "test")
@Service
public class SearchCommand implements Action {

    private static Logger logger = LoggerFactory.getLogger(SearchCommand.class);

    @Override
    public Object execute() throws Exception {
        logger.debug("======== In search test command.");
        Settings settings = ImmutableSettings.settingsBuilder().put("node.name", "Blitziana").build();
        Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("192.168.5.55", 9300));
        try {
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch("dam").setTypes("asset")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.matchQuery("filename", "test")) // Query
                    .setFrom(0).setSize(10);
            // .addFacet(termsFacetBuilder);
            logger.debug("======== search query {}", searchRequestBuilder.internalBuilder().toString());

            SearchResponse response = searchRequestBuilder.execute().actionGet();
            
        } finally {
            client.close();
        }

        return null;
    }

}
