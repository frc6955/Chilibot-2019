package cl.loschilis.util;

import cl.loschilis.Constantes;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.function.Supplier;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MQTTReporterManager {

    public enum MQTTTransmitRate {
        FAST,
        SLOW
    }

    private Thread fastMQTTReporterThread, slowMQTTReporterThread;
    private MQTTReporter fastMQTTReporter, slowMQTTReporter;
    
    private static MQTTReporterManager instance;
    private MqttClient client;

    private class MQTTReporter implements Runnable {

        private long refreshRate;
        private boolean liveThread;
        private Vector<MQTTReportable> reportablesVector;
        private MqttClient clientReference;
        private DecimalFormat d3f;

        MQTTReporter(long refreshRate, MqttClient clientRef) {
            this.refreshRate = refreshRate;
            this.liveThread = true;
            this.reportablesVector = new Vector<MQTTReportable>();
            this.clientReference = clientRef;
            this.d3f = new DecimalFormat("#.###");
            this.d3f.setRoundingMode(RoundingMode.HALF_UP);
        }

        void stopThread() {
            this.liveThread = false;
        }

        void addReportable(MQTTReportable reportable) {
            this.reportablesVector.add(reportable);
        }

        @Override
        public void run() {
            while(this.liveThread) {
                for (MQTTReportable reportable : new Vector<MQTTReportable>(this.reportablesVector)) {
                    try {
                        Object retValue = reportable.getMethod().get();
                        String messageStr;
                        if (retValue instanceof String) {
                            messageStr = (String) retValue;
                        } else if (retValue instanceof Integer || retValue instanceof Double) {
                            messageStr = this.d3f.format(retValue);
                        } else if (retValue instanceof Boolean) {
                            messageStr = (Boolean) retValue ? "true" : "false";
                        } else {
                            System.out.println("Method passed to MQTTReporter is invalid.");
                            this.stopThread();
                            break;
                        }
                        MqttMessage message = new MqttMessage(messageStr.getBytes());
                        try {
                            this.clientReference.publish(reportable.getTopic(), message);
                        } catch (MqttException me) {
                            me.printStackTrace();
                            System.out.println("Failed connection. Exiting thread.");
                            this.stopThread();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.print("Failed running Reportable on topic: ");
                        System.out.print(reportable);
                        System.out.println(". Dropping Reportable.");
                        this.reportablesVector.remove(reportable);
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
        Supplier<Object> method;
        String topic;

        MQTTReportable(Supplier<Object> method, String topic) {
            this.method = method;
            this.topic = topic;
        }
        
        Supplier<Object> getMethod() {
            return this.method;
        }

        String getTopic() {
            return this.topic;
        }

        public String toString() {
            return this.getTopic();
        }
    }

    public static MQTTReporterManager getInstance() {
        if (instance == null) {
            instance = new MQTTReporterManager(Constantes.brokerHostUrl);
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

        this.fastMQTTReporter = new MQTTReporter(Constantes.fastMQTTRefreshRate, client);
        this.slowMQTTReporter = new MQTTReporter(Constantes.slowMQTTRefreshRate, client);

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
            this.slowMQTTReporter.addReportable(new MQTTReportable(method, topic));
        }
    }
}