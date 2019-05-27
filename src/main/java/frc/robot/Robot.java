
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends TimedRobot {
  Joystick driver;
  Joystick operador;
  DifferentialDrive Chassis;
  Spark LeftChassis;
  Spark RightChassis;
  Compressor compressor;
  DoubleSolenoid HPintake;
  DoubleSolenoid rail;
  VictorSPX intakeMaster;
  VictorSPX rightIntake;
  TalonSRX armMaster;
  TalonSRX armFollower;
  

  

  @Override
  public void robotInit() {
    Chassis = new DifferentialDrive(LeftChassis, RightChassis);
    driver = new Joystick(0);
    operador = new Joystick(1);
    compressor = new Compressor();
    compressor.setClosedLoopControl(true);
    HPintake = new DoubleSolenoid(0,1);
    rail = new DoubleSolenoid(2,3);
    intakeMaster = new VictorSPX(9);
    rightIntake = new VictorSPX(10);
    rightIntake.set(ControlMode.Follower, 9);
    armFollower = new TalonSRX(11);
    armMaster = new TalonSRX(12);
    armFollower.set(ControlMode.Follower, 12);

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
    if(driver.getRawButton(2)){
      Chassis.arcadeDrive(driver.getRawAxis(1), driver.getRawAxis(4));
    }
    else{
      Chassis.arcadeDrive((driver.getRawAxis(1))/2, (driver.getRawAxis(4))/2);
    }
    //intake in
    if(driver.getRawButton(5)){
        intakeMaster.set(ControlMode.PercentOutput, -0.6);
    }
    //intake out
    else if(driver.getRawButton(6)){
      intakeMaster.set(ControlMode.PercentOutput, 0.4); 
    }
    //intake Stop
    else{
      intakeMaster.set(ControlMode.PercentOutput, 0); 
    }
    //arm Up
    if(driver.getRawButton(4)){
      armMaster.set(ControlMode.PercentOutput, 0.25);
    }
    //arm Down
    else if(driver.getRawButton(1)){
      armMaster.set(ControlMode.PercentOutput, -0.25);
    }
    //arm Stop
    else{
      armMaster.set(ControlMode.PercentOutput, 0);
    }
    //rail  
    if(operador.getRawButton(1)){
      rail.set(Value.kForward);
    }
    else if(operador.getRawButton(4)){
      rail.set(Value.kReverse);
    }
    //HPintake
    if(operador.getRawButton(5)){
      HPintake.set(Value.kForward);
    }
    else if(operador.getRawButton(6)){
      HPintake.set(Value.kReverse);
    }
  }


  @Override
  public void testPeriodic() {
  }
}
