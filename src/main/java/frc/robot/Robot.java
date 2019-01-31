package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystem.Cargosystem;
import frc.robot.subsystem.Chasis;
import frc.robot.subsystem.HPsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;

 
public class Robot extends TimedRobot {

  UsbCamera cam;
  Compressor compresor;
  Cargosystem Cargo;
  RobotIO entradas;
  Chasis chassis;
  HPsystem hatchPanelIntake;
  Output output;
  Scheduler scheduler;


  @Override
  public void robotInit() {

    //Camara y parametros
    cam = CameraServer.getInstance().startAutomaticCapture();
    cam.setFPS(Constantes.kFPS);
    cam.setResolution(Constantes.kWidth, Constantes.kHeight);
    
    //Compresor
    compresor = new Compressor();
    compresor.setClosedLoopControl(true);

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
    //chassis.update(entradas);
    //hatchPanelIntake.update(entradas);
    //Cargo.update(entradas);

  }

  @Override
  public void testPeriodic() {
  }
}
