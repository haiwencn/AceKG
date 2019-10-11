//: autoLoad.java

/**
 * 1. every values of property are embeded in "". (eg. {<> <> "" .})
 * 2. the pair of {<edgeId> <vertexpro> <vertexId>} must be adjacent to each other
 * 3. the edge label must contains '_' while the vertex label mustn't
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class AutoLoad {
    private final String filename; // path of the file(*.nt)
    private long curLine; // the current line
    private HashSet<String> edgeLabelSet;
    private HashSet<String> vertexLabelSet;
    private final String TYPE = "type"; // avoid to construturing new String timely
    private final String SPACE = " ";

    public AutoLoad(String fileName) {
        this.filename = fileName;
        curLine = 0;
        this.edgeLabelSet = new HashSet<String>();
        this.vertexLabelSet = new HashSet<String>();
    }

    private void init() throws IOException {
        try (BufferedReader fileIn = new BufferedReader(new FileReader(new File(filename)))) {
            String curStr = null; // the current String
            while((curStr = fileIn.readLine()) != null) {
                int index2 = curStr.indexOf('>');
                int index1 = curStr.lastIndexOf('/', index2);
                //String theId = curStr.substring(index1 + 1, index2);
                index2 = curStr.lastIndexOf('/', index1 - 1) + 1;
                String theLabel = curStr.substring(index2, index1);
                if(theLabel.lastIndexOf('_') == -1) { 
                    // Since the label doesn't contain '_'
                    // it is of vertex
                    if(!vertexLabelSet.contains(theLabel)) {
                        vertexLabelSet.add(theLabel);
                    }
                } else {
                    // Since the label contains '_'
                    // it is of edge
                    if(!edgeLabelSet.contains(theLabel)) {
                        edgeLabelSet.add(theLabel);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void solve() throws IOException {
        init();
        try (BufferedReader fileIn = new BufferedReader(new FileReader(new File(filename)))) {
            String curStr = null; // the current String
            while((curStr = fileIn.readLine()) != null ) {
                ++curLine;
                String []splStr = curStr.split(SPACE, 3);
                if(splStr[2].charAt(0) == '\"') { // Import property
                    int index1 = splStr[0].lastIndexOf('/');
                    int index2 = splStr[0].lastIndexOf('/', index1 - 1) + 1;
                    String label = splStr[0].substring(index2, index1);
                    index2 = splStr[0].lastIndexOf('>');
                    String vtxId = splStr[0].substring(index1 + 1, index2);
                    index1 = splStr[1].lastIndexOf('/') + 1;
                    index2 = splStr[1].lastIndexOf('>');
                    String propertyKey = splStr[1].substring(index1, index2);
                    index2 = splStr[2].lastIndexOf('\"');
                    String value = splStr[2].substring(1, index2);
                    
                    //System.out.println("label: " + label + " vtxId: " + vtxId + "#\t\t#" + propertyKey + "\t:\t" + value);
                    /**
                     *  Import vertex or edge's property
                     */
                } else { 
                    // check whether it is of type?
                    int index = splStr[1].lastIndexOf(TYPE);
                    if(index != -1) { // it is of type
                        int aa = 1;
                        /*
                        int index1 = splStr[0].lastIndexOf('/');
                        int index2 = splStr[0].lastIndexOf('/', index1 - 1) + 1;
                        String label = splStr[0].substring(index2, index1);
                        if(this.vertexLabelSet.contains(label) || this.vertexLabelSet.contains(label)) {
                            index2 = splStr[0].lastIndexOf('>');
                            String vtxId = splStr[0].substring(index1 + 1, index2);
                            index2 = splStr[2].lastIndexOf('>');
                            index1 = splStr[2].lastIndexOf('/') + 1;
                            String value = splStr[2].substring(index1, index2);
                            //System.out.println("label: " + label + " vtxId: " + vtxId + "#\t\t#" + "type" + "\t:\t" + value);
                            
                        }
                        */
                    } else {
                        // check whether it is of Edge?
                        // by checking the consistency of the prefixs of str[0] and str[2] 
                        int cmp1, cmp2;
                        int index1 = splStr[0].lastIndexOf('/');
                        int index2 = cmp1 = splStr[0].lastIndexOf('/', index1 - 1) + 1;
                        String label1 = splStr[0].substring(index2, index1);
                        index2 = splStr[0].lastIndexOf('>');
                        String theId1 = splStr[0].substring(index1 + 1, index2);
                        index1 = splStr[1].lastIndexOf('/') + 1;
                        index2 = splStr[1].lastIndexOf('>');
                        String midPro = splStr[1].substring(index1, index2);
                        index1 = splStr[2].lastIndexOf('/');
                        index2 = cmp2 = splStr[2].lastIndexOf('/', index1 - 1) + 1;
                        String label2 = splStr[2].substring(index2, index1);
                        index2 = splStr[2].lastIndexOf('>');
                        String theId2 = splStr[2].substring(index1 + 1, index2);
                        
                        // compare 
                        boolean ok = true;
                        if(cmp1 != cmp2) ok = false;
                        else for(int i = 0; i != cmp2; ++i) {
                            if(splStr[0].charAt(i) == splStr[2].charAt(i)) continue;
                            ok = false; break;
                        }
                        //

                        if(ok && vertexLabelSet.contains(label2)){
                            System.out.println(label1 + "#" + theId1 + "\t" + midPro + "\t" + label2 + "#" + theId2);
                            if(vertexLabelSet.contains(label1)) {
                                // <Vertex> <Edge> <Vertex>
                                /**
                                 * Import Edge (The kind of edge has not property)
                                 */
                            }
                            else if(edgeLabelSet.contains(label1)) {
                                // <Edge> <Vertex1> <Vertex1>
                                String anoStr = fileIn.readLine();
                                String[] splStr2 = anoStr.split(SPACE, 3);
                                index1 = splStr2[0].lastIndexOf('/');
                                index2 = splStr2[0].lastIndexOf('/', index1 - 1) + 1;
                                String label21 = splStr2[0].substring(index2, index1);
                                index2 = splStr2[0].lastIndexOf('>');
                                String theId21 = splStr2[0].substring(index1 + 1, index2);
                                index1 = splStr2[1].lastIndexOf('/') + 1;
                                index2 = splStr2[1].lastIndexOf('>');
                                String midPro2 = splStr2[1].substring(index1, index2);
                                index1 = splStr2[2].lastIndexOf('/');
                                index2 = splStr2[2].lastIndexOf('/', index1 - 1) + 1;
                                String label22 = splStr2[2].substring(index2, index1);
                                index2 = splStr2[2].lastIndexOf('>');
                                String theId22 = splStr2[2].substring(index1 + 1, index2);
                            }

                           
                        }
                        
                    }
                }

                //System.out.println(splStr[2]);
                if(curLine == 403714) return;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        AutoLoad aL = new AutoLoad("drugbank_dump.nt");
        aL.solve();
    }
}