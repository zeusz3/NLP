/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pa1;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harab
 */
public class Pa1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String oldText = "";
        Charset charset = Charset.forName("US-ASCII");
        try( BufferedReader reader = Files.newBufferedReader(Paths.get("b:/coursera/NLP/PA1/h1-p/gene.train"), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                oldText += line + "\r\n";
                System.out.println(line);
            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("b:/coursera/NLP/PA1/h1-p/gene.counts"), charset)) {
            System.out.println("counts");
            String line = null;
            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty()) {
                    if(line.contains("WORDTAG")) {
                        System.out.print(line + " ");
                        String[] words = line.split(" ");
                        int wordCount = Integer.valueOf(words[0]);
                        if(wordCount < 5) {
                            System.out.println("Old: " + words[3] + " Count: " + words[0]);
                            oldText.replaceAll(words[3], "_RARE_");
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        FileWriter writer;
        try {
            writer = new FileWriter("b:/coursera/NLP/PA1/h1-p/gene_rare.counts");
            writer.write(oldText);
        } catch (IOException ex) {
            Logger.getLogger(Pa1.class.getName()).log(Level.SEVERE, null, ex);
        }
             
    }
    
}
