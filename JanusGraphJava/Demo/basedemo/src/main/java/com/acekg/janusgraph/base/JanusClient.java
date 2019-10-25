package com.acekg.janusgraph.base;

import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.janusgraph.core.JanusGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public enum JanusClient {
    INSTANCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(JanusClient.class);
    private static String propFileName = "conf/remote-graph.properties";

    private JanusGraph      janusgraph;
    private Cluster         cluster;
    private Client          client;
    private Configuration   conf;

    private JanusClient() { }
    public void openGraph() throws ConfigurationException {
        LOGGER.info("opening graph");
        conf = new PropertiesConfiguration(propFileName);

        try {
            cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
            client = cluster.connect();
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    public void closeGraph() throws Exception {
        LOGGER.info("closing graph");
        try {
            if(cluster != null) cluster.close();
        } finally {
            client = null;
            cluster = null;
        }
    }

    public void submitCodes(final String str, Consumer<? super String> action) {
        final ResultSet resultSet = client.submit(str);
        Stream<Result> futureList = resultSet.stream();
        futureList.map(Result::toString).forEach(action);
    }

    public void submitCodes(final String str, Map<String, Object> map, Consumer<? super String> action) {
        final ResultSet resultSet = client.submit(str, map);
        Stream<Result> futureList = resultSet.stream();
        futureList.map(Result::toString).forEach(action);
    }

}
