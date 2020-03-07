## What does the simulation app do?

Given a text file containing a sequence of commands delimited by line terminator, it accepts the file and execute the 
commands in it. for example,  the input text file contains below commands: ( the file has been included under the 
project directory, the file name is Commands.robot )
```javascript
PLACE 0,0,NORTH
MOVE
REPORT

left
REPORT
PLACE 6,2,EAST
PLACE 1,6,EAST
MOVE
MOVE
LEFT
MOVE
REPORT
PLACE 3,4,EAST
PLACE 1,1,EAST
MOVE
MOVE
MOVE
MOVE
MOVE
REPORT
```
output of the execution is as below
```javascript
PLACE 0,0,NORTH
MOVE
REPORT
    OUTPUT: 0,1,NORTH
LEFT
REPORT
    OUTPUT: 0,1,WEST
'PLACE 6,2,EAST' is not a valid place command
'PLACE 1,6,EAST' is not a valid place command
'MOVE' command  is discarded!
'MOVE' command  is discarded!
'LEFT' command  is discarded!
'MOVE' command  is discarded!
'REPORT' command  is discarded!
PLACE 3,4,EAST
PLACE 1,1,EAST
MOVE
MOVE
MOVE
MOVE
MOVE
REPORT
    OUTPUT: 4,1,EAST
```
## The Solution

Each simulation process is actually running a FSM (Finite State Machine) with PLACE command setting the initial state.
The approach here is to have a cleanning process before every actual execution. The cleanning process removes all lines that are
not recognised as a valid command, during the cleaning process, it also converts commands to be all uppercase, so this
allows the commands to be case insensitive.

Particular care need to give to the PLACE and MOVE command, as they need to check the constrains ( with the scope of 5x5
cells board ). 
For later PLACE commands other than the first one, invalid PLACE can be repeated, all commands between invlid PLACE
and valid PLACE command must be discarded and showed in the output.

## Run the simulation App in IntelliJ

open the toy_robot_sim as a maven project. ( select pom.xml as project file )
1. create a launcher by going to "Run->Edit Configurations ...", give launcher a name, for example "Main".
2. Choose 'Main' class as 'com.zone.Main'
3. specify a text file as input argument, e.g. Commands.robot ( you can change this file to test other cases)
4. apply and save

you've got a java application launcher that run the app with arguments feed

## Run the simulation App with Maven

Make sure you have the latest maven version. 
If you're on Windows, run below in the command line window (terminal) :
```javascript
mvn exec:java -D"exec.mainClass"="com.zone.Main" -D"exec.args"="Commands.robot"
```
If you'are on linux or IOS, run below in the terminal:
```javascript
mvn exec:java -Dexec.mainClass="com.zone.Main" -Dexec.args="Commands.robot"
```
This will run your Main class in the application.

## Run Test in IntelliJ

it is quite straightforward, right click the SimulationServiceTest.java file, then click 'Run xxxxxx'. Another way is to
create a JUnit test launcher, omitted here as it is very similar to create a java application launcher as described 
above)

## Run Test with Maven

under any kind of operation systems, run the below command:
```javascript
mvn -Dtest=SimulationServiceTest test
```


