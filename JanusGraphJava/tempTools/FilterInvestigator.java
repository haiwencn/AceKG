//: FilterInvestigator.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FilterInvestigator {
    private final String fileName;
    private long line;
    public FilterInvestigator(final String filename) {
        this.fileName = filename;
        this.line = 0;
    }

    public void solve() throws IOException {
        try (BufferedReader fileIn = new BufferedReader(new FileReader(new File(fileName)))) {
            String tmpStr = null;
            boolean hasChange = false;
            FileWriter writer = new FileWriter(new File(fileName + "_Award"), true);
            while((tmpStr = fileIn.readLine()) != null) {
                ++line;
                if(!hasChange && tmpStr.endsWith("\" .")) {
                    hasChange = true;
                    writer.close();
                    writer = new FileWriter(new File(fileName + '1'), true);
                }
                writer.write(tmpStr + '\n');
            }
            writer.close();
            writer = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        FilterInvestigator fi = new FilterInvestigator("Investigator");
        fi.solve();
    }

}