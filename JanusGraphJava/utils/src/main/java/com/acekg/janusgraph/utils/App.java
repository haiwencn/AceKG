package com.acekg.janusgraph.utils;


import com.acekg.janusgraph.utils.JanusUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.tinkerpop.gremlin.process.traversal.Bindings;

public class App 
{
	private static final String FILENAME = "usr/Investigator1";
	
    public static void main( String[] args )
    {
    	JanusUtils ju = new JanusUtils("conf/remote-graph.properties");
        try {
        	ju.openGraph();
        	final Bindings b = Bindings.instance();
        	//ju.addV("name", "tea000", "age", 123);
        	try(BufferedReader fileIn = new BufferedReader(new FileReader(new File(FILENAME)))) {
        		int line = 7;
        		String []tmpStr = new String[line];
        		while((tmpStr[0] = fileIn.readLine()) != null) {
        			for(int i = 1; i != line; ++i) {
        				tmpStr[i] = fileIn.readLine();
        			}
        			// vtxID
        			int index1 = tmpStr[0].indexOf('/') + 1;
        			int index2 = tmpStr[0].indexOf('>', index1);
        			long vtxID = Long.parseLong(tmpStr[0].substring(index1, index2));
        			System.out.print(vtxID);
        			/////Property
        			String []pK = new String[line];
        			for(int i = 0; i != line; ++i) {
        				index2 = tmpStr[i].lastIndexOf('\"');
            			index1 = tmpStr[i].lastIndexOf('\"', index2 - 1) + 1;
            			pK[i] = tmpStr[i].substring(index1, index2);
            			System.out.print("\t##\t" + pK[i]);
        			}
        			
        			System.out.println("\t##\t");
        			
        			if(Long.parseLong(pK[0]) != vtxID) break;
        			///*
        			ju.getG().addV(b.of("label", "investigator"))
        				.property("vtxLabel", b.of("vtxLabel", "investigator"))
        				.property("vtxID", b.of("vtxID", vtxID))
        				.property("roleCode",b.of("roleCode", pK[1]))
        				.property("firstName",b.of("firstName", pK[2]))
        				.property("startDate",b.of("startDate", pK[3]))
        				.property("emailAddress",b.of("emailAddress", pK[4]))
        				.property("lastName",b.of("lastName", pK[5]))
        				.property("endDate",b.of("endDate", pK[6]))
        				.next();
        			//*/
        		}
        		
        		
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	
        	ju.closeGraph();
        } catch(Exception e) {
        	e.printStackTrace();
        }
    	System.out.println( "Hello World!" );
    }
}
