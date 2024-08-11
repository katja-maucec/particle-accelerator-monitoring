import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
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

    final int WIDTH = 160;

    final int HEIGHT = 320;

    final Font HEADER1 = new Font("Dialog", Font.BOLD, 18);

    final Color COL_COLOR = new Color(206, 132, 132);

    final EmptyBorder HEADER_BORDER = new EmptyBorder(20, 20, 10, 20);

    public GUI() {
        /* Loading GUI Frame */
        super("Particle Accelerator Monitoring");
        setBackground(new Color(229, 191, 191));
        
        //row 1
        JLabel title = new JLabel("Live Sensor Monitoring", JLabel.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 30));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(title);

        //row 3
        JPanel sensorRow = new JPanel();

        //row 3 col 1
        JPanel col1 = new JPanel();
        col1.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        col1.setBackground(COL_COLOR);
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));

        JLabel tempText = new JLabel("Temperature", JLabel.CENTER);
        JLabel curTemp = new JLabel("---");
        JLabel tempTime = new JLabel("\n");
        JLabel tempStatus = new JLabel("Status: OFF");
        JLabel tempAlert = new JLabel("\n", JLabel.CENTER);

        tempText.setFont(HEADER1);
        tempText.setBorder(HEADER_BORDER);
        tempText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        curTemp.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        tempTime.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        tempStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        tempStatus.setBorder(new EmptyBorder(0, 0, 10, 0));
        tempAlert.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        tempAlert.setMaximumSize(new Dimension(130, 100));
        tempAlert.setForeground(new Color(255, 255, 255));

        col1.add(tempText);
        col1.add(curTemp);
        col1.add(tempTime);
        col1.add(tempStatus);

        //add buttons
        JButton tempOn = new JButton("Turn on");
        col1.add(tempOn);
        tempOn.setEnabled(true);
        tempOn.setFocusPainted(false);
        tempOn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton tempMon = new JButton("Start");
        col1.add(tempMon);
        tempMon.setEnabled(false);
        tempMon.setFocusPainted(false);
        tempMon.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton tempStop = new JButton("Pause");
        col1.add(tempStop);
        tempStop.setEnabled(false);
        tempStop.setFocusPainted(false);
        tempStop.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton tempOff = new JButton("Turn off");
        col1.add(tempOff);
        tempOff.setEnabled(false);
        tempOff.setFocusPainted(false);
        tempOff.setAlignmentX(JLabel.CENTER_ALIGNMENT);

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
                tempTime.setText("(Last at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + ")");

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

        //row 3 col 2
        JPanel col2 = new JPanel();
        col2.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        col2.setBackground(COL_COLOR);
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));

        JLabel humText = new JLabel("Humidity", JLabel.CENTER);
        JLabel curHum = new JLabel("---");
        JLabel humTime = new JLabel("\n");
        JLabel humStatus = new JLabel("Status: OFF");
        JLabel humAlert = new JLabel("\n", JLabel.CENTER);

        humText.setFont(HEADER1);
        humText.setBorder(HEADER_BORDER);
        humText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        curHum.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        humTime.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        humStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        humStatus.setBorder(new EmptyBorder(0, 0, 10, 0));
        humAlert.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        humAlert.setMaximumSize(new Dimension(130, 100));
        humAlert.setForeground(new Color(255, 255, 255));

        col2.add(humText);
        col2.add(curHum);
        col2.add(humTime);
        col2.add(humStatus);

        //add buttons
        JButton humOn = new JButton("Turn on");
        col2.add(humOn);
        humOn.setEnabled(true);
        humOn.setFocusPainted(false);
        humOn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton humMon = new JButton("Start");
        col2.add(humMon);
        humMon.setEnabled(false);
        humMon.setFocusPainted(false);
        humMon.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton humStop = new JButton("Pause");
        col2.add(humStop);
        humStop.setEnabled(false);
        humStop.setFocusPainted(false);
        humStop.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton humOff = new JButton("Turn off");
        col2.add(humOff);
        humOff.setEnabled(false);
        humOff.setFocusPainted(false);
        humOff.setAlignmentX(JLabel.CENTER_ALIGNMENT);

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
                humTime.setText("(Last at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + ")");

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

        //row 3 col 3
        JPanel col3 = new JPanel();
        col3.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        col3.setBackground(COL_COLOR);
        col3.setLayout(new BoxLayout(col3, BoxLayout.Y_AXIS));

        JLabel radText = new JLabel("Radiation", JLabel.CENTER);
        JLabel curRad = new JLabel("---");
        JLabel radTime = new JLabel("\n");
        JLabel radStatus = new JLabel("Status: OFF");
        JLabel radAlert = new JLabel("\n", JLabel.CENTER);

        radText.setFont(HEADER1);
        radText.setBorder(HEADER_BORDER);
        radText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        curRad.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        radTime.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        radStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        radStatus.setBorder(new EmptyBorder(0, 0, 10, 0));
        radAlert.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        radAlert.setMaximumSize(new Dimension(130, 100));
        radAlert.setForeground(new Color(255, 255, 255));

        col3.add(radText);
        col3.add(curRad);
        col3.add(radTime);
        col3.add(radStatus);

        //add buttons
        JButton radOn = new JButton("Turn on");
        col3.add(radOn);
        radOn.setEnabled(true);
        radOn.setFocusPainted(false);
        radOn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton radMon = new JButton("Start");
        col3.add(radMon);
        radMon.setEnabled(false);
        radMon.setFocusPainted(false);
        radMon.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton radStop = new JButton("Pause");
        col3.add(radStop);
        radStop.setEnabled(false);
        radStop.setFocusPainted(false);
        radStop.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton radOff = new JButton("Turn off");
        col3.add(radOff);
        radOff.setEnabled(false);
        radOff.setFocusPainted(false);
        radOff.setAlignmentX(JLabel.CENTER_ALIGNMENT);

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
                radTime.setText("(Last at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + ")");

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

        //row 3 col 4
        JPanel col4 = new JPanel();
        col4.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        col4.setBackground(COL_COLOR);
        col4.setLayout(new BoxLayout(col4, BoxLayout.Y_AXIS));

        JLabel presText = new JLabel("Pressure", JLabel.CENTER);
        JLabel curPres = new JLabel("---");
        JLabel presTime = new JLabel("\n");
        JLabel presStatus = new JLabel("Status: OFF");
        JLabel presAlert = new JLabel("\n", JLabel.CENTER);

        presText.setFont(HEADER1);
        presText.setBorder(new EmptyBorder(20, 25, 10, 25));
        presText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        curPres.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        presTime.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        presStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        presStatus.setBorder(new EmptyBorder(0, 0, 10, 0));
        presAlert.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        presAlert.setMaximumSize(new Dimension(130, 100));
        presAlert.setForeground(new Color(255, 255, 255));

        col4.add(presText);
        col4.add(curPres);
        col4.add(presTime);
        col4.add(presStatus);

        //add buttons
        JButton presOn = new JButton("Turn on");
        col4.add(presOn);
        presOn.setEnabled(true);
        presOn.setFocusPainted(false);
        presOn.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton presMon = new JButton("Start");
        col4.add(presMon);
        presMon.setEnabled(false);
        presMon.setFocusPainted(false);
        presMon.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton presStop = new JButton("Pause");
        col4.add(presStop);
        presStop.setEnabled(false);
        presStop.setFocusPainted(false);
        presStop.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        JButton presOff = new JButton("Turn off");
        col4.add(presOff);
        presOff.setEnabled(false);
        presOff.setFocusPainted(false);
        presOff.setAlignmentX(JLabel.CENTER_ALIGNMENT);

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
                presTime.setText("(Last at: " + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + ")");

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
                        tempAlert.setText("<html><center>ALERT: Temperature above -268.5 \u2103; press Start to continue measuring.</center></html>");
                        tempStop.doClick();
                    } else if (monitor == "too low") {
                        tempAlert.setText("<html><center>ALERT: Temperature below -269.5 \u2103; press Start to continue measuring.</center></html>");
                        tempStop.doClick();
                    } else {
                        tempAlert.setText("\n");
                    }

                    pack();
                }

                if (humSensor.getState() == State.MEASURING) {
                    //generate data
                    double humVal = humSensor.generateData();

                    //display data
                    curHum.setText(String.format("%.3f %%", humVal));

                    //archive and monitor
                    String monitor = service.measure(humSensor, humVal);
                    if (monitor == "too high") {
                        humAlert.setText("<html><center>ALERT: Humidity above 10 &#37;; press Start to continue measuring.</center></html>");
                        humStop.doClick();
                    } else {
                        humAlert.setText("\n");
                    }

                    pack();
                }

                if (radSensor.getState() == State.MEASURING) {
                    //generate data
                    double radVal = radSensor.generateData();
                
                    //display data
                    curRad.setText(String.format("%.3f MeV", radVal));
                
                    //archive and monitor
                    String monitor = service.measure(radSensor, radVal);
                    if (monitor == "too high") {
                        radAlert.setText("<html><center>ALERT: Radiation above 40 MeV; press Start to continue measuring.</center></html>");
                        radStop.doClick();
                    } else {
                        radAlert.setText("\n");
                    }

                    pack();
                }

                if (presSensor.getState() == State.MEASURING) {
                    //generate data
                    double presVal = presSensor.generateData();
                
                    //display data
                    curPres.setText(String.format("%.3f nanombar", presVal));
                
                    //archive and monitor
                    String monitor = service.measure(presSensor, presVal);
                    if (monitor == "too high") {
                        presAlert.setText("<html><center>ALERT: Pressure above 1 nanombar; press Start to continue measuring.</center></html>");
                        presStop.doClick();
                    } else {
                        presAlert.setText("\n");
                    }

                    pack();
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
