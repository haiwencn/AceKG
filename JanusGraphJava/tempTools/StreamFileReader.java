//: StreamFileReader.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StreamFileReader {
    private static final String featureStr1 = "*******************************";
    private static final String featureStr2 = "**********";
    private static final String delfeatureStr1 = "*******************************";
    private static final String delfeatureStr2 = "****************************************";
    private static final String delfeatureStr3 = "^^";
    private final String fileName;
    private long line;

    public StreamFileReader(final String fileName) throws IOException {
        this.fileName = fileName;
        line = 0;
    }

    public void solve() throws IOException {
        try (BufferedReader fileIn = new BufferedReader(new FileReader(new File(fileName)))) {
            String tmpStr = null;
            String keyStr = null;
            FileWriter writer = null;
            while((tmpStr = fileIn.readLine()) != null) {
                ++line;
                
                if(tmpStr.indexOf(delfeatureStr1) != -1) continue;
                if(tmpStr.indexOf(delfeatureStr2) != -1) continue;
                if(tmpStr.indexOf(delfeatureStr3) != -1) continue;
                tmpStr = tmpStr.replace(featureStr1, "").replace(featureStr2, "");
                
                if( keyStr == null || !tmpStr.startsWith(keyStr)) {
                    keyStr = tmpStr.substring(0, tmpStr.indexOf('/'));
                    if(writer != null)
                        writer.close();
                    writer = new FileWriter(keyStr, true);    // 这里写的有点不大好。。。
                }
                writer.write(tmpStr+'\n');
                System.out.println("line " + line + ": " + tmpStr);

                
                if(line == 2000000) break;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    public static void main(String[] args) throws IOException {
        StreamFileReader reader = new StreamFileReader("aceDBtest.nt");
        reader.solve();
    }

}