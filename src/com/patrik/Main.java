package com.patrik;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        MakeRoutes();

    }

    public static void MakeRoutes() throws IOException{
        String path = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        ProcessBuilder builder;
        if (System.getProperty("os.name").startsWith("Windows")) {
            builder = new ProcessBuilder(
                    "cmd.exe", "/c", "cd \"" + path + separator + "data\" && python randomTrips.py -n network.net.xml -r network.rou.xml -e 50 -l");

        }else{
            builder = new ProcessBuilder(
                    "bash", "-c", "cd \"" + path + separator + "data\" && python randomTrips.py -n network.net.xml -r network.rou.xml -e 50 -l");
            }
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }

    }
}