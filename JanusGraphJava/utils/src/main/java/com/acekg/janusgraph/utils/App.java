package com.acekg.janusgraph.utils;

import com.acekg.janusgraph.utils.JanusUtils;

public class App 
{
    public static void main( String[] args )
    {
    	JanusUtils ju = new JanusUtils("conf/remote-graph.properties");
        try {
        	ju.openGraph();
        	ju.addV("name", "tea000", "age", 123);
        	
        	ju.closeGraph();
        } catch(Exception e) {
        	e.printStackTrace();
        }
    	System.out.println( "Hello World!" );
    }
}
