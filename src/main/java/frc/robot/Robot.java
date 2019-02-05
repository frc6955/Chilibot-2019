package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystem.Chasis;
import frc.robot.subsystem.HPsystem;
import frc.robot.subsystem.Cargosystem;

 
public class Robot extends TimedRobot {

  RobotIO entradas;

  Output output;
  Chasis chassis;
  Cargosystem Cargo;
  HPsystem hatchPanelIntake;

  Scheduler scheduler;


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
