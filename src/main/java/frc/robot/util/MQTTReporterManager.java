package frc.robot.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.function.Supplier;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MQTTReporterManager {

    //TODO: Place constants in Constants file
    private static long fastMQTTRefreshRate = 50;
    private static long slowMQTTRefreshRate = 250;
    private static String brokerHostUrl = "tcp://chilivision.local:1883";

    public enum MQTTTransmitRate {
        FAST,
        SLOW
    }

    Thread fastMQTTReporterThread, slowMQTTReporterThread;
    MQTTReporter fastMQTTReporter, slowMQTTReporter;
    
    private static MQTTReporterManager instance;
    private MqttClient client;

    private class MQTTReporter implements Runnable {

        private long refreshRate;
        private boolean liveThread;
        private Vector<MQTTReportable> reportablesVector;
        private MqttClient clientReference;
        private DecimalFormat d3f;

        public MQTTReporter(long refreshRate, MqttClient clientRef) {
            this.refreshRate = refreshRate;
            this.liveThread = true;
            this.reportablesVector = new Vector<MQTTReportable>();
            this.clientReference = clientRef;
            this.d3f = new DecimalFormat("#.###");
            this.d3f.setRoundingMode(RoundingMode.HALF_UP);
        }

        public synchronized void stopThread() {
            this.liveThread = false;
        }

        public synchronized void addReportable(MQTTReportable reportable) {
            this.reportablesVector.add(reportable);
        }

        @Override
        public void run() {
            while(this.liveThread) {
                for (MQTTReportable reportable : this.reportablesVector) {
                    Object retValue = reportable.getMethod().get();
                    String messageStr;
                    MqttMessage message;
                    if (retValue instanceof String) {
                        messageStr = (String) retValue;
                    } else if (retValue instanceof Integer || retValue instanceof Double) {
                        messageStr = this.d3f.format(retValue);
                    } else {
                        System.out.println("Method passed to MQTTReporter is invalid.");
                        this.liveThread = false;
                        break;
                    }
                    message = new MqttMessage(messageStr.getBytes());
                    try {
                        this.clientReference.publish(reportable.getTopic(), message);
                    } catch (MqttException me) {
                        me.printStackTrace();
                    }
                    
                }
                try {
                    Thread.sleep(this.refreshRate);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } 
            }
        }
    }

    private class MQTTReportable {
        public Supplier<Object> method;
        public String topic;

        public MQTTReportable(Supplier<Object> method, String topic) {
            this.method = method;
            this.topic = topic;
        }
        
        public Supplier<Object> getMethod() {
            return this.method;
        }

        public String getTopic() {
            return this.topic;
        }

    }

    public static MQTTReporterManager getInstance() {
        if (instance == null) {
            instance = new MQTTReporterManager(brokerHostUrl);
        }
        return instance;
    }

    private MQTTReporterManager(String hostUrl) {

        try {
            this.client = new MqttClient(hostUrl, MqttClient.generateClientId(), null);
            this.client.connect();
        } catch (MqttException me) {
            me.printStackTrace();
        }

        this.fastMQTTReporter = new MQTTReporter(fastMQTTRefreshRate, client);
        this.slowMQTTReporter = new MQTTReporter(slowMQTTRefreshRate, client);

        this.fastMQTTReporterThread = new Thread(this.fastMQTTReporter);
        this.slowMQTTReporterThread = new Thread(this.slowMQTTReporter);

        this.fastMQTTReporterThread.setDaemon(true);
        this.slowMQTTReporterThread.setDaemon(true);

        this.fastMQTTReporterThread.start();
        this.slowMQTTReporterThread.start();
        
    }

    public void addValue(Supplier<Object> method, String topic, MQTTTransmitRate refreshRate) {
        if (refreshRate == MQTTTransmitRate.FAST) {
            this.fastMQTTReporter.addReportable(new MQTTReportable(method, topic));
        } else if (refreshRate == MQTTTransmitRate.SLOW) {
            this.slowMQTTReporter.addReportable(new MQTTReportable(method, topic)
            );
        }
    }
}