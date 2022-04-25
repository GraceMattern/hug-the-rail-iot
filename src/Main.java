import java.lang.Math;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Main.java is similar to Display.java non-admin login
 * except that it runs in the terminal instead of the GUI. 
 * There is no need to click ok as it automatically refreshes. 
 * To run the simulation, simply run Main.java
 */
public class Main {
    /**
     * Formats the text outputted to the terminal
     * Simply provides the following states:
     *      Normal operations
     *      Yellow warning
     *      Red warning
     * for each of the three functionalities
     */
    public static void displayInfo(double[] info) {
        /** 
        1. login
        2. object
            - speed determins stationary/moving
            - distance 2mile radius
            - [object: speed, distance]
        3. gate
            - distance 2mile radius
            - status
            - [gate: status, distance]
        4. slippage
            - ADD calculate rpm using tachometer
            - [slip: difference threshold reacheddisplay warning]
        */

        System.out.println(java.util.Calendar.getInstance().getTime());
        System.out.println("RPM: " + info[0] + "        GPS: " + info [1]);
        System.out.println("gateStat: " + info[2] + "   gateDist: " + info [3]);
        System.out.println("objDist: " + info[4] + "    objSpeed: " + info [5]);
        System.out.println();
        System.out.println("Wheel Slippage: "+ slippageDetection(info[0], info[1])); // rpm and gps speed
        System.out.println("Gate Status: "+ gateDetection(info[2], info[3])); // gateStat and gateDist
        System.out.println("Obstruction: "+ objDetection(info[4], info[5]));  //objDist and objSpeed
        System.out.println();
    }
    
    // https://stackoverflow.com/questions/5683728/convert-java-util-date-to-string
    public static void writeToLog(String text) {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();        
        String todayAsString = df.format(today);
        try {
            FileWriter fw = new FileWriter("log.txt", true);
            // will append to end of already existing file in repository
            fw.write(todayAsString + " - " + text);
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred while logging.");
            e.printStackTrace();
        }
    }

    // the functions named Xdetection preform the logic to test if the rng data would generate warnings/recommendations
    
    private static String slippageDetection(double rpm, double speed) {
        // in this case circum is 1m
        // vehicle speed = wheel rpm * wheel diameter * pi * 60 / 63360
         String x = new String();
         char ch=27;
         double rpm_to_speed = rpm * 1 * Math.PI * 60 / 63360;
         if (Math.abs(rpm_to_speed - speed) >= 5) {
            if (Math.abs(rpm_to_speed - speed) >= 10) {
                x = ch+"[31mMajor slippage. Brake immediately."+ch+"[0m";
                writeToLog("Major slippage. Brake immediately.\n");
            } else {
                x = ch+"[33mMinor slippage. Slow down."+ch+"[0m";
                writeToLog("Minor slippage. Slow down.\n");
            }
         } else {
             x = "Normal operation";
         }
         return x;
     }

     private static String gateDetection(double gateStat, double gateDist) {
        String x = new String();
        char ch=27;
        if ((gateStat == 1) && (Math.abs(gateDist) <= 2)) {
            if(Math.abs(gateDist) == 1) {
                x = ch+"[31mOpen Gate. Brake immediately and Blow horn for 15 seconds."+ch+"[0m";
                writeToLog("Open Gate. Brake immediately and Blow horn for 15 seconds.\n");
            } else if(Math.abs(gateDist) == 0) {
                x = ch+"[31mOpen Gate. Brake immediately and Blow horn for 5 seconds."+ch+"[0m";
                writeToLog("Open Gate. Brake immediately and Blow horn for 5 seconds.\n");
            } else if(Math.abs(gateDist) < 1) {
                x = ch+"[31mOpen Gate. Brake immediately."+ch+"[0m";
                writeToLog("Open Gate. Brake immediately.\n");
            } else {
                x = ch+"[33mOpen Gate. Slow down."+ch+"[0m";
                writeToLog("Open Gate. Slow down.\n");
            }
        } else {
            x = "Normal operation";
        }
        return x;
     } 

     private static String objDetection(double objDist, double objSpeed) {
        String x = new String();
        char ch=27;
        if(Math.abs(objDist) <= 2) {
            if(Math.abs(objDist) <= 1) {
                if(objSpeed != 0) {
                    x = ch+"[31mMoving Object. Brake immediately."+ch+"[0m";
                    writeToLog("Moving Object. Brake immediately.\n");
                } else {
                    x = ch+"[31mStationary Object. Brake immediately."+ch+"[0m";
                    writeToLog("Stationary Object. Brake immediately.\n");
                }
            } else {
                if(objSpeed != 0) {
                    x = ch+"[33mMoving Object. Slow down."+ch+"[0m";
                    writeToLog("Moving Object. Slow down.\n");
                } else {
                    x = ch+"[33mStationary Object. Slow down."+ch+"[0m";
                    writeToLog("Stationary Object. Slow down.\n");
                }
            }
        } else {
            x = "Normal operation";
        }
        return x;
     }

    public static void main(String[] args) throws InterruptedException {
        Simulations sim = new Simulations();
        while(true){
            System.out.println("--------------------------------------------\n");
            displayInfo(sim.runSimulation());
            Thread.sleep(1000);
        }     
    }
}
