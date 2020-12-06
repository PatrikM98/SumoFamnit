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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws IOException {
        //MakeRoutes();
        String[] starting_egdes = {"294817175", "182945654"};
        String[] ending_egdes = {"197451487#1", "-774963561", "150941573", "124431339#1"};


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


            /*String path = System.getProperty("user.dir");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File trips_file = new File(path + "\\data\\trips.trips.xml");
            Document doc = builder.parse(trips_file);
            //doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getChildNodes();*/

            for (int i = 0; i < 3600; i++) {
               /* if (i%50==21 || i%50==35) continue;
                Node nNode = nList.item(i%50);*/
                //System.out.println(nNode.getNodeName());

               /* if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;*/
                    SumoStage x = (SumoStage) conn.do_job_get(Simulation.findRoute(randomEdge(starting_egdes), randomEdge(ending_egdes), "DEFAULT_VEHTYPE", 0, 0));
                    conn.do_timestep();
                    //conn.do_job_set(Vehicle.setColor(""+i, new SumoColor(255,255,51,100)));
                    conn.do_job_set(Route.add("r" + i, x.edges));
                    conn.do_job_set(Vehicle.addFull("v" + i, "r" + i, "DEFAULT_VEHTYPE", "now", "0", "0", "max", "current", "max", "current", "", "", "", 0, 0));
                    conn.do_job_set(Vehicle.setColor("v" + i, new SumoColor(255, 255, 55, 100)));
                //}
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

    public static String randomEdge( String[] edges){
        //String[] edges = {"294817175", "182945654", "197451487#1", "-774963561", "150941573", "124431339#1"};
        Edge[] edge_objects = new Edge[edges.length];
        for (int i = 0; i < edge_objects.length; i++) {
            edge_objects[i] = new Edge(edges[i]);
        }
        Random r = new Random();
        //System.out.println(edge_objects.length);
        //System.out.println(r.nextInt(edge_objects.length));
        return edge_objects[r.nextInt(edge_objects.length)].getEdge_id();
    }
}
