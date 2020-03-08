package com.zone.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationServiceTest {

    SimulationServiceImpl simulationService = new SimulationServiceImpl();

    @DisplayName( "given non-PLACE or invalid PLACE as first command , then fail immediately")
    @Test
    public void invalidPlaceOrNonPlaceFirstCommandNotAllow()
    {
        List<String> lines = new ArrayList<>(Arrays.asList("PLACE 6,1,NORTH", "LEFT", "MOVE"));
        String result = simulationService.execute(lines);
        assertTrue( result.endsWith( "please check the command and the paramters"));
    }

    @DisplayName( "given valid PLACE, then initial state as set")
    @Test
    public void validPositionAndFaceParameterLeadToNewInitialState( )
    {
        simulationService.place( 1, 2, Face.EAST );
        assertSame( simulationService.currentFace, Face.EAST);
        assertEquals( simulationService.currentXAxisValue,1);
        assertEquals( simulationService.currentYAxisValue, 2);
    }

    @DisplayName( "given valid PLACE, then LEFT command works")
    @Test
    public void validPlaceLeadToCorrectLeft( )
    {
        List<String> lines = new ArrayList<>(Arrays.asList("PLACE 4,4,EAST", "LEFT", "REPORT"));
        String output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,NORTH"));
        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,SOUTH", "LEFT", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,EAST"));
        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,WEST", "LEFT", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,SOUTH"));
        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,NORTH", "LEFT", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,WEST"));
    }

    @DisplayName( "given valid PLACE, then RIGHT command works")
    @Test
    public void validPlaceLeadToCorrecRight( )
    {
        List<String> lines = new ArrayList<>(Arrays.asList("PLACE 4,4,EAST", "RIGHT", "REPORT"));
        String output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,SOUTH"));
        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,SOUTH", "RIGHT", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,WEST"));
        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,WEST", "RIGHT", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,NORTH"));
        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,NORTH", "RIGHT", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,EAST"));
    }

    @DisplayName( "given valid PLACE, then MOVE command works")
    @Test
    public void validPlaceLeadToCorrectMove( )
    {
        List<String> lines = new ArrayList<>(Arrays.asList("PLACE 3,3,EAST", "MOVE", "REPORT"));
        String output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,3,EAST"));
        lines = new ArrayList<>(Arrays.asList("PLACE 3,3,SOUTH", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "3,2,SOUTH"));
        lines = new ArrayList<>(Arrays.asList("PLACE 3,3,WEST", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "2,3,WEST"));
        lines = new ArrayList<>(Arrays.asList("PLACE 3,3,NORTH", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "3,4,NORTH"));
        lines = new ArrayList<>(Arrays.asList("PLACE 3,3,NORTH", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "3,4,NORTH"));

        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,NORTH", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,NORTH"));

        lines = new ArrayList<>(Arrays.asList("PLACE 4,4,EAST", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "4,4,EAST"));

        lines = new ArrayList<>(Arrays.asList("PLACE 0,0,WEST", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "0,0,WEST"));

        lines = new ArrayList<>(Arrays.asList("PLACE 0,0,SOUTH", "MOVE", "REPORT"));
        output = simulationService.execute(lines);
        assertTrue(  output.endsWith( "0,0,SOUTH"));
    }

    @DisplayName( "given the later invalid PLACE, then following non-PLACE commands be discarded until valid PLACE is up")
    @Test
    public void inValidPlaceWillNotProceedAndFollowingCommandsDicardedUntilValidPlace( )
    {
        List<String> lines = new ArrayList<>();
        lines.add("PLACE 1,1,NORTH");
        lines.add("LEFT");
        lines.add("MOVE");
        lines.add("MOVE");
        lines.add( "REPORT" );
        lines.add( "PLACE 0,6,WEST" );
        lines.add( "PLACE 0,0,MIDDLE" );
        lines.add( "REPORT" );
        lines.add( "Place 4,4,SOUTH");
        lines.add( "MOVE");
        lines.add( "REPORT" );
        String result = simulationService.execute(lines);
        assertTrue( result.endsWith( "4,3,SOUTH"));
        assertTrue( result.contains( "OUTPUT: 0,1,WEST"));
        assertTrue( result.contains( "'PLACE 0,6,WEST' is not a valid place command"));
        assertTrue( result.contains( "PLACE 0,0,MIDDLE' is not a valid place command"));
        assertTrue( result.contains( "'REPORT' command  is discarded!"));
    }

    @DisplayName( "given a raw input commands file, then all not-sensible information will be removed. Every text become uppercase")
    @Test
    public void rawInputCommandsWouldbecleanedAndConvertToUpperCase( )
    {
        List<String> lines = new ArrayList<>();
        lines.add("PLACE 1,1,NORTH");
        lines.add("");
        lines.add("MOVEE");
        lines.add("move");
        lines.add( "REPORT" );
        lines.add( "PLACE 0,6,WEST" );
        lines.add( "PLACE 0,0,MIDDLE" );
        lines.add( "REPORT" );
        lines.add( "REPORTWHAT" );
        lines.add( "Place 4,4,SOUTH");
        lines.add( "MOVE");
        lines.add( "REPORT" );
        List<String> newLines = simulationService.clean(lines);
        assertEquals( newLines.get(1), "MOVE");
        assertEquals( newLines.get(6), "PLACE 4,4,SOUTH");
    }

}
