import java.util.Random;
/**
 * Simulation.java is the method that assigns rng values
 * to each input every 1 second
 */

public class Simulations {
    private static final int MAX_rpm = 20000; // converts to 60mph
    private static final double SAFETY = 3;
    private static final double INTERVAL = 1.0;

    private Random rng = new Random();

    private double rpm;
    private double speed;
    private int gateStat;
    private double gateDist;
    private double objDist;
    private double objSpeed;
    private boolean station;

    // assigns the initial values of the inputs
    public Simulations() {
        rpm = 0.0;
        speed = 0;
        gateStat = 1;
        gateDist = (rng.nextInt(51-0) + 0); 
        objDist = (rng.nextInt(51-0)+0); 
        objSpeed = 0;
    }
    
    public double[] runSimulation() {
        if(rpm <= 0) { // start engine up and running : speeding up
            speed = 5*INTERVAL;
            rpm+=speed;
            station =false;
        } else if (station || objDist < SAFETY || rpm > MAX_rpm) { // at station or obj nearby: slowing down
            rpm += -3.0*INTERVAL;
            if(rpm<0){
                rpm = 0;
            }
            objDist +=(((rng.nextInt(2)+1)/10.0)*INTERVAL); 
        } 
        // else if(rpm > MIN_rpm){
        //     speed = 1.5;
        //     rpm+= speed;
        //     objSpeed += (((rng.nextInt(2))/10.0)*INTERVAL);
        // } 
        else {
            speed = (rpm<MAX_rpm) ? (1.5*INTERVAL): 0;
            rpm += speed;
            objSpeed += ((((rng.nextInt(5))-2)/10.0)*INTERVAL);
            if(rng.nextInt(10)==9 && objSpeed>=SAFETY){
                station = true;
            }
        }
        // the following four inputs are independent of speed, rpm, station
        gateStat =  rng.nextInt(2);
        gateDist =  rng.nextInt(101-0)+0;
        objDist = (rng.nextInt(51-0)+0);
        objSpeed = (rng.nextInt(61-0)+0);

        // format and return results
        double[] result = {rpm, speed, gateStat, gateDist, objDist, objSpeed};
        return result;
    }
}
