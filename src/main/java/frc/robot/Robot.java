package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
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
  DriverStation dsinfo;
  
  @Override
  public void robotInit() {
    CameraServer.getInstance().startAutomaticCapture();
    d3f = new DecimalFormat("#.###");
    d3f.setRoundingMode(RoundingMode.HALF_UP);
    try {
      client = new MqttClient("tcp://10.69.55.20:1883", "fcef399c421740af845b8153e476533a", null);
      client.connect();
    } catch (MqttException e) {
      e.printStackTrace();
    }
    dsinfo = DriverStation.getInstance();
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

    MqttMessage mqttBatteryStateMessage = new MqttMessage();
    MqttMessage mqttTimerMessage = new MqttMessage();

    double voltage = RobotController.getBatteryVoltage();
    double remainingMatchTime = dsinfo.getMatchTime();

    String strBatteryStateMessage = d3f.format(voltage);
    String strTimerMessage = d3f.format(remainingMatchTime);

    mqttBatteryStateMessage.setPayload(strBatteryStateMessage.getBytes());
    mqttTimerMessage.setPayload(strTimerMessage.getBytes());

    try {
      client.publish("webui/battery/voltage", mqttBatteryStateMessage);
      client.publish("webui/matchtime", mqttTimerMessage);
    } catch (MqttException e) {
      e.printStackTrace();
	  }

  }

  @Override
  public void testPeriodic() {
  }
}
