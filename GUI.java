import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class GUI extends JFrame{

    /* Constructing sensor objects as instance variables*/
    //components of FAIR are cooled to as low as -269C and need thermal control within +/-0.5C
    Sensor tempSensor = new Sensor(State.OFF, -269.5, -268.5, "temperature", true);

    //humidity sensor needs to stay within 10%
    Sensor humSensor = new Sensor(State.OFF,  0.0, 10.0, "humidity", false);

    //energy must be kept below 5MeV to avoid producing radioactivities in the sample
    //due to the photonuclear effect which has resonance peak between 5-40MeV
    Sensor radSensor = new Sensor(State.OFF, 5.0, 40.0, "radiation", false);

    //particle accelerators require ultra high vacuum with pressure levels ranging from 
    //10^(-9) - 10^(-12) mbar; i.e. 1 - 10^(-3) nanombar
    Sensor presSensor = new Sensor(State.OFF, Math.pow(10, -3), 1, "pressure", false);

    //service instance variable responsible for archiving and monitoring
    Service service = new Service();

    public GUI() {
        /* Loading GUI Frame */
        super("Particle Accelerator Monitoring");
        
        //row 1
        JLabel title = new JLabel("Live Sensor Monitoring", JLabel.CENTER);
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(title);

        //row 3
        JPanel sensorRow = new JPanel();

        //row 3 col 1
        JPanel col1 = new JPanel();

        JLabel tempText = new JLabel("Temperature");
        JLabel curTemp = new JLabel("---");
        JLabel tempTime = new JLabel("\n");
        JLabel tempStatus = new JLabel("Status: OFF");
        JLabel tempAlert = new JLabel("\n");

        col1.add(tempText);
        col1.add(curTemp);
        col1.add(tempTime);
        col1.add(tempStatus);

        //add buttons
        JButton tempOn = new JButton("Turn on");
        col1.add(tempOn);
        tempOn.setEnabled(true);
        tempOn.setFocusPainted(false);
        
        JButton tempMon = new JButton("Start");
        col1.add(tempMon);
        tempMon.setEnabled(false);
        tempMon.setFocusPainted(false);

        JButton tempStop = new JButton("Pause");
        col1.add(tempStop);
        tempStop.setEnabled(false);
        tempStop.setFocusPainted(false);
        
        JButton tempOff = new JButton("Turn off");
        col1.add(tempOff);
        tempOff.setEnabled(false);
        tempOff.setFocusPainted(false);

        col1.add(tempAlert);

        //manage action listeners
        tempOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update tempSensor's state
                tempSensor.setState(State.ON);

                //remove time display
                tempTime.setText("\n");

                //update displayed status
                tempStatus.setText("Status: ON");

                //enable other buttons; disable on button
                tempOn.setEnabled(false);
                tempMon.setEnabled(true);
                tempOff.setEnabled(true);
            }
        });

        tempMon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                tempSensor.setState(State.MEASURING);

                //remove time display
                tempTime.setText("\n");

                //update displayed status
                tempStatus.setText("Status: MEAS");

                //disable measuring button
                tempMon.setEnabled(false);
                tempStop.setEnabled(true);
            }
        });

        tempStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                tempSensor.setState(State.IDLE);

                //add time of last measurement
                tempTime.setText("Taken at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));

                //update status
                tempStatus.setText("Status: IDLE");

                //update buttons
                tempMon.setEnabled(true);
                tempStop.setEnabled(false);
            }
        });

        tempOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                tempSensor.setState(State.OFF);

                //remove time display
                tempTime.setText("\n");

                //update status
                tempStatus.setText("Status: OFF");

                //update text
                curTemp.setText("---");

                //update buttons
                tempOn.setEnabled(true);
                tempMon.setEnabled(false);
                tempStop.setEnabled(false);
                tempOff.setEnabled(false);
            }
        });

        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));

        //row 3 col 2
        JPanel col2 = new JPanel();

        JLabel humText = new JLabel("Humidity");
        JLabel curHum = new JLabel("---");
        JLabel humTime = new JLabel("\n");
        JLabel humStatus = new JLabel("Status: OFF");
        JLabel humAlert = new JLabel("\n");

        col2.add(humText);
        col2.add(curHum);
        col2.add(humTime);
        col2.add(humStatus);

        //add buttons
        JButton humOn = new JButton("Turn on");
        col2.add(humOn);
        humOn.setEnabled(true);

        
        JButton humMon = new JButton("Start");
        col2.add(humMon);
        humMon.setEnabled(false);

        JButton humStop = new JButton("Pause");
        col2.add(humStop);
        humStop.setEnabled(false);
        
        JButton humOff = new JButton("Turn off");
        col2.add(humOff);
        humOff.setEnabled(false);

        col2.add(humAlert);

        //manage action listeners
        humOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update humSensor's state
                humSensor.setState(State.ON);

                //remove time display
                humTime.setText("\n");

                //update displayed status
                humStatus.setText("Status: ON");

                //enable other buttons; disable on button
                humOn.setEnabled(false);
                humMon.setEnabled(true);
                humOff.setEnabled(true);
            }
        });

        humMon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                humSensor.setState(State.MEASURING);

                //remove time display
                humTime.setText("\n");

                //update displayed status
                humStatus.setText("Status: MEAS");

                //disable measuring button
                humMon.setEnabled(false);
                humStop.setEnabled(true);
            }
        });

        humStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                humSensor.setState(State.IDLE);

                //add time of last measurement
                humTime.setText("Taken at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));

                //update status
                humStatus.setText("Status: IDLE");

                //update buttons
                humMon.setEnabled(true);
                humStop.setEnabled(false);
            }
        });

        humOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                humSensor.setState(State.OFF);

                //remove time display
                humTime.setText("\n");

                //update status
                humStatus.setText("Status: OFF");

                //update text
                curHum.setText("---");

                //update buttons
                humOn.setEnabled(true);
                humMon.setEnabled(false);
                humStop.setEnabled(false);
                humOff.setEnabled(false);
            }
        });

        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));

        //row 3 col 3
        JPanel col3 = new JPanel();

        JLabel radText = new JLabel("Radiation");
        JLabel curRad = new JLabel("---");
        JLabel radTime = new JLabel("\n");
        JLabel radStatus = new JLabel("Status: OFF");
        JLabel radAlert = new JLabel("\n");

        col3.add(radText);
        col3.add(curRad);
        col3.add(radTime);
        col3.add(radStatus);

        //add buttons
        JButton radOn = new JButton("Turn on");
        col3.add(radOn);
        radOn.setEnabled(true);
        
        JButton radMon = new JButton("Start");
        col3.add(radMon);
        radMon.setEnabled(false);

        JButton radStop = new JButton("Pause");
        col3.add(radStop);
        radStop.setEnabled(false);
        
        JButton radOff = new JButton("Turn off");
        col3.add(radOff);
        radOff.setEnabled(false);

        col3.add(radAlert);

        //manage action listeners
        radOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update radSensor's state
                radSensor.setState(State.ON);

                //remove time display
                radTime.setText("\n");

                //update displayed status
                radStatus.setText("Status: ON");

                //enable other buttons; disable on button
                radOn.setEnabled(false);
                radMon.setEnabled(true);
                radOff.setEnabled(true);
            }
        });

        radMon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                radSensor.setState(State.MEASURING);

                //remove time display
                radTime.setText("\n");

                //update displayed status
                radStatus.setText("Status: MEAS");

                //disable measuring button
                radMon.setEnabled(false);
                radStop.setEnabled(true);
            }
        });

        radStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                radSensor.setState(State.IDLE);

                //add time of last measurement
                radTime.setText("Taken at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));

                //update status
                radStatus.setText("Status: IDLE");

                //update buttons
                radMon.setEnabled(true);
                radStop.setEnabled(false);
            }
        });

        radOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                radSensor.setState(State.OFF);

                //remove time display
                radTime.setText("\n");

                //update status
                radStatus.setText("Status: OFF");

                //update text
                curRad.setText("---");

                //update buttons
                radOn.setEnabled(true);
                radMon.setEnabled(false);
                radStop.setEnabled(false);
                radOff.setEnabled(false);
            }
        });

        col3.setLayout(new BoxLayout(col3, BoxLayout.Y_AXIS));

        //row 3 col 4
        JPanel col4 = new JPanel();

        JLabel presText = new JLabel("Pressure");
        JLabel curPres = new JLabel("---");
        JLabel presTime = new JLabel("\n");
        JLabel presStatus = new JLabel("Status: OFF");
        JLabel presAlert = new JLabel("\n");

        col4.add(presText);
        col4.add(curPres);
        col4.add(presTime);
        col4.add(presStatus);

        //add buttons
        JButton presOn = new JButton("Turn on");
        col4.add(presOn);
        presOn.setEnabled(true);
        
        JButton presMon = new JButton("Start");
        col4.add(presMon);
        presMon.setEnabled(false);

        JButton presStop = new JButton("Pause");
        col4.add(presStop);
        presStop.setEnabled(false);
        
        JButton presOff = new JButton("Turn off");
        col4.add(presOff);
        presOff.setEnabled(false);

        col4.add(presAlert);

        //manage action listeners
        presOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update presSensor's state
                presSensor.setState(State.ON);

                //remove time display
                presTime.setText("\n");

                //update displayed status
                presStatus.setText("Status: ON");

                //enable other buttons; disable on button
                presOn.setEnabled(false);
                presMon.setEnabled(true);
                presOff.setEnabled(true);
            }
        });

        presMon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                presSensor.setState(State.MEASURING);

                //remove time display
                presTime.setText("\n");

                //update displayed status
                presStatus.setText("Status: MEAS");

                //disable measuring button
                presMon.setEnabled(false);
                presStop.setEnabled(true);
            }
        });

        presStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                presSensor.setState(State.IDLE);

                //add time of last measurement
                presTime.setText("Taken at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));

                //update status
                presStatus.setText("Status: IDLE");

                //update buttons
                presMon.setEnabled(true);
                presStop.setEnabled(false);
            }
        });

        presOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //update state
                presSensor.setState(State.OFF);

                //remove time display
                presTime.setText("\n");

                //update status
                presStatus.setText("Status: OFF");

                //update text
                curPres.setText("---");

                //update buttons
                presOn.setEnabled(true);
                presMon.setEnabled(false);
                presStop.setEnabled(false);
                presOff.setEnabled(false);
            }
        });

        col4.setLayout(new BoxLayout(col4, BoxLayout.Y_AXIS));

        sensorRow.add(col1);
        sensorRow.add(col2);
        sensorRow.add(col3);
        sensorRow.add(col4);
        sensorRow.setLayout(new FlowLayout());

        add(sensorRow);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        //panel updating
        int delay = 1000;
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                
                if (tempSensor.getState() == State.MEASURING) {
                    //generate data
                    double tempVal = tempSensor.generateData();

                    //display data
                    curTemp.setText(String.format("%.3f \u2103", tempVal));

                    //archive and monitor
                    String monitor = service.measure(tempSensor, tempVal);
                    if (monitor == "too high") {
                        tempAlert.setText("<html>ALERT: <br/>Temperature above <br/>-268.5 \u2103</html>");
                        tempStop.doClick();
                        pack();
                    } else if (monitor == "too low") {
                        tempAlert.setText("<html>ALERT: <br/>Temperature below <br/>-269.5 \u2103</html>");
                        tempStop.doClick();
                        pack();
                    } else {
                        tempAlert.setText("\n");
                    }
                }

                if (humSensor.getState() == State.MEASURING) {
                    //generate data
                    double humVal = humSensor.generateData();

                    //display data
                    curHum.setText(String.format("%.3f %%", humVal));

                    //archive and monitor
                    String monitor = service.measure(humSensor, humVal);
                    if (monitor == "too high") {
                        humAlert.setText("<html>ALERT: <br/>Humidity above <br/>10 &#37;</html>");
                        humStop.doClick();
                        pack();
                    } else if (monitor == "too low") {
                        humAlert.setText("<html>ALERT: <br/>Humidity below <br/>0 &#37;</html>");
                        humStop.doClick();
                        pack();
                    } else {
                        humAlert.setText("\n");
                    }
                }

                if (radSensor.getState() == State.MEASURING) {
                    //generate data
                    double radVal = radSensor.generateData();
                
                    //display data
                    curRad.setText(String.format("%.3f Mev", radVal));
                
                    //archive and monitor
                    String monitor = service.measure(radSensor, radVal);
                    if (monitor == "too high") {
                        radAlert.setText("<html>ALERT: <br/>Radiation above <br/>40 MeV</html>");
                        radStop.doClick();
                        pack();
                    } else if (monitor == "too low") {
                        radAlert.setText("<html>ALERT: <br/>Radiation below <br/>5 MeV</html>");
                        radStop.doClick();
                        pack();
                    } else {
                        radAlert.setText("\n");
                    }
                }

                if (presSensor.getState() == State.MEASURING) {
                    //generate data
                    double presVal = presSensor.generateData();
                
                    //display data
                    curPres.setText(String.format("%.3f nanombar", presVal));
                
                    //archive and monitor
                    String monitor = service.measure(presSensor, presVal);
                    if (monitor == "too high") {
                        presAlert.setText("<html>ALERT: <br/>Pressure above <br/>1 nanombar</html>");
                        presStop.doClick();
                        pack();
                    } else if (monitor == "too low") {
                        presAlert.setText("<html>ALERT: <br/>Pressure below <br/>10<sup>-3</sup> nanombar</html>");
                        presStop.doClick();
                        pack();
                    } else {
                        presAlert.setText("\n");
                    }
                }
            }
        };
        new Timer(delay, action).start();

        //display frame
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    service.saveToFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });
        pack();
        setVisible(true);
    }
    public static void main(String[] args) {

        /* Invoke Swing for GUI */
        SwingUtilities.invokeLater(GUI::new);
    }
}
