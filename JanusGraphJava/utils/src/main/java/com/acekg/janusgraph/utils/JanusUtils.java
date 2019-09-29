package com.acekg.janusgraph.utils;

import java.util.stream.Stream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JanusUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JanusUtils.class);
	
	
	protected String				propFileName;
	protected JanusGraph			janusgraph;
	protected Cluster				cluster;
	protected Client				client;
	protected Configuration			conf;
	protected Graph					graph;
	protected GraphTraversalSource	g;
	protected boolean	supportsTransactions;
	protected boolean	supportsSchema;
	protected boolean	supportsGeoshape;
	
	
	
	/**
     * Constructs a graph app using the given properties.
     * @param fileName location of the properties file
     */
	public JanusUtils(final String filename) {
		propFileName = filename;
		// the server auto-commits per request, so the application code doesn't
        // need to explicitly commit transactions
        this.supportsTransactions	=	false;
        
		this.supportsSchema 		=	true;
		this.supportsGeoshape 		=	true;
		
	}
	
	 /**
     * Opens the graph instance. If the graph instance does not exist, a new
     * graph instance is initialized.
     */
	public GraphTraversalSource openGraph() throws ConfigurationException {
		LOGGER.info("opening graph");
		conf = new PropertiesConfiguration(propFileName);
		
		// using the remote driver for schema
		try {
			cluster = Cluster.open(conf.getString("gremlin.remote.driver.clusterFile"));
			client = cluster.connect();
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
		
		//using the remote graph for queries
		graph = EmptyGraph.instance();
		g = graph.traversal().withRemote(conf);
		return g;
	}
	
	public GraphTraversalSource getG() {
		return g;
	}
	
	/**
     * Closes the graph instance.
     */
	public void closeGraph() throws Exception {
		LOGGER.info("closing graph");
		try {
			if(g != null) {
				// this closes the remote, no need to close the empty graph
				g.close();
			}
			if(cluster != null) {
				// the cluster closes all of its clients
                cluster.close();
			}
		} finally {
			g = null;
			graph = null;
			client = null;
			cluster = null;
		}
	}
	
	
	public void addV(String key1, String value1, String key2, long value2) {
		LOGGER.info("adding V");
		
		// Use bindings to allow the Gremlin Server to cache traversals that
        // will be reused with different parameters. This minimizes the
        // number of scripts that need to be compiled and cached on the server.
        // https://tinkerpop.apache.org/docs/3.2.6/reference/#parameterized-scripts
        final Bindings b = Bindings.instance();
        
        if(g.addV().has(key1, value1).has(key2, value2).valueMap(true).tryNext().isPresent()) {
        	LOGGER.info("V exists");
        	return;
        }
        Vertex tea000 = g.addV().property(key1, b.of(key1, value1)).property(key2, b.of(key2, value2)).next();
        
	}
	
	
	/**
     * Runs some traversal queries to get data from the graph.
     */
    public void readElements() {
    	LOGGER.info("reading elements");
    }
	
    /**
     * Adds the vertices, edges, and properties to the graph.
     */
	public void createElements() {
		LOGGER.info("creating elements");
		
	}
	
	/**
     * Drops the graph instance. The default implementation does nothing.
     */
    public void dropGraph() throws Exception {
    	LOGGER.info("dropping graph");
    }
    
    
    
    
    
    
	public void createSchema() {
		LOGGER.info("creating schema");
		// get the schema request as a string
		final String req = createSchemaRequest();
		// submit the request to the server
		final ResultSet resultSet = client.submit(req);
		// drain the results completely
		Stream<Result> futureList = resultSet.stream();
		futureList.map(Result::toString).forEach(LOGGER::info);
		
	}
	
	/**
     * Returns a string representation of the schema generation code. This
     * request string is submitted to the Gremlin Server via a client
     * connection to create the schema on the graph instance running on the
     * server.
     */
	protected String createSchemaRequest() {
		final StringBuilder s = new StringBuilder();
		
		s.append("JanusGraphManagement management = graph.openManagement(); ");
		s.append("boolean created = false; ");
		
		// naive check if the schema was previously created
		s.append("if (management.getRelationTypes(RelationType.class).iterator().hasNext()) { management.rollback(); created = false; } else { ");
		
		//
		// management operation
		//
		
		s.append("management.commit(); created = true; }");
		
		return s.toString();
	}
	
	 /**
     * Returns the geographical coordinates as a float array.
     */
    protected float[] getGeoFloatArray(final float lat, final float lon) {
        final float[] fa = { lat, lon };
        return fa;
    }
}