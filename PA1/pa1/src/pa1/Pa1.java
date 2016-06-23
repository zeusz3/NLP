/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pa1;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author harab
 */
public class Pa1 {
    
    private static TreeMap<String, Integer> tm = new TreeMap();
    private static Charset charset = Charset.forName("UTF-8");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        eOfXY();
    }
    
    public static void eOfXY() {
        float igene1 = 41072, o1 = 345128, emi = 0, emo = 0;
        TreeMap<String, Integer> wordWTCount = new TreeMap();
        TreeMap<String, String> argem = new TreeMap();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("../h1-p/gene_rare.counts"), charset)) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()) {
                    if(line.contains("WORDTAG")) {
                        String[] words = line.split(" WORDTAG ");
                        wordWTCount.put(words[1], Integer.valueOf(words[0]));
                    }
                }
            }
            System.out.println(wordWTCount);
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("../h1-p/gene_rare.counts"), charset)) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                emi = 0;
                emo = 0;
                if(!line.isEmpty()) {
                    if(line.contains("WORDTAG")) {
                        String[] words = line.split(" ");
                        System.out.print(words[3] + " - " + words[2]);
                        if(words[2].matches("O")) {
                            emo = wordWTCount.get(words[2] + " " + words[3])/o1;
                            if(wordWTCount.containsKey("I-GENE "+words[3])) {
                                System.out.print(" IIII");
                                emi = wordWTCount.get("I-GENE "+words[3])/igene1;
                            }
                        } else {
                            emi = wordWTCount.get(words[2] + " " + words[3])/igene1;
                            if(wordWTCount.containsKey("O "+words[3])) {
                                System.out.print(" OOOOO");
                                emo = wordWTCount.get("O "+words[3])/o1;
                            }
                        }
                        System.out.println(" EMO: " + emo + " EMI: " + emi);
                        if(emi > emo) {
                            argem.put(words[3], "I-GENE");
                        } else argem.put(words[3], "O");
                    }
                }
            }
            reader.close();
            System.out.println(argem);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("../h1-p/gene_dev.p1.out"), charset);
            BufferedReader reader = Files.newBufferedReader(Paths.get("../h1-p/gene.dev"), charset);
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.isEmpty()) {
                    String s;
                    if(argem.containsKey(line))
                        s = line + " " + argem.get(line);
                    else 
                        s = line + " " + argem.get("_RARE_");
                    writer.write(s, 0, s.length());
                    writer.newLine();
                } else 
                    writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Pa1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void applyRare() {
        String oldText = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("../h1-p/gene.counts"), charset)) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()) {
                    if(line.contains("WORDTAG")) {
                        System.out.println(line + " ");
                        String[] words = line.split(" ");
                        int wordCount = Integer.valueOf(words[0]);
                        tm.put(words[3], wordCount);
                    }
                }
            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        System.out.println("##############################");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("../h1-p/gene.train"), charset)) {
            String line = null;
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("../h1-p/gene_rare.train"), charset);
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()) {
                    String words[] = line.split(" ");
                    //System.out.println(words[0]);
                    if(tm.get(words[0]) < 5) {
                        line = line.replaceFirst(Pattern.quote(words[0]), "_RARE_");
                    }
                }
                writer.write(line, 0, line.length());
                writer.newLine();
                System.out.println(line);
            }
            reader.close();
            writer.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}
