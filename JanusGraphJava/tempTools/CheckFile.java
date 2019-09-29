//: checkFile.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CheckFile {
    private final String filename;
    private final int line;
    private boolean OK;
    public int l;
    
    public CheckFile(final String filename, int line) {
        this.filename = filename;
        this.line = line;
        this.OK = true;
        this.l = 0;
    }

    public boolean check() throws IOException {
        try(BufferedReader fileIn = new BufferedReader(new FileReader(new File(filename)))) {
            String []tmpStr = new String[line];
            while(OK){
                for(int i = 0; i != line; ++i) {
                    tmpStr[i] = fileIn.readLine();
                    ++l;
                    if(tmpStr[i] == null){
                        if(i == 0) return true;
                        return false;
                    }
                }
                long Id = -1;
                for(int i = 0; i != line; ++i) {
                    int index1 = tmpStr[i].indexOf('/') + 1;
                    int index2 = tmpStr[i].indexOf('>', index1);
                    long tmpId = Long.parseLong(tmpStr[i].substring(index1, index2));
                    if(Id == -1) Id = tmpId;
                    else if(Id != tmpId) return false;
                }

            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void main (String[] args) throws IOException {
        CheckFile cF = new CheckFile("Organization_Division", 2);
    
        System.out.println(cF.check());
        System.out.println(cF.l);
        
    }
}