package com.zone;

import com.zone.service.SimulationServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private SimulationServiceImpl simulationService = new SimulationServiceImpl();

    public static void main(String[] args) {
	     if ( args == null || args.length == 0) {
             System.err.println("parameters is missing");
             System.exit( -1 );
         }
        try {
            List<String> lines = Files.readAllLines(Paths.get(args[0]));
            Main app = new Main();
            String trace = app.simulationService.execute(lines);
            System.out.println(trace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
