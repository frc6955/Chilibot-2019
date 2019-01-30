package frc.robot;


import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;

 
public class Robot extends TimedRobot {

  UsbCamera cam;
  Compressor compresor;
  Cargosystem Cargo;
  RobotIO Joystick;
  Chasis chassis;
  HPsystem hatchPanelIntake;
  Output output;


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
    Joystick = RobotIO.getInstance();

    //Sistemas
    chassis = Chasis.getInstance();
    Cargo = Cargosystem.getInstance();
    hatchPanelIntake = HPsystem.getInstance();
    output = Output.getInstance();
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
   
    //Chassis
    chassis.driveMode(Joystick.Ejes(Constantes.KAxisY_L),Joystick.Ejes(Constantes.KAxisX_R));
    output.update(Joystick, Cargo, hatchPanelIntake,chassis);


  }

  @Override
  public void testPeriodic() {
  }
}
