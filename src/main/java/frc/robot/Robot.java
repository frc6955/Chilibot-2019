package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystem.Chasis;
import frc.robot.subsystem.HPsystem;
import frc.robot.subsystem.Cargosystem;
import frc.robot.util.MQTTReporterManager;
import frc.robot.util.MQTTReporterManager.MQTTTransmitRate;

 
public class Robot extends TimedRobot {

  RobotIO entradas;

  Output output;
  Chasis chassis;
  Cargosystem Cargo;
  HPsystem hatchPanelIntake;

  Scheduler scheduler;


  DriverStation dsinfo;
  MQTTReporterManager mqttLogger;

  
  @Override
  public void robotInit() {
 
    //Inputs
    entradas = RobotIO.getInstance();

    //Sistemas
    chassis = Chasis.getInstance();
    Cargo = Cargosystem.getInstance();
    hatchPanelIntake = HPsystem.getInstance();

    
    scheduler = Scheduler.getInstance();
    scheduler.addSubsystem(chassis);
    scheduler.addSubsystem(hatchPanelIntake);
    scheduler.addSubsystem(Cargo);

    // CameraServer.getInstance().startAutomaticCapture();
    dsinfo = DriverStation.getInstance();
    mqttLogger = MQTTReporterManager.getInstance();
    mqttLogger.addValue(()->(RobotController.getBatteryVoltage()), "webui/battery/voltage", MQTTTransmitRate.SLOW);
    mqttLogger.addValue(()->(dsinfo.getMatchTime()), "webui/driverstation/matchtime", MQTTTransmitRate.SLOW);
    mqttLogger.addValue(()->(entradas.getGyroAngle()), "webui/sensors/gyro", MQTTTransmitRate.FAST);
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
    scheduler.update(entradas); 
  }

  @Override
  public void testPeriodic() {
  }
}
