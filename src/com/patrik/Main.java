package com.patrik;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        String python = getPythonEnviorment();
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"" + path + separator + "data\" && " + python + " randomTrips.py -n network.net.xml -r network.rou.xml -e 50 -l");
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

    public static String getPythonEnviorment() {
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (System.getProperty("os.name").startsWith("Windows")) {
            processBuilder = new ProcessBuilder().command("where", "python");
        } else {
            processBuilder.command("which", "python");
        }
        String path = "";
        try {
            Process process = processBuilder.start();

            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = null;
            while ((output = bufferedReader.readLine()) != null) {
                path += output;
            }
            //wait for the process to complete
            process.waitFor();
            //close the resources
            bufferedReader.close();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return path;
    }
}