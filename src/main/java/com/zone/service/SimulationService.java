package com.zone.service;

import java.util.List;

/**
 *  represent a simulation service of toy robot, the service is able to execute a sequence of commands
 */
public interface SimulationService {
    String execute(List<String> commands);
}
