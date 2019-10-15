//: MergeFile.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MergeFile {
    private final String filename1;
    private final String filename2;
    private final String filename3;
    private final int line;
    private boolean OK;
    public int l;

    public MergeFile(final String filename1, final String filename2, final String filename3) {
        this.filename1 = filename1;
        this.filename2 = filename2;
        this.filename3 = filename3;
        this.line = 0;
        this.OK = true;
    }

    public boolean merge() throws IOException {
        try(BufferedReader fileIn1 = new BufferedReader(new FileReader(new File(filename1)));
            BufferedReader fileIn2 = new BufferedReader(new FileReader(new File(filename2)));
            FileWriter writer = new FileWriter(new File(filename3), true)) {
            String nextStr1 = fileIn1.readLine();
            String curStr1 = nextStr1;
            String nextStr2 = fileIn2.readLine();
            String curStr2 = nextStr2;
            nextStr1 = fileIn1.readLine();
            nextStr2 = fileIn2.readLine();
            while(OK){
                
                if(curStr1 == null && curStr2 == null && nextStr1 == null && nextStr2 == null){
                    return true;
                }
                if(nextStr1 == null && curStr1 == null) {
                    while(curStr2 != null) {
                        writer.write(curStr2+'\n');
                        curStr2 = nextStr2;
                        nextStr2 = fileIn2.readLine();
                    }
                    return true;
                }
                if(nextStr2 == null && curStr2 == null) {
                    while(curStr1 != null) {
                        writer.write(curStr1 + '\n');
                        curStr1 = nextStr1;
                        nextStr1 = fileIn1.readLine();
                    }
                    return true;
                }
                long ID1 = getID(curStr1);
                while(ID1 == getID(curStr2)) {
                    writer.write(curStr2 + '\n');
                    curStr2 = nextStr2;
                    nextStr2 = fileIn2.readLine();
                }
                boolean flag = true;
                while(flag) {
                    if(getID(curStr1) != getID(nextStr1)) flag = false;
                    writer.write(curStr1 + '\n');
                    curStr1 = nextStr1;
                    nextStr1 = fileIn1.readLine();
                }
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private long getID(final String str) {
        if(str == null)return -1;
        final String AWARD = "Award";
        int index = 0, endIndex = 0;
        index = str.indexOf(AWARD) + 8;
        endIndex = str.indexOf(']', index);
        return Long.parseLong(str.substring(index, endIndex));
    }

    public static void main(String []args) throws IOException {
        MergeFile mf = new MergeFile("data/1", "data/2", "data/res1");
        mf.merge();
        mf = null;
        MergeFile mf2 = new MergeFile("data/3", "data/4", "data/res2");
        mf2.merge();
        mf2 = null;
        MergeFile mf3 = new MergeFile("data/res1", "data/res2", "data/res3");
        mf3.merge();
        mf3 = null;

    }
}