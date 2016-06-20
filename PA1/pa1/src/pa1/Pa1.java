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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String oldText = "";
        Charset charset = Charset.forName("UTF-8");
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
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("../h1-p/gene.train"), charset)) {
            String line = null;
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("../h1-p/gene_rare.train"), charset);
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()) {
                    String words[] = line.split(" ");
                    System.out.println(words[0]);
                    if(tm.get(words[0]) < 5) {
                        line = line.replace(words[0], "_RARE_");
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
