//:FormatImporter.java

/**
 * V author [120001] [name,string,"..."] [age,string,"..."]
 * V author [120002] [name,string,"..."] [age,string,"..."]
 * E coauthor [author] [120001] [author] [120002] [time,string,"2019"]
 * [120001] <- [120002]
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

 class FormatImporter {
    private final String FILENAME; // path of the input file
    
    private long curLine; // for test

    public FormatImporter(final String fileName) {
        this.FILENAME = fileName;
        curLine = 0;
    }

    public HashMap<String, String> extractProperties(final String str, int beginIndex) {
        HashMap<String, String> ret = null;
        int curIndex = beginIndex + 1; // '['
        int endToIndex = 0;
        StringBuffer sbKey = null;
        StringBuffer sbValue = null;
        final int strLength = str.length();
        for(; curIndex < strLength; ++curIndex) {
            if(str.charAt(curIndex) == '['){
                sbKey = new StringBuffer();
                sbValue = new StringBuffer();
                ++curIndex;
                endToIndex = str.indexOf(',', curIndex);
                sbKey.append('\'').append(str.substring(curIndex,endToIndex)).append('\'');
                curIndex = endToIndex + 1; 
                if(str.charAt(curIndex) == 's') { // string
                    curIndex += 8; //
                    endToIndex = str.indexOf('"', curIndex);
                    sbValue.append('\'').append(str.substring(curIndex, endToIndex)).append('\'');
                } else { // long
                    curIndex += 6;
                    endToIndex = str.indexOf('"', curIndex);
                    sbValue.append(str.substring(curIndex, endToIndex));
                }
                ret.put(sbKey.toString(), sbValue.toString());

                sbKey = null;
                sbValue = null;
            }
        }

        return ret;
    }

    public void importVertex(final String str) {
        int curIndex = 2;
        int subToIndex = 0;
        // extract Label
        subToIndex = str.indexOf(' ', curIndex);
        String vtxLabel = str.substring(curIndex, subToIndex);
        curIndex = subToIndex + 2; // str.charAt(curIndex) is the first char of vtxID
        
        // extract vtxID
        subToIndex = str.indexOf(']', curIndex);
        String vtxID = str.substring(curIndex, subToIndex);
        curIndex = subToIndex; // str.charAt(curIndex) is ' '

        HashMap<String, String> props = extractProperties(str, curIndex);
    } 

    public void importEdge(final String str) {
        int curIndex = 2;
        int subToIndex = 0;
        // extract Label
        subToIndex = str.indexOf(' ', curIndex);
        String edgLabel = str.substring(curIndex, subToIndex);
        curIndex = subToIndex + 2; // str.charAt(curIndex) is the first char of vtxLabel1
        
        // extract vtxLabel and vtxID
        subToIndex = str.indexOf(']', curIndex);
        String vtxLabel1 = str.substring(curIndex, subToIndex);
        curIndex = subToIndex + 3; // str.charAt(curIndex) is the first char of vtxID1

        subToIndex = str.indexOf(']', curIndex);
        String vtxID1 = str.substring(curIndex, subToIndex);
        curIndex = subToIndex + 3; // str.charAt(curIndex) is the first char of vtxLabel2

        subToIndex = str.indexOf(']', curIndex);
        String vtxLabel2 = str.substring(curIndex, subToIndex);
        curIndex = subToIndex + 3; // str.charAt(curIndex) is the first char of vtxID2

        subToIndex = str.indexOf(']', curIndex);
        String vtxID2 = str.substring(curIndex, subToIndex);
        curIndex = subToIndex + 1; // str.charAt(curIndex) is ' '

        HashMap<String, String> props = extractProperties(str, curIndex);
    }

    public void Import() throws IOException {
        try(BufferedReader fileIn = new BufferedReader(new FileReader(new File(FILENAME)))) {
            String lineStr = null;
            while((lineStr = fileIn.readLine()) != null) {
                ++curLine;
                if(lineStr.charAt(0) == 'V') importVertex(lineStr);
                else importEdge(lineStr);
            }
        } catch(IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

 }