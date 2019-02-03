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

import frc.robot.util.MQTTReporterManager;
import frc.robot.util.MQTTReporterManager.MQTTTransmitRate;

public class Robot extends TimedRobot {

  DriverStation dsinfo;
  MQTTReporterManager mqttLogger;
  
  @Override
  public void robotInit() {
    // CameraServer.getInstance().startAutomaticCapture();
    dsinfo = DriverStation.getInstance();
    mqttLogger = MQTTReporterManager.getInstance();
    mqttLogger.addValue(RobotController::getBatteryVoltage, "webui/battery/voltage", MQTTTransmitRate.SLOW);
    mqttLogger.addValue(dsinfo::getMatchTime, "webui/driverstation/matchtime", MQTTTransmitRate.SLOW);
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

  }

  @Override
  public void testPeriodic() {
  }
}
