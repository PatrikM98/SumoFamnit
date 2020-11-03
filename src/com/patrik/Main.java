package com.patrik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir");
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"" + path + "\\data\" && python randomTrips.py -n network.net.xml -r network.rou.xml -e 50 -l");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }


        System.out.println("Working Directory = " + path);


    }
}
