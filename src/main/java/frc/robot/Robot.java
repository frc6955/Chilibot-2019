package frc.robot;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {

  MqttClient client;
  
  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
    try {
      client = new MqttClient("tcp://10.69.55.20:1883", MqttClient.generateClientId());
      client.connect();
      // client.subscribe("battery");
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
      client.publish("battery", message);
    } catch (MqttException e) {
      e.printStackTrace();
	  }

  }

  @Override
  public void testPeriodic() {
  }
}
