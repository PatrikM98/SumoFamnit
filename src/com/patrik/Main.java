package com.patrik;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        runCommand();

        /*var homeDir = System.getProperty("user.home");
        Process p = null;
        ProcessBuilder pb = new ProcessBuilder("python randomTrips.py -n network.net.xml -r network.rou.xml -e 50 -l");
        pb.directory(new File(homeDir + "\\IdeaProjects\\Sumo\\data"));
        var process = pb.start();
        try (var reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        }*/
    }

    public static String getPythonEnviorment(){
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (System.getProperty("os.name").startsWith("Windows")) {
            processBuilder = new ProcessBuilder().command("where", "python");
        } else {
            processBuilder.command("which", "python");
        }
        String path="";
        try {
            Process process = processBuilder.start();

            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = null;
            while ((output = bufferedReader.readLine()) != null) {
                path+=output;
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

    public static void runCommand() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String separator = System.getProperty("file.separator");
        String workDir = System.getProperty("user.dir");
        String python = getPythonEnviorment();
        processBuilder = new ProcessBuilder().directory(new File(workDir + separator + "data")).command(python,"randomTrips.py","-n network.net.xml","-r network.rou.xml","-e 50","-l");

        try {
            Process process = processBuilder.start();
            //read the output
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output = null;
            while ((output = bufferedReader.readLine()) != null) {
                System.out.println(output);
            }
            //wait for the process to complete
            process.waitFor();
            //close the resources
            bufferedReader.close();
            process.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
}
