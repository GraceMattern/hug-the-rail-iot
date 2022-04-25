import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.math.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
// https://www.tutorialsfield.com/login-form-in-java-swing-with-source-code/

/**
 * Display.java is the GUI of the IOT HTR project and allows for manual input testing.
 * It displays a login where the 
 *       username is admin or user
 *       password is pass
 * The display will then prompt the user if the login crediential were correct.
 * The admin has the ability to test the software while the user can see operations.
 * 
 * The display will then prompt the admin to input values for the 3 main functionalities.
 * Then the software will report the respective warnings if the same data was recieved from the sensors.
 * 
 * If signed in as a non-admin, the user will see the trains operations (slippage, gate status, and obstructions)
 * 
 * To run Display.java, simply run Display.java
 */
public class Display extends JFrame implements ActionListener {
    // Create the login GUI
    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JCheckBox showPassword = new JCheckBox("Show Password");
    // GUI for manual user input
    private JFrame frame;
    private JPanel pane;
    private JTextField objSpeedField;
    private JTextField objDistField;
    private JTextField gateDistField;
    private JTextField gateStatusField;
    private JTextField wheelRPMField;
    private JTextField gpsSpeedField;
    private int objSpeed = 0;
    private int objDist = 0;
    private double gateDist = 0;
    private int gateStat = 0; // true 1 means its open, false 0 means its closed and safe to cross
    private int wheelRPM = 0;
    private int gpsSpeed = 0;

    Display() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        loginButton.setBounds(100, 300, 100, 30);
    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        showPassword.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // checks to see if the correct login credientials were provided
        if (e.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();
            if (userText.equalsIgnoreCase("admin") && pwdText.equalsIgnoreCase("pass")) {
                JOptionPane.showMessageDialog(this, "Login Successful");
            
                // if correct admin login, ask for manual user input
               
                pane = new JPanel();
                pane.setLayout(new GridLayout(0, 2, 2, 2));

                objSpeedField= new JTextField(5);;
                objDistField= new JTextField(5);;
                gateDistField= new JTextField(5);;
                gateStatusField= new JTextField(5);;
                wheelRPMField= new JTextField(5);;
                gpsSpeedField= new JTextField(5);;

                pane.add(new JLabel("what is the obstruction speed (mph)?"));
                pane.add(objSpeedField);

                pane.add(new JLabel("how far is the obstruction (mile)?"));
                pane.add(objDistField);
                
                pane.add(new JLabel("how far is the gate (mile)?"));
                pane.add(gateDistField);

                pane.add(new JLabel("What is the status of the gate (int: open=1, close=0)?"));
                pane.add(gateStatusField);

                pane.add(new JLabel("What is the wheel rpm (int)?"));
                pane.add( wheelRPMField);
                
                pane.add(new JLabel("What is the speed as given by the GPS (mph)?"));
                pane.add(gpsSpeedField);

                int option = JOptionPane.showConfirmDialog(frame, pane, "Please fill all the fields and press yes when completed", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                
                if (option == JOptionPane.NO_OPTION) {
                    return;
                } 
                
                if (option == JOptionPane.YES_OPTION) {
                    String objSpeedInput = objSpeedField.getText();
                    String objDistInput = objDistField.getText();
                    String gateDistInput = gateDistField.getText();
                    String gateStatusInput = gateStatusField.getText();
                    String wheelRPMInput = wheelRPMField.getText();
                    String gpsSpeedInput = gpsSpeedField.getText();

                    try {
                        objSpeed = Integer.parseInt(objSpeedInput);
                        objDist = Integer.parseInt(objDistInput);
                        gateDist = Double.parseDouble(gateDistInput);
                        gateStat = Integer.parseInt(gateStatusInput);
                        wheelRPM = Integer.parseInt(wheelRPMInput);
                        gpsSpeed = Integer.parseInt(gpsSpeedInput);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                    pane = new JPanel();
                    pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
                    
                    // Warning/recommendation generation
                    
                    if((gateStat == 1) && (gateDist <= 2)) {
                        if(Math.abs(gateDist) == 1) {
                            JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Open Gate! Break immediately and blow horn for 15 seconds.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Open Gate! Break immediately and blow horn for 15 seconds.\n");
                        } else if(Math.abs(gateDist) == 0) {
                            JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Open Gate! Break immediately and blow horn for 5 seconds.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Open Gate! Break immediately and blow horn for 5 seconds.\n");
                        } else if(Math.abs(gateDist) < 1) {
                            JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Open Gate! Break immediately.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Open Gate! Break immediately.\n");
                        } else {
                            JOptionPane.showMessageDialog(null ,"<html><div color=orange>"+"Open Gate! Slow down.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Open Gate! Slow down.\n");
                        }
                    }

                    // wheel circum = 1
                    int rpm_to_speed = (int) (wheelRPM * 1 * Math.PI * 60 / 63360);
                    if(Math.abs(rpm_to_speed - gpsSpeed) >= 5) {
                        if (Math.abs(rpm_to_speed - gpsSpeed) >= 10) {
                            JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Major Slippage! Break immediately.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Major Slippage! Break immediately.\n");
                        } else {
                            JOptionPane.showMessageDialog(null ,"<html><div color=orange>"+"Minor Slippage! Slow down.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Minor Slippage! Slow down.\n");
                        }
                    }

                    if(objDist <= 2) {
                        if(objDist <= 1) {
                            if(objSpeed != 0) {
                                JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Moving Object! Break immediately.", "Warning" , JOptionPane.ERROR_MESSAGE);
                                writeToLog("Moving Object! Break immediately.\n");
                            } else {
                                JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Stationary Object! Break immediately.", "Warning" , JOptionPane.ERROR_MESSAGE);
                                writeToLog("Stationary Object! Break immediately.\n");
                            }
                        } else {
                            if(objSpeed != 0) {
                                JOptionPane.showMessageDialog(null ,"<html><div color=orange>"+"Moving Object! Slow down.", "Warning" , JOptionPane.ERROR_MESSAGE);
                                writeToLog("Moving Object! Slow down.\n");
                            } else {
                                JOptionPane.showMessageDialog(null ,"<html><div color=orange>"+"Stationary Object! Slow down.", "Warning" , JOptionPane.ERROR_MESSAGE);
                                writeToLog("Stationary Object! Slow down.\n");
                            }
                        }
                    }

                    if ((gateStat != 1) && (Math.abs(rpm_to_speed - gpsSpeed) < 5) && (objDist > 2) ) {
                        if (gateDist == 0) {
                            JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Blow horn for 5 seconds.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Blow horn for 5 seconds.\n");
                        } else if (gateDist == 1) {
                            JOptionPane.showMessageDialog(null ,"<html><div color=red>"+"Blow horn for 15 seconds.", "Warning" , JOptionPane.ERROR_MESSAGE);
                            writeToLog("Blow horn for 15 seconds.\n");
                        } else {
                            JOptionPane.showMessageDialog(this ,"Normal operations");
                        }
                    }
                    return;
                }
            } 
            
            if (userText.equalsIgnoreCase("user") && pwdText.equalsIgnoreCase("pass")) {
                JOptionPane.showMessageDialog(this, "Login Successful");

                // if correct user login, display operations in terminal

                Simulations sim = new Simulations();
                while(true){
                    displayInfo(sim.runSimulation());
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }   
            }  
            
            if (
                (!userText.equalsIgnoreCase("admin") && !pwdText.equalsIgnoreCase("pass")) ||
                (!userText.equalsIgnoreCase("user") && !pwdText.equalsIgnoreCase("pass"))) {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        }
        
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        }
    }

    public static void displayInfo(double[] info) {
        JOptionPane.showMessageDialog(null, "Wheel Slippage: "+ slippageDetection(info[0], info[1]) + '\n' + 
                                            "Gate Status: "+ gateDetection(info[2], info[3]) + '\n' + 
                                            "Obstruction: "+ objDetection(info[4], info[5]));
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
    
    // the functions named Xdetection preform the logic to test if X data would generate warnings/recommendations
    
    private static String slippageDetection(double rpm, double speed) {
        // in this case circum is 1m
        // vehicle speed = wheel rpm * wheel diameter * pi * 60 / 63360
        String x = new String();
        double rpm_to_speed = rpm * 1 * Math.PI * 60 / 63360;
        if (Math.abs(rpm_to_speed - speed) >= 5) {
            if (Math.abs(rpm_to_speed - speed) >= 10) {
                x = "ALERT!   Major slippage. Brake immediately.";
                writeToLog("ALERT!   Major slippage. Brake immediately.\n");
            } else {
                x = "ALERT!   Minor slippage. Slow down.";
                writeToLog("ALERT!   Minor slippage. Slow down.\n");
            }
        } else {
            x = "Normal operation";
        }
        return x;
    }

    private static String gateDetection(double gateStat, double gateDist) {
        String x = new String();
        if ((gateStat == 1) && (Math.abs(gateDist) <= 3)) {
            if(Math.abs(gateDist) <= 2) {
                x = "ALERT!   Open Gate. Brake immediately.";
                writeToLog("ALERT!   Open Gate. Brake immediately.\n");
            } else {
                x = "ALERT!   Open Gate. Slow down.";
                writeToLog("ALERT!   Open Gate. Slow down.\n");
            }
        } else {
            x = "Normal operation";
        }
        return x;
    } 

    private static String objDetection(double objDist, double objSpeed) {
        String x = new String();
        if(Math.abs(objDist) <= 2) {
            if(Math.abs(objDist) <= 1) {
                if(objSpeed != 0) {
                    x = "ALERT!   Moving Object. Brake immediately.";
                    writeToLog("ALERT!   Moving Object. Brake immediately.\n");
                } else {
                    x = "ALERT!   Stationary Object. Brake immediately.";
                    writeToLog("ALERT!   Stationary Object. Brake immediately.\n");
                }
            } else {
                if(objSpeed != 0) {
                    x = "ALERT!   Moving Object. Slow down.";
                    writeToLog("ALERT!   Moving Object. Slow down.\n");
                } else {
                    x = "ALERT!   Stationary Object. Slow down.";
                    writeToLog("ALERT!   Stationary Object. Slow down.\n");
                }
            }
        } else {
            x = "Normal operation";
        }
        return x;
    }

    public static void main(String[] a) {
        Display frame = new Display();
        frame.setTitle("Login Form");
        frame.setVisible(true);
        frame.setBounds(10, 10, 370, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
}
