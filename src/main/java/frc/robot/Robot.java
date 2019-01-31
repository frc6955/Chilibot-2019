package frc.robot;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {

  MqttClient client;
  DecimalFormat d3f;
  
  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
    d3f = new DecimalFormat("#.###");
    d3f.setRoundingMode(RoundingMode.HALF_UP);
    try {
      client = new MqttClient("tcp://10.69.55.20:1883", "fcef399c421740af845b8153e476533a", null);
      client.connect();
      client.subscribe("battery");
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
    MqttMessage message = new MqttMessage();
    String svoltage = d3f.format(voltage);
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
