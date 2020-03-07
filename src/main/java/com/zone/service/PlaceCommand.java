package com.zone.service;

/**
 * a command to place the robot at a position of the board with the direction it faces
 */
public class PlaceCommand {
        boolean isValid;
        int x;
        int y;
        Face face;
}