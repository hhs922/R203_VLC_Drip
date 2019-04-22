package trial.app.management;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends Application {

    public static void main(String argv[]) {
        String[] defaultArgv = new String[] {
//                "127.0.0.1", "7000"
                "10.9.103.240", "7000"
        };
        if (argv.length == 0) {
            argv = defaultArgv;
        }

        mainForm = new MainForm();

        dataPacketProvider = new DataPacketProvider(argv[0], Integer.parseInt(argv[1]), Main::receive);
        dataPacketProvider.setEnabled(true);

        if (argv.length > 2 && argv[2].toLowerCase().equals("javafx")) {
            showInFx();
        } else {
            showInSwing();
        }
    }

    static MainForm mainForm;
    static DataPacketProvider dataPacketProvider;

    static void showInSwing() {
        JFrame frame = new JFrame("Locating Dashboard (Swing)");
        frame.setContentPane(mainForm.getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dataPacketProvider.close();
            }
        });
    }

    static void showInFx() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        final SwingNode swingNode = new SwingNode();

        SwingUtilities.invokeLater(()-> swingNode.setContent(mainForm.getRootPanel()));

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        stage.setTitle("Locating Dashboard (JavaFX)");
        stage.setScene(new Scene(pane, 600, 800));
        //stage.setScene(new Scene(pane));
        //stage.sizeToScene();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        dataPacketProvider.close();
        System.exit(0);
    }

    static void receive(DataPacket dataPacket) {
        mainForm.getDeviceDashboardModel().receive(dataPacket);
        mainForm.getDeviceTableModel().receive(dataPacket);
    }
}
