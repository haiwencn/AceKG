package com.acekg.janusgraph.base;

/**
 * Hello world!
 *
 */
public class App 
{
    private static void info(final String str) { System.out.println( str ); }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try {
            JanusClient.INSTANCE.openGraph();

            String dsl = "g.V().has(\"vtxID\", 12345)";
            JanusClient.INSTANCE.submitCodes(dsl, App::info);

            JanusClient.INSTANCE.closeGraph();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
