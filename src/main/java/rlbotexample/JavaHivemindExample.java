package rlbotexample;

import rlbot.manager.HivemindManager;
import rlbot.pyinterop.HiveSocketServer;
import rlbotexample.util.PortReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * See JavaAgent.py for usage instructions.
 *
 * Look inside MyHivemind.java for the actual bot logic!
 */
public class JavaHivemindExample {

    private static final int DEFAULT_PORT = 20321;

    public static void main(String[] args) {

        HivemindManager hivemindManager = new HivemindManager(MyHivemind::new);
        int port = PortReader.readPortFromArgs(args).orElseGet(() -> {
            System.out.println("Could not read port from args, using default!");
            return DEFAULT_PORT;
        });

        HiveSocketServer server = new HiveSocketServer(port, hivemindManager);
        new Thread(server::start).start();

        displayWindow(hivemindManager, port);
    }
    
    private static void displayWindow(HivemindManager hivemindManager, int port) {
        JFrame frame = new JFrame("Java Hivemind Bot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        BorderLayout borderLayout = new BorderLayout();
        panel.setLayout(borderLayout);
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        dataPanel.add(new JLabel("Listening on port " + port), BorderLayout.CENTER);
        dataPanel.add(new JLabel("I'm the thing controlling the Java hivemind bot, keep me open :)"), BorderLayout.CENTER);
        JLabel blueHive = new JLabel("Blue hive indices: ");
        JLabel orangeHive = new JLabel("Orange hive indices: ");
        dataPanel.add(blueHive, BorderLayout.CENTER);
        dataPanel.add(orangeHive, BorderLayout.CENTER);
        panel.add(dataPanel, BorderLayout.CENTER);
        frame.add(panel);

        URL url = JavaHivemindExample.class.getClassLoader().getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);
        panel.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        frame.setIconImage(image);

        frame.pack();
        frame.setVisible(true);

        ActionListener myListener = e -> {
            Set<Integer> blueRunningIndices = hivemindManager.getRunningBotIndices(0, "Default");
            Set<Integer> orangeRunningIndices = hivemindManager.getRunningBotIndices(1, "Default");

            String blueBotStr;
            if (blueRunningIndices.isEmpty()) {
                blueBotStr = "None";
            } else {
                blueBotStr = blueRunningIndices.stream()
                        .sorted()
                        .map(i -> "#" + i)
                        .collect(Collectors.joining(", "));
            }
            blueHive.setText("Blue hive indices: " + blueBotStr);

            String orangeBotStr;
            if (orangeRunningIndices.isEmpty()) {
                orangeBotStr = "None";
            } else {
                orangeBotStr = orangeRunningIndices.stream()
                        .sorted()
                        .map(i -> "#" + i)
                        .collect(Collectors.joining(", "));
            }
            orangeHive.setText("Orange hive indices: " + orangeBotStr);
        };

        new Timer(1000, myListener).start();
    }
}
