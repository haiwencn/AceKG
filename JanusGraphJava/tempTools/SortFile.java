//: SortFile.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

import javafx.util.Pair;

/**
 * /
 * |---SortFile.java
 * |---data
 *       |---filename
 *       |---filename_Done
 * |---tmp
 *       |---filename_sorted_x
 *       |---filename_sorted_merged_x
 */

public class SortFile{
    private final String filename;
    private final int blockLine;
    private int num;
    
    public SortFile(final String filename, int blockLine){
        this.filename = filename;
        this.blockLine = blockLine;
        this.num = 0;
    }
    
    public void sort() throws IOException {
        fileSplit();
        fileSort();
        mergeSort();
        System.out.println("Done.");
    }

    private void fileSplit() throws IOException {
        try(BufferedReader fileIn = new BufferedReader(new FileReader(new File("data/" + filename)))) {
            String curStr = null;
            int line = 0;
            FileWriter writer = new FileWriter(new File("tmp/" + filename + '_' + num)); 
            while((curStr = fileIn.readLine()) != null) {
                ++line;
                if(line > blockLine) {
                    ++num;
                    line = 0;
                    writer.close();
                    writer = new FileWriter(new File("tmp/" + filename + '_' + num));
                }
                writer.write(curStr + '\n');
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private int comparePair(Pair<String,String> p1, Pair<String,String> p2) {
        int res = p1.getValue().compareTo(p2.getValue());
        if(res == 0) {
            res = p1.getKey().compareTo(p2.getKey());
        }
        return res;
    }

    private void fileSort() throws IOException {
        for(int i = 0; i <= num; ++i) {
            TreeSet<Pair<String,String>> ts = new TreeSet<Pair<String,String>>((o1, o2) -> comparePair(o1, o2));
            try(BufferedReader fileIn = new BufferedReader(new FileReader(new File("tmp/" + filename + '_' + i)))) {
                String tmpStr = null;
                while((tmpStr = fileIn.readLine()) != null) {
                    ts.add(new Pair<String,String>(tmpStr,getComparionKey(tmpStr)));
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
            try(FileWriter writer = new FileWriter(new File("tmp/" + filename + "_sorted_" + i), true)) {
                for(Pair<String,String> ob : ts) {
                    writer.write(ob.getKey() + '\n');
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mergeSort() throws IOException {
        String filename1 = "tmp/" + filename + "_sorted_" + 0;
        for(int i = 1; i <= num; ++i) {
            String filename2 = "tmp/" + filename + "_sorted_" + i;
            String writedFilename = "tmp/" + filename + "_sotred_merged_" + i;
            if(i == num) writedFilename = "data/" + filename + "_Done";
            try(BufferedReader fileIn1 = new BufferedReader(new FileReader(new File(filename1)));
                BufferedReader fileIn2 = new BufferedReader(new FileReader(new File(filename2)));
                FileWriter writer = new FileWriter(new File(writedFilename), true)) {
                
                String curLine1 = fileIn1.readLine();
                String curLine2 = fileIn2.readLine();
                boolean OK = true;
                while(OK) {
                    
                    if(curLine1 == null && curLine2 == null) {
                        break;
                    }
                    if(curLine1 == null) {
                        while(curLine2 != null) {
                            writer.write(curLine2 + '\n');
                            curLine2 = fileIn2.readLine();
                        }
                        break;
                    }
                    if(curLine2 == null) {
                        while(curLine1 != null) {
                            writer.write(curLine1 + '\n');
                            curLine1 = fileIn1.readLine();
                        }
                        break;
                    }

                    if(getComparionKey(curLine1).compareTo(getComparionKey(curLine2)) > 0) {
                        writer.write(curLine2 + '\n');
                        curLine2 = fileIn2.readLine();
                    } else {
                        writer.write(curLine1 + '\n');
                        curLine1 = fileIn1.readLine();
                    }


                }
            } catch(IOException e) {
                e.printStackTrace();
            }
            filename1 = writedFilename;
        }
    }

    private String getComparionKey(final String str) {
        if(str == null) return null;
        final String AWARD = "Award";
        int index = 0, endIndex = 0;
        index = str.indexOf(AWARD) + 8;
        endIndex = str.indexOf(']', index);
        return str.substring(index, endIndex);
    }

    public static void main(String [] args) throws IOException {
        SortFile sf = new SortFile("responsible_for_in_txt.txt", 10000);
        sf.sort();
    }
}