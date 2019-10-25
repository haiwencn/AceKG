package com.acekg.janusgraph.base;


import java.util.HashMap;
import java.util.Map;
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
            /*
            * !Begin
            * */

            /*
            * example1
            * */
            System.out.println("example1:");

            String dsl = "g.V().has(\"vtxID\", 12345)";

            JanusClient.INSTANCE.submitCodes(dsl, App::info);

            /*
            * example2
            * */

            System.out.println("example2:");

            String dsl2 = "g.V().has(\"vtxID\", val)";

            Map<String, Object> map = new HashMap<>();
            map.put("val", 12345);

            JanusClient.INSTANCE.submitCodes(dsl2, map, App::info);

            /*
            * !End
            * */
            JanusClient.INSTANCE.closeGraph();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
