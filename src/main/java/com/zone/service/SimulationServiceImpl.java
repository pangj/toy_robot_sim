package com.zone.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * an implementation of toy robot simulation service, it is able to accept a sequence of command
 * in a command set of ( PLACE, MOVE, LEFT, RIGHT, REPORT) and execute them in a board of 5x5 cells.
 * The PLATE command must be the first command in the sequence as it initialises the state of the
 * robot. For any input, before it is put to execute, the sequence will be cleaned, means removal
 * of any empty command and any other things that are not a command. result of execution leads to some outcome as below:
 * (1) fail fast if the first command is not a valid PLATE command because it's not allow, and all following command
 *     will be ignored
 * (2) the robot comes to new state after the execution. all invalid commands will be reported or discarded
 */
public class SimulationServiceImpl implements SimulationService{

    public static final int MAX_BOARD_WIDTH = 5;
    public static final int MAX_BOARD_HEIGHT = 5;

    public static final Set<String> NON_PLACE_COMMAND_SET = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("MOVE", "LEFT", "RIGHT", "REPORT")));

    public static final Set<String> FACE_SET = Collections.unmodifiableSet(Stream.of(Face.values()).map(Face::toString).collect(Collectors.toSet()));

    protected  Face currentFace;
    protected int currentXAxisValue;
    protected int currentYAxisValue;

    /**
     * execute a sequence of commands in the list by order.
     * @param commands a sequence of commands as a list
     * @return outcome of the execution, contains command text that have been executed, error messages showing invalid
     * commands and discarded commands due to invalid leading PLATE command
     */
    public String execute(List<String> commands) {
        commands = clean(commands);
        PlaceCommand place = parsePlaceCommand(commands.get(0).trim());
        if (!place.isValid)
            return "First command " + commands.get(0) + " is not a valid Place command, please check the command and the paramters";
        List<String> outputList = new ArrayList<>();
        place(place.x, place.y, place.face);
        outputList.add(commands.get(0).trim());
        int i = 1;
        int length = commands.size();

        while (i < length) {
            while (i < length && isNonPlaceCommand(commands.get(i)))
                outputList.add(executeNonPlaceCommand(commands.get(i++)));
            if (i == length)
                break;
            do {
                place = parsePlaceCommand(commands.get(i).trim());
                if (place.isValid) break;
                if ( NON_PLACE_COMMAND_SET.contains( commands.get(i) ) )
                    outputList.add("'" + commands.get(i) + "' command " + " is discarded!");
                else outputList.add("'" + commands.get(i) + "'" + " is not a valid place command");
                i++;
            } while (i < length);
            if (i == length)
                break;
            else {
                place(place.x, place.y, place.face);
                outputList.add(commands.get(i).trim());
                i++;
            }
        }
        return String.join("\n", outputList);
    }

    protected List<String> clean(List<String> commands) {
        return commands.stream().map(c -> c.trim().toUpperCase()).filter(this::shouldNotIgnore).collect(Collectors.toList());
    }

    private boolean shouldNotIgnore(String line) {
        return !line.isEmpty() && (NON_PLACE_COMMAND_SET.contains(line) || line.startsWith("PLACE "));
    }

    private boolean isNonPlaceCommand(String command) {
        return NON_PLACE_COMMAND_SET.contains(command.trim().toUpperCase());
    }

    private PlaceCommand parsePlaceCommand(String command) {
        PlaceCommand place = new PlaceCommand();
        String[] split = command.split("\\s+");
        if (!split[0].equals("PLACE")) {
            place.isValid = false;
            return place;
        }
        if (split.length == 1) {
            place.isValid = false;
            return place;
        }
        String[] spl2 = split[1].split("\\s*,\\s*");
        if (spl2.length != 3) {
            place.isValid = false;
            return place;
        }
        if (!FACE_SET.contains(spl2[2])) {
            place.isValid = false;
            return place;
        }
        int x = parseToInt(spl2[0]);
        if (x < 0 || x >= MAX_BOARD_WIDTH) {
            place.isValid = false;
            return place;
        }
        int y = parseToInt((spl2[1]));
        if (y < 0 || y >= MAX_BOARD_HEIGHT) {
            place.isValid = false;
            return place;
        }
        place.isValid = true;
        place.face = Face.valueOf(spl2[2]);
        place.x = x;
        place.y = y;
        return place;
    }

    private int parseToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    private String executeNonPlaceCommand(String command) {
        switch (command.toUpperCase()) {
            case "LEFT":
                return left();
            case "RIGHT":
                return right();
            case "REPORT":
                return report();
            case "MOVE":
                return move();
            default:
                return "No such command as " + command;
        }
    }

    protected void place(int x, int y, Face face) {
        currentXAxisValue = x;
        currentYAxisValue = y;
        currentFace = face;
    }

    private String left() {
        switch (currentFace) {
            case EAST:
                currentFace = Face.NORTH;
                break;
            case WEST:
                currentFace = Face.SOUTH;
                break;
            case NORTH:
                currentFace = Face.WEST;
                break;
            case SOUTH:
                currentFace = Face.EAST;
        }
        return "LEFT";
    }

    private String right() {
        switch (currentFace) {
            case EAST:
                currentFace = Face.SOUTH;
                break;
            case WEST:
                currentFace = Face.NORTH;
                break;
            case NORTH:
                currentFace = Face.EAST;
                break;
            case SOUTH:
                currentFace = Face.WEST;
        }
        return "RIGHT";
    }

    private String move() {
        switch (currentFace) {
            case EAST:
                if (currentXAxisValue + 1 < MAX_BOARD_WIDTH)
                    currentXAxisValue++;
                break;
            case WEST:
                if (currentXAxisValue > 0)
                    currentXAxisValue--;
                break;
            case NORTH:
                if (currentYAxisValue + 1 < MAX_BOARD_HEIGHT)
                    currentYAxisValue++;
                break;
            case SOUTH:
                if (currentYAxisValue > 0)
                    currentYAxisValue--;
        }
        return "MOVE";

    }

    private String report() {

        return "REPORT\n" +
                "    OUTPUT: " + currentXAxisValue + "," + currentYAxisValue + "," + currentFace;
    }

}
