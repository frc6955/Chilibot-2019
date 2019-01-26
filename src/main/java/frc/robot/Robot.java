package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
 
public class Robot extends TimedRobot {
  Compressor compresor;
  DifferentialDrive chassis;
  Joystick Joy;
  Spark left;
  Spark right;
  UsbCamera cam;
  HPsystem hatchPanelIntake;
  Cargosystem CargoSystem;



  @Override
  public void robotInit() {
    //Camara y parametros
    cam = CameraServer.getInstance().startAutomaticCapture();
    cam.setFPS(Constantes.kFPS);
    cam.setResolution(Constantes.kWidth, Constantes.kHeight);
    
    //Compresor
    compresor = new Compressor();
    compresor.setClosedLoopControl(true);

    //Elementos
    Joy = new Joystick(0);
    left = new Spark(0);
    right= new Spark (1);

    //Sistemas
    chassis = new DifferentialDrive(left, right);
    hatchPanelIntake = new HPsystem(Constantes.kSolenoideHPIntakeIn,Constantes.kSolenoideHPIntakeOut,Constantes.kSolenoideHPReelIn,Constantes.kSolenoideHPReelOut);
    CargoSystem = new Cargosystem(Constantes.kMotorIntake,Constantes.kTalonSRX,Constantes.kVictorSPX);

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

    chassis.arcadeDrive(Joy.getRawAxis(Constantes.KAxisY_L),Joy.getRawAxis(Constantes.KAxisX_R));
    
    //Control Cargo Intake
    if(Joy.getRawButton(Constantes.kButtonA)){
      CargoSystem.RollerIn();
    }
    else if(Joy.getRawButton(Constantes.kButtonB)){
      CargoSystem.RollerOut();  
    }
    else{
      CargoSystem.RollerStop();
    }

    //Control Riel
    if(Joy.getRawButton(Constantes.kButtonX)){
      hatchPanelIntake.releaseReel();
    }
      else if(Joy.getRawButton(Constantes.kButtonY)){
        hatchPanelIntake.contractReel();
    }
    
    //Control HP Intake
    if(Joy.getRawButton(Constantes.kButtonLB)){
      hatchPanelIntake.openThingy();
    }
    else if(Joy.getRawButton(Constantes.kButtonRB)){
        hatchPanelIntake.closeThingy();
    }

  }

  @Override
  public void testPeriodic() {
  }
}
