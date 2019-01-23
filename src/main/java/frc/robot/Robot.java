package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
  Spark right;
  Spark left;
  Spark Intake;
  Compressor compresor;
  DoubleSolenoid solenoidhp;
  DoubleSolenoid solenoidR;
  DifferentialDrive chassis;
  Joystick Joy;
  CameraServer cam;
  

  @Override
  public void robotInit() {
    cam.getInstance().startAutomaticCapture();
    right = new Spark(0);
    left = new Spark(1);
    Intake = new Spark(2);
    chassis = new DifferentialDrive (left,right);
    compresor = new Compressor(0);
    solenoidhp = new DoubleSolenoid(0,1); 
    Joy = new Joystick(0);
    compresor.setClosedLoopControl(true);
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
    chassis.arcadeDrive(Joy.getRawAxis(1),Joy.getRawAxis(4));
    if(Joy.getRawButton(1)){
      Intake.set(0.5);
    }
      else if(Joy.getRawButton(2)){
        Intake.set(-1);
      }
      else{
        Intake.set(0);
      }
    if(Joy.getRawButton(5)){
      solenoidhp.set(Value.kForward);
    }
      else if(Joy.getRawButton(6)){
        solenoidhp.set(Value.kReverse);
      }
    if(Joy.getRawButton(3)){
      solenoidR.set(Value.kForward);
    }
      else if(Joy.getRawButton(4)){
        solenoidR.set(Value.kReverse);
      }
  }
  @Override
  public void testPeriodic() {
  }
}
