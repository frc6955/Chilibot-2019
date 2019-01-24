package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;
 
public class Robot extends TimedRobot {
  Compressor compresor;
  DifferentialDrive chassis;
  Joystick Joy;
  CameraServer cam;
  HPsystem hatchPanelIntake;
  Cargosystem CargoSystem;



  @Override
  public void robotInit() {
    cam.getInstance().startAutomaticCapture();
    Joy = new Joystick(0);
    compresor.setClosedLoopControl(true);
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
      if(Joy.getRawButton(Constantes.kButtonA)){
        CargoSystem.RollerIn();

    }
      else if(Joy.getRawButton(Constantes.kButtonB)){
        CargoSystem.RollerOut();  
    
      }
      else{
       CargoSystem.RollerStop();

      }
    if(Joy.getRawButton(Constantes.kButtonX)){
      hatchPanelIntake.releaseReel();
  
    }
      else if(Joy.getRawButton(Constantes.kButtonY)){
        hatchPanelIntake.contractReel();

      }
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
