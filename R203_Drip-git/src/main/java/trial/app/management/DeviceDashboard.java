package trial.app.management;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static trial.app.management.DataConfig.activatorToAnchor;
import static trial.app.management.DataConfig.layoutImageResourcePath;
import static trial.app.management.Utility.debug;

public class DeviceDashboard extends JPanel {

    public DeviceDashboard() {
        setPreferredSize(new Dimension(getLayoutImage().getWidth(), getLayoutImage().getHeight()));
    }

    BufferedImage layoutImage;

    public BufferedImage getLayoutImage() {
        if (layoutImage == null) {
            try {
                layoutImage = ImageIO.read(getClass().getResource(layoutImageResourcePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return layoutImage;
    }

    Timer timer;

    public void setAnimationEnabled(int delay) {
        if (delay > 0) {
            if (timer != null) timer.stop();
            timer = new Timer(delay, actionEvent -> { animate(); });
            timer.start();
            return;
        }

        if (delay <= 0 && (timer != null)) {
            timer.stop();
            timer = null;
            return;
        }
    }

    public boolean isAnimationEnabled() {
        return timer != null;
    }

    boolean blinkToggle = false;
    private void animate() {
        blinkToggle = !blinkToggle;
        repaint();
    }

    private DeviceDashboardModel deviceDashboardModel;

    final private DeviceDashboardModel.ModelListener listener = new DeviceDashboardModel.ModelListener() {
        @Override
        public void modelChanged() {
            if (isAnimationEnabled()) return;

            repaint();
        }
    };

    public void setModel(DeviceDashboardModel deviceDashboardModel) {
        if (this.deviceDashboardModel != null) {
            this.deviceDashboardModel.removeModelListener(listener);
        }
        this.deviceDashboardModel = deviceDashboardModel;
        if (this.deviceDashboardModel != null) {
            this.deviceDashboardModel.addModelListener(listener);
        }
    }

    public DeviceDashboardModel getModel() {
        return deviceDashboardModel;
    }

    private boolean designMode = true;

    public void setDesignMode(boolean value) {
        designMode = value;
    }

    public boolean isDesignMode() {
        return designMode;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(getLayoutImage(), 0, 0, null);

        if (isDesignMode()) return;
        if (deviceDashboardModel == null) return;

        drawAnchor((Graphics2D) g);
        drawAID((Graphics2D) g);
        drawMarkers((Graphics2D) g);
    }

    public static int NO_MORE_INFO_TIMEOUT = 3 * 1000;

    Color wheelColor = Color.BLUE;
    int wheelSize = 50;
    int wheelCapacity = 5;
//    Color markerColor = Color.GREEN;
    Color markerColor = Color.YELLOW;
    int markerSize = wheelSize * 2 / wheelCapacity;
    Point[] markerOffset = new Point[wheelCapacity];
    {
        int radius = wheelSize / 2;
        double startAngle = Math.PI;
        double deltaAngle = -Math.PI * 2 / wheelCapacity;
        for (int i = 0; i < wheelCapacity; ++i) {
            double angle = startAngle + deltaAngle * i;
            markerOffset[i] = new Point((int) (Math.sin(angle) * radius), (int) (Math.cos(angle) * radius));
        }
    }

    private void drawMarkers(Graphics2D g) {
        deviceDashboardModel.removeOldItems(1 * NO_MORE_INFO_TIMEOUT);

        if ( deviceDashboardModel.activatorWithTags == null) {
            return;
        }

        g.setStroke(new BasicStroke(2));
        g.setFont(g.getFont().deriveFont((float) markerSize));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        //Utility.debug(String.format("string \"haha\" with size = (%d, %d)", fm.stringWidth("haha"), fm.getHeight()));

        for (Map.Entry<Integer, ArrayList<Integer>> entry : deviceDashboardModel.activatorWithTags.entrySet()) {
            int activatorId = entry.getKey();
            ArrayList<Integer> tagIds = entry.getValue();
            if (tagIds.size() == 0) {
                continue;
            }
            tagIds.sort((val1, val2) -> val1 - val2);

            if (! activatorToAnchor.containsKey(activatorId)) {
                debug("unknow activatorId = " + Integer.toString(activatorId));
                debug("with tagIds = " + tagIds.toString());
                continue;
            }

            DeviceDashboardModel.Anchor anchor = activatorToAnchor.get(activatorId);

            g.setColor(wheelColor);
            g.drawOval(anchor.centerX - wheelSize / 2, anchor.centerY - wheelSize / 2, wheelSize, wheelSize);

            for (int i = 0, i_end = tagIds.size(); i < i_end; ++i) {
                int tagId = tagIds.get(i);
                Point offset = markerOffset[i % wheelCapacity];
                boolean noMoreInfo = (new Date().getTime() - deviceDashboardModel.tagLatestInfoTime.get(tagId).getTime()) > NO_MORE_INFO_TIMEOUT;
                //boolean noMoreInfo_long = (new Date().getTime() - deviceDashboardModel.tagLatestInfoTime.get(tagId).getTime()) > 10*NO_MORE_INFO_TIMEOUT;
                String tag = Integer.toString(tagId);
                int width = fm.stringWidth(tag);
                int height = fm.getHeight();
                int x = anchor.centerX + offset.x - width/2;
                int y = anchor.centerY + offset.y - height/2;

                if (isAnimationEnabled() && !noMoreInfo) {
                    if (blinkToggle) {
                        g.setColor(wheelColor);
                        g.fillRect(x, y, width, height);
                        g.setColor(markerColor);
                        g.drawString(tag, x, y + fm.getAscent());
                    }
                } else {
                    g.setColor(wheelColor);
                    g.fillRect(x, y, width, height);
                    g.setColor(markerColor.darker());
                    g.drawString(tag, x, y + fm.getAscent());
//                    if(noMoreInfo_long){
//                        g.setColor(new Color(0,0,0,0));
//                    }
                }
            }
        }
    }

    boolean aidVisible = false;

    public void setAidVisible(boolean value) {
        aidVisible = value;
        repaint();
    }

    public boolean isAidVisible() {
        return aidVisible;
    }

    Color aidColor = Color.RED;
    Color aidBackgroundColor = Color.GREEN;

    private void drawAID(Graphics2D g) {
        if (! aidVisible) return;

        g.setStroke(new BasicStroke(2));
        g.setFont(g.getFont().deriveFont((float) markerSize));
        FontMetrics fm = g.getFontMetrics(g.getFont());

        for (Map.Entry<Integer, DeviceDashboardModel.Anchor> entry : activatorToAnchor.entrySet()) {
            int aid = entry.getKey();
            DeviceDashboardModel.Anchor anchor = entry.getValue();

            String aidString = Integer.toString(aid);
            int width = fm.stringWidth(aidString);
            int height = fm.getHeight();
            int x = anchor.centerX - width/2;
            int y = anchor.centerY - height/2;

//            g.setColor(aidBackgroundColor);
//            g.fillRect(x, y, width, height);
            g.setColor(aidColor);
            g.drawString(aidString, x, y + fm.getAscent());
        }
    }

    Color anchorColor(DeviceDashboardModel.AnchorType anchorType) {
        switch (anchorType) {
            case Activator:
                return Color.YELLOW;
            case ReaderAndActivator:
                return Color.PINK;
            default:
                    return Color.BLACK;
        }
    }

    int anchorSize = wheelSize / 2 + 2;

    private void drawAnchor(Graphics2D g) {
        g.setStroke(new BasicStroke(2));

        for (DeviceDashboardModel.Anchor anchor : activatorToAnchor.values()) {
            g.setColor(anchorColor(anchor.anchorType));
            g.fillOval(anchor.centerX - anchorSize / 2, anchor.centerY - anchorSize / 2, anchorSize, anchorSize);
        }
    }
}
