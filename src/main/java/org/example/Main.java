package org.example;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.*;

public class Main {
    private static void verificationChecker(List<List<String>> userData, int[] userCredibility){
        for (int i = 0; i < userData.size(); i++){
            if (Objects.equals(userData.get(i).get(6), "False")){
                userCredibility[i] -= 2;
            }
        }
    }

    private static void naiveKeywordCheck(List<List<String>> userData, int[] userCredibility){
        HashSet<String> keywords = new HashSet();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("keywords.txt")));

            // Reads all words from keywords.txt file and puts them into the keywords hashset
            String line = reader.readLine();
            while (line != null) {
                keywords.add(line);
                line = reader.readLine();
            }


            String[] words;
            double suspicious;
            double nonsuspicious;

            // Loops through every tweet
            for (int i = 0; i < userData.size(); i++) {
                suspicious = 0;
                nonsuspicious = 0;

                // Ignores punctuation from tweet's content, sets all chars to lowercase
                words = userData.get(i).get(2).toLowerCase().split("\\W+");
                for (String word : words) {
                    if (keywords.contains(word)) suspicious++;
                    else nonsuspicious++;
                }

                // If the ratio of suspicious words to nonsuspicious words > 1:10, subtract from the credibility score
                if (suspicious / nonsuspicious >= .1) {
                    userCredibility[i] -= 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<List<String>> userData = new ArrayList<>();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("bot_detection_data.csv");
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] user;
            while((user = reader.readNext())!= null) {
                userData.add(Arrays.asList(user));
            }
            int[] userCredibility = new int[userData.size()];
            Arrays.fill(userCredibility, 10);
            verificationChecker(userData, userCredibility);
            naiveKeywordCheck(userData, userCredibility);
        }
        catch(IOException e){
                e.printStackTrace();
            } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
