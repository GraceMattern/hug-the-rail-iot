/**
 * Sensor.java is the sensor class that hold/sets/gets data that
 * relates to the respective sensor
 */
public class Sensors {
    // individual sensor instances
    Lidar           lidar           = new Lidar();
    GPS             gps             = new GPS();
    Tachometer      tachometer      = new Tachometer();
    Radar           radar           = new Radar();

    public class Lidar {
        private int distance;
        private int speed;
    
        public Lidar() {
            this.distance = 0;
            this.speed = 0;
        }
        public void LidarDist(int distance){
            this.distance = distance;
            this.speed = 0;
        }
        public void LidarSpeed(int speed){
            this.distance = 0;
            this.speed = speed;
        }
        public Lidar(int distance, int speed){
            this.distance = distance;
            this.speed = speed;
        }
        public void setDistance(int distance){
            this.distance = distance;
        }
        public void setSpeed(int speed){
            this.speed = speed;
        }
        public int getDistance(){
            return distance;
        }
        public int getSpeed(){
            return speed;
        }
    }

    public class GPS {
        private int speed;
       
        public GPS(){
            this.speed = 0;
        }
        public GPS(int speed){
            this.speed = speed;
        }
        public void setSpeed(int speed) {
            this.speed = speed;
        }
        public int getSpeed() {
            return speed;
        }
    }

    public class Tachometer{
        private int rpm;
    
        public Tachometer(){
            this.rpm = 0;
        }
        public Tachometer(int rpm) {
            this.rpm = rpm;
        }
        public void setTachometer(int rpm) {
            this.rpm = rpm;
        }
        public int getTachometer(){
            return rpm;
        }
    }
    
    public class Radar {
        private int distance;
        private int gateStat;
    
        public Radar() {
            this.distance = 0;
            this.gateStat = 0;
        }
        public void RadarDist(int distance){
            this.distance = distance;
            this.gateStat = 0;
        }
        public void RadarGate(int gateStat){
            this.distance = 0;
            this.gateStat = gateStat;
        }
        public Radar(int distance, int gateStat){
            this.distance = distance;
            this.gateStat = gateStat;
        }
        public void setDistance(int distance){
            this.distance = distance;
        }
        public void setGateStat(int gateStat){
            this.gateStat = gateStat;
        }
        public int getDistance(){
            return distance;
        }
        public int getGateStat(){
            return gateStat;
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return returns dist from lidar
     */
    public int getLidarDist() {
        return lidar.getDistance();
    }

    /**
     * @return returns speed from lidar
     */
    public int getLidarSpeed() {
        return lidar.getSpeed();
    }

    /**
     * @return returns speed from GPS
     */
    public int getGPSSpeed() {
        return gps.getSpeed();
    }

    /**
     * @return returns wheel speed from tachometer
     */
    public double getRPM() {
        return tachometer.getTachometer();
    }

    /**
     * @return returns dist from radar
     */
    public int getRadarDist() {
        return radar.getDistance();
    }

    /**
     * @return returns gate status from radar
     */
    public int getRadarGateStat() {
        return radar.getGateStat();
    }

}
