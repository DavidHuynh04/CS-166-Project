package org.example;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static void verificationChecker(List<List<String>> userData, int[] userCredibility){
        for (int i = 0; i < userData.size(); i++){
            if (Objects.equals(userData.get(i).get(6), "false")){
                userCredibility[i] -= 2;
            }
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
        }
        catch(IOException e){
                e.printStackTrace();
            } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
