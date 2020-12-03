package com.patrik;

import com.sun.xml.ws.util.Constants;
import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.subscription.SumoDomain;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoColor;
import de.tudresden.ws.container.SumoStage;
import de.tudresden.ws.container.SumoStringList;
import de.tudresden.ws.container.SumoVehicleData;
import it.polito.appeal.traci.SumoTraciConnection;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws IOException {
        //MakeRoutes();

        String sumo_bin = "sumo-gui";
        String config_file = "data/config.sumocfg";
        double step_length = 0.1;

        if (args.length > 0) {
            sumo_bin = args[0];
        }
        if (args.length > 1) {
            config_file = args[1];
        }

        try {
            SumoTraciConnection conn = new SumoTraciConnection(sumo_bin, config_file);
            conn.addOption("step-length", step_length + "");
            conn.addOption("start", "true"); //start sumo immediately
            //start Traci Server
            conn.runServer();
            for (int i = 0; i < 360000; i++) {


                //System.out.println();

                SumoStage x = (SumoStage)conn.do_job_get(Simulation.findRoute("-150996422#0","194452010#2","DEFAULT_VEHTYPE",0,0));
                for (String j: x.edges
                     ) {
                    System.out.println(j);
                }
                conn.do_timestep();
                //conn.do_job_set(Vehicle.setColor(""+i, new SumoColor(255,255,51,100)));

                conn.do_job_set(Vehicle.addFull("v" + i, ""+i, "DEFAULT_VEHTYPE", "now", "0", "0", "max", "current", "max", "current", "", "", "", 0, 0));
                conn.do_job_set(Vehicle.setRoute("v"+i,x.edges));

            }

            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void MakeRoutes() throws IOException {
        String path = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        ProcessBuilder builder;
        if (System.getProperty("os.name").startsWith("Windows")) {
            builder = new ProcessBuilder(/*--vehicle-class bus --trip-attributes="maxSpeed=\"27.8\""                   \\//         network.rou.xml -e 100 -l --trip-attributes="departLane=""best\" departSpeed=\"max\" departPos=\"random\""*/
                    "cmd.exe", "/c", "cd \"" + path + separator + "data\" && python randomTrips.py -n network.net.xml ");
        } else {
            builder = new ProcessBuilder(
                    "bash", "-c", "cd \"" + path + separator + "data\" && python randomTrips.py -n network.net.xml ");
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
