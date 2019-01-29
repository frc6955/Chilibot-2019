package frc.robot;


import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;
 
public class Robot extends TimedRobot {

  UsbCamera cam;
  Compressor compresor;

  RobotIO joy;
  Chasis chassis;
  Cargosystem cargoSystem;
  HPsystem hatchPanelIntake;

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
    joy = new RobotIO(0);

    //Sistemas
    chassis = new Chasis(Constantes.kChassisLeft, Constantes.kChassisRight);

    cargoSystem = new Cargosystem(3,Constantes.kVictor);
    hatchPanelIntake = new HPsystem(Constantes.kSolenoideHPIntakeIn,Constantes.kSolenoideHPIntakeOut,Constantes.kSolenoideHPReelIn,Constantes.kSolenoideHPReelOut);
    
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
    chassis.driveMode(joy.Ejes(Constantes.KAxisY_L),joy.Ejes(Constantes.KAxisX_R));
    
    //Control Cargo Intake
    if(joy.Boton(Constantes.kButtonB)){
      cargoSystem.RollerOut();
    }
    else if(joy.Boton(Constantes.kButtonA)){
      cargoSystem.RollerIn();  
    }
    else{
      cargoSystem.RollerStop();
    }

    //Control Riel
    if(joy.Boton(Constantes.kButtonX)){
      hatchPanelIntake.releaseReel();
    }
    else if(joy.Boton(Constantes.kButtonY)){
      hatchPanelIntake.contractReel();
    }
    
    //Control HP Intake
    if(joy.Boton(Constantes.kButtonLB)){
      hatchPanelIntake.openThingy();
    }
    else if(joy.Boton(Constantes.kButtonRB)){
      hatchPanelIntake.closeThingy();
    }

  }

  @Override
  public void testPeriodic() {
  }
}
