package com.patrik;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        var homeDir = System.getProperty("user.home");
        Process p = null;
        ProcessBuilder pb = new ProcessBuilder("python randomTrips.py -n network.net.xml -r network.rou.xml -e 50 -l");
        pb.directory(new File(homeDir+"\\IdeaProjects\\Sumo\\data"));
        var process  = pb.start();
        try (var reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        }
    }
}
	/*Process p = Runtime.getRuntime().exec("python ../../../data/randomTrips.py -n network.net.xml -r network.rou.xml -e 50 -l");
        printResults(p);
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }*/
