package trial.app.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DeviceDashboardModel {

    public enum AnchorType {
        Activator,
        ReaderAndActivator
    }

    public static class Anchor {
        public Anchor(AnchorType anchorType, int centerX, int centerY) {
            this.anchorType = anchorType;
            this.centerX = centerX;
            this.centerY = centerY;
        }

        final public AnchorType anchorType;
        final public int centerX;
        final public int centerY;
    }

    HashMap<Integer, Integer> tagAtActivator = new HashMap<>();
    HashMap<Integer, Date> tagLatestInfoTime = new HashMap<>();

    HashMap<Integer, ArrayList<Integer>> activatorWithTags = null;

    public void receive(DataPacket dataPacket) {
        int tagId = dataPacket.tagId;
        int activatorId = dataPacket.activatorId;

        tagLatestInfoTime.put(tagId, new Date());

        if ((! tagAtActivator.containsKey(tagId)) || (tagAtActivator.get(tagId) != activatorId)) {
            tagAtActivator.put(tagId, activatorId);
            refreshActivatorWithTags();
        }

        fireModelChanged();
    }

    void refreshActivatorWithTags() {
        if (tagAtActivator.size() == 0) {
            activatorWithTags = null;
            return;
        }

        activatorWithTags = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : tagAtActivator.entrySet()) {
            int tagId = entry.getKey();
            if (tagId == 0xffff) continue; // activator info, not from tag.

            int activatorId = entry.getValue();

            if (! activatorWithTags.containsKey(activatorId)) {
                activatorWithTags.put(activatorId, new ArrayList<>());
            }

            activatorWithTags.get(activatorId).add(tagId);
        }
    }

    void removeOldItems(int keepDuration) {
        ArrayList<Integer> oldOnes = new ArrayList<>();
        long now = new Date().getTime();

        for (Map.Entry<Integer, Date> pair : tagLatestInfoTime.entrySet()) {
            if ((now - pair.getValue().getTime()) > keepDuration) oldOnes.add(pair.getKey());
        }

        if (oldOnes.size() > 0) {
            for (int tagId : oldOnes) {
                tagLatestInfoTime.remove(tagId);
                tagAtActivator.remove(tagId);
            }
            refreshActivatorWithTags();
            fireModelChanged();
        }
    }

    private ArrayList<ModelListener> listeners = new ArrayList<>();

    public void addModelListener(ModelListener listener) {
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }

    public void removeModelListener(ModelListener listener) {
        listeners.remove(listener);
    }

    protected void fireModelChanged() {
        for (ModelListener listener : listeners) listener.modelChanged();
    }

    public interface ModelListener {
        void modelChanged();
    }
}
