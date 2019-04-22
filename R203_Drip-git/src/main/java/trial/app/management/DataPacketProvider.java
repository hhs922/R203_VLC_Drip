package trial.app.management;

import javax.json.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class DataPacketProvider {

    public DataPacketProvider(String ip, int port, Consumer<DataPacket> consumer) {
        this.consumer = consumer;
//        createSource(ip, port);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            initTimer();
        } else {
            cancelTimer();
        }
    }

    public boolean isEnabled() {
        return timer != null;
    }

    public void close() {
        cancelTimer();
    }

    private Timer timer;
    private Consumer<DataPacket> consumer;

    private final int timerDelay = 0;
    private final int timerPeroid = 1000;
    private long stepCount = 0;
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                ++stepCount;
                debug("step count: " + stepCount);

                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
//               Date date = new Date();
//               String strDate = sdFormat.format(date);
//                debug("step count: " + strDate);
//            PseudoData[] pseudoDatas = source.next();
                DataPacket[] dataPackets = call();
                debug("    data size: " + dataPackets.length);
                for (DataPacket dp : dataPackets) {
                    consumer.accept(dp);
                }
            } catch (Exception ex) {
                debug(ex.getMessage());
            }
        }
    };

    private void initTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(timerTask, timerDelay, timerPeroid);
            debug("timer on");
//            call();
        } else {
            debug("timer already on");
        }
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            debug("timer off");
        } else {
            debug("timer already off");
        }
    }

    DataPacket[] call() {

        String apiAddress = "http://192.168.0.106/dropsys";
        URL apiEndPoint = null;
        try {
            apiEndPoint = new URL(apiAddress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try (JsonReader reader = Json.createReader(apiEndPoint.openStream())) {
            JsonValue jv = reader.readValue();
            System.out.println(jv);
            if (jv instanceof JsonObject) {
                return dataProcess((JsonObject) jv);
            } else {
                return null;//show error message
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    DataPacket[] dataProcess(JsonObject jsonObject) {
        JsonArray source = jsonObject.getJsonArray("member");
//        ArrayList<PseudoData> pseudoDataList =  new ArrayList<>();
        ArrayList<DataPacket> dataPacketList =  new ArrayList<>();

        for (JsonValue jv : source) {
            JsonObject jo = (JsonObject) jv;

            DataPacket dataPacket = new DataPacket();

            dataPacket.tagId = Integer.parseInt(jo.getString("tagid"));
            dataPacket.activatorId = jo.getInt("lightid1");
            dataPacket.timestamp = jo.getString("time");
            dataPacket.remainderWeight = jo.getString("remainderweight");
            dataPacket.dripSpeed = jo.getString("dripspeed");
            dataPacket.remainderTime = jo.getString("remaindertime");
            dataPacket.batteryPower = jo.getString("batterypower");

            dataPacketList.add(dataPacket);
        }

        return dataPacketList.toArray(new DataPacket[0]);
 //       data.clear();
 //       data.addAll(pseudoDataList);
    }

    int roomToAid(String room) {
        String[] sections = room.split("-");
        return 100 * Integer.parseInt(sections[0]) + 1 * Integer.parseInt(sections[1]);
    }
//
//    private PseudoSource source;
//
//    private void createSource(String ip, int port) {
//        source = new PseudoSource();
//    }
//
//    private static class PseudoData {
//        public int fixingPartId;
//        public int movingPartId;
//        public String additionalData;
//    }
//
//    private static class PseudoSource {
//
//        public PseudoData[] next() {
//            pseudoDataList.clear();
//
//            case1();
//            case11();
//            case2();
//            case3();
//
//            return pseudoDataList.toArray(new PseudoData[0]);
//        }
//
//        ArrayList<PseudoData> pseudoDataList = new ArrayList<>();
//        Random rnd = new Random();
//
//        int[] case1TagIds = new int[] {101, 102, 103};
//        int[] case1StepCount = new int[] {-1, -1, -1};
//        int[] case1Trace = new int[] {15, 16, 3, 17, 3, 4, 26, -26, -26, -26, -26, -26, -25};
//        void case1() {
//            int candidate = rnd.nextInt(case1TagIds.length);
//            forwardOneStep(candidate, case1TagIds, case1StepCount, case1Trace);
//        }
//
//        int[] case11TagIds = new int[] {111};
//        int[] case11StepCount = new int[] {-1};
//        int[] case11Trace = new int[] {15, 16, 3, 17, 3, 4, 26, 4, -4, -4, -4, -3, -3};
//        void case11() {
//            int candidate = rnd.nextInt(case11TagIds.length);
//            forwardOneStep(candidate, case11TagIds, case11StepCount, case11Trace);
//        }
//
//        int[] case2TagIds = new int[] {201};
//        int[] case2StepCount = new int[] {-1};
//        //int[] case2Trace = new int[] {8, 9, 31, 11, 12, 32, 14, 15, 16, 17, 18, 19, 20, 21, 33, 23, 24, 25};
//        int[] case2Trace = new int[] {8, 9, 31, 11, 12, 32, 14, 15, 16, 17, 18, 19, 20, 21, 33, 24, 25, 23};
//        void case2() {
//            forwardOneStep(0, case2TagIds, case2StepCount, case2Trace);
//        }
//
//        int[] case3TagIds = new int[] {301};
//        int[] case3StepCount = new int[] {-1};
//        int[] case3Trace = new int[] {15, 16, -16, 16, -16, 16, 2, 32, -32, 32, -32, 32, 2, 3, 4, 26, -26, 26, 4, -4, -4, -3, -3};
//        void case3() {
//            int candidate = rnd.nextInt(case3TagIds.length);
//            forwardOneStep(candidate, case3TagIds, case3StepCount, case3Trace);
//        }
//
//        void forwardOneStep(int candidate, int[] tagIds, int[] stepCount, int[] trace) {
//            stepCount[candidate] = (++ stepCount[candidate]) % trace.length;
//            oneSample(tagIds[candidate], Math.abs(trace[stepCount[candidate]]), trace[stepCount[candidate]] > 0, 3);
//        }
//
////        void oneSample(int tagId, int activatorId, boolean statusWear, int batteryLevel) {
//        void oneSample(int iv, int room, boolean statusWear, int batteryLevel) {
////            dataBuffer[3] = (byte)((activatorId & 0xFF00) >> 8);
////            dataBuffer[4] = (byte)((activatorId & 0x00FF));
////            dataBuffer[5] = (byte)((tagId & 0xFF00) >> 8);
////            dataBuffer[6] = (byte)((tagId & 0x00FF));
////            dataBuffer[8] = (byte)((dataBuffer[8] & 0b11111110) + (statusWear ? 1 : 0));
////            dataBuffer[16] = (byte)((dataBuffer[16] & 0b00111111) + ((batteryLevel & 0b00000011) << 6));
////
////            dataBuffer[20] = (byte) computeCheckSum();
//
//            PseudoData pseudoData = new PseudoData();
////            pseudoData.movingPartId = tagId;
//            pseudoData.movingPartId = iv;
////            pseudoData.fixingPartId = activatorId;
//            pseudoData.fixingPartId = room;
//            pseudoData.additionalData = String.format("InUse:%b, Battery:%d", statusWear, batteryLevel);
//            pseudoDataList.add(pseudoData);
//        }
//    }
//
//    private DataPacket pseudoDataToDataPacket(PseudoData pseudoData) {
//        DataPacket dataPacket = new DataPacket();
//
////        dataPacket.activatorId = pseudoData.fixingPartId;
//        dataPacket.room = pseudoData.fixingPartId;
////        dataPacket.tagId = pseudoData.movingPartId;
//        dataPacket.iv = pseudoData.movingPartId;
//        dataPacket.timestamp = new Timestamp(System.currentTimeMillis());
//        dataPacket.status = pseudoData.additionalData;
//
//        return dataPacket;
//    }

    private void debug(String msg) {
        System.out.println(msg);
    }
}
