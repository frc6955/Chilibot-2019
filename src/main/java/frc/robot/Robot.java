package frc.robot;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {

  MqttClient client;
  
  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
    try {
      client = new MqttClient("tcp://chilivision.local:1883", MqttClient.generateClientId());
      client.connect();
      // client.subscribe("webui/battery");
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {
    double voltage = RobotController.getBatteryVoltage();
    System.out.println(voltage);
    MqttMessage message = new MqttMessage();
    String svoltage = Double.toString(voltage);
    message.setPayload(svoltage.getBytes());
    try {
      client.publish("webui/battery/voltage", message);
    } catch (MqttPersistenceException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (MqttException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
	  }

  }

  @Override
  public void testPeriodic() {
  }
}
