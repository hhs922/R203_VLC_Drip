package trial.app.management;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

public class LightingFrame extends JFrame {
    public LightingFrame() {
        super("Lighting Control");
        setSize(400, 400);
        setLayout(new GridLayout(2, 3));

        // Set the panel to add buttons
//        JPanel panel = new JPanel();
        JPanel luxPanel = new JPanel();
        JPanel cctPanel = new JPanel();
        JPanel idPanel = new JPanel();

        // Add luxStatus label to show the luxStatus of the slider
        JLabel luxStatus = new JLabel("亮度", JLabel.CENTER);
        luxStatus.setFont(new java.awt.Font("微軟正黑體", Font.BOLD, 18));
        JLabel cctStatus = new JLabel("色溫", JLabel.CENTER);
        cctStatus.setFont(new java.awt.Font("微軟正黑體", Font.BOLD, 18));
        JLabel idStatus = new JLabel("燈號", JLabel.CENTER);
        idStatus.setFont(new java.awt.Font("微軟正黑體", Font.BOLD, 18));

        // Set the slider
//        JSlider luxSlider = new JSlider(0, 100, 50);
        JSlider luxSlider = new JSlider(JSlider.VERTICAL,0, 100, 50);
        luxSlider.setMajorTickSpacing(10);
        luxSlider.setPaintTicks(true);
        luxSlider.setSnapToTicks(true);
        luxSlider.setPreferredSize(new Dimension (100,250));

        JSlider cctSlider = new JSlider(JSlider.VERTICAL,3500, 5500, 4500);
        cctSlider.setMajorTickSpacing(500);
        cctSlider.setPaintTicks(true);
        cctSlider.setSnapToTicks(true);
        cctSlider.setPreferredSize(new Dimension (100,250));

        JSlider idSlider = new JSlider(JSlider.VERTICAL,0, 9, 0);
        idSlider.setMajorTickSpacing(1);
        idSlider.setPaintTicks(true);
        idSlider.setSnapToTicks(true);
        idSlider.setPreferredSize(new Dimension (100,250));

        // Set the labels to be painted on the slider
        luxSlider.setPaintLabels(true);
        cctSlider.setPaintLabels(true);
        idSlider.setPaintLabels(true);

        // Add positions label in the slider
        Hashtable<Integer, JLabel> luxPosition = new Hashtable<Integer, JLabel>();
        luxPosition.put(0, new JLabel("0"));
        luxPosition.put(10, new JLabel("10"));
        luxPosition.put(20, new JLabel("20"));
        luxPosition.put(30, new JLabel("30"));
        luxPosition.put(40, new JLabel("40"));
        luxPosition.put(50, new JLabel("50"));
        luxPosition.put(60, new JLabel("60"));
        luxPosition.put(70, new JLabel("70"));
        luxPosition.put(80, new JLabel("80"));
        luxPosition.put(90, new JLabel("90"));
        luxPosition.put(100, new JLabel("100"));
        luxStatus.setForeground( new Color(218,165,32));

        Hashtable<Integer, JLabel> cctPosition = new Hashtable<Integer, JLabel>();
//        cctPosition.put(3000, new JLabel("3000"));
        cctPosition.put(3500, new JLabel("3500"));
        cctPosition.put(4000, new JLabel("4000"));
        cctPosition.put(4500, new JLabel("4500"));
        cctPosition.put(5000, new JLabel("5000"));
        cctPosition.put(5500, new JLabel("5500"));
//        cctPosition.put(6000, new JLabel("6000"));
//        cctPosition.put(6500, new JLabel("6500"));
        cctStatus.setForeground( new Color(32,178,170));

        Hashtable<Integer, JLabel> idPosition = new Hashtable<Integer, JLabel>();
        idPosition.put(0, new JLabel("all"));
        idPosition.put(1, new JLabel("1"));
        idPosition.put(2, new JLabel("2"));
        idPosition.put(3, new JLabel("3"));
        idPosition.put(4, new JLabel("4"));
        idPosition.put(5, new JLabel("5"));
        idPosition.put(6, new JLabel("6"));
        idPosition.put(7, new JLabel("7"));
        idPosition.put(8, new JLabel("8"));
        idPosition.put(9, new JLabel("9"));
        idStatus.setForeground( new Color(46,139,87));

        // Set the label to be drawn
        luxSlider.setLabelTable(luxPosition);
        cctSlider.setLabelTable(cctPosition);
        idSlider.setLabelTable(idPosition);

        // Add change listener to the slider
        luxSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                luxStatus.setText("亮度: " + ((JSlider)e.getSource()).getValue()+" %");
                //if (luxSlider.getValue() % luxSlider.getMajorTickSpacing() == 0) {
                if (! luxSlider.getValueIsAdjusting()) {
                    sendMsg(cctSlider.getValue(), luxSlider.getValue(), idSlider.getValue());
                }
            }
        });

        cctSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                cctStatus.setText("色溫: " + ((JSlider)e.getSource()).getValue()+" K");
                if (! cctSlider.getValueIsAdjusting()) {
                    sendMsg(cctSlider.getValue(), luxSlider.getValue(), idSlider.getValue());
                }
            }
        });

        idSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                idStatus.setText("燈號: " + ((JSlider)e.getSource()).getValue());
                if (! idSlider.getValueIsAdjusting()) {
                    sendMsg(cctSlider.getValue(), luxSlider.getValue(), idSlider.getValue());
                }
            }
        });

        // Add the slider to the panel
        luxPanel.add(luxSlider);
        cctPanel.add(cctSlider);
        idPanel.add(idSlider);

        // Set the window to be visible as the default to be false
        add(luxPanel);
        add(cctPanel);
        add(idPanel);
        add(luxStatus);
        add(cctStatus);
        add(idStatus);
        pack();
        setVisible(true);

//        add(luxPanel);
//        add(luxStatus);
////        add(luxPanel);
//        pack();
//        setVisible(true);
//
//        add(cctPanel);
//        add(cctStatus);
////        add(cctPanel);
//        pack();
//        setVisible(true);
//
//        add(idPanel);
//        add(idStatus);
////        add(idPanel);
//        pack();
//        setVisible(true);
    }

    public void sendMsg(int cct, int lux, int id) {
        try {
//            String msg = String.format("http://54.210.146.204/itriHF/cct/send50/?cct=%d&lux=%d&id=%d", cct, lux, id);
            String msg = String.format("http://192.168.0.106/vlc_v13_lightcontrol?brightness=%d&driverId=%d&colorTemperature=%d", lux, id, cct);
            debug(msg);
            getStringURLResources(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStringURLResources(final String queryrasie) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb = new StringBuffer();
                String line = null;
                try {
                    URL url = new URL(queryrasie);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStreamReader INread = new InputStreamReader(urlConn.getInputStream());
                    BufferedReader buffer1 = new BufferedReader(INread);
                    while ((line = buffer1.readLine()) != null) {
                        sb.append(line);
                    }
                    System.out.println(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void debug(String msg) {
        System.out.println(msg);
    }
}