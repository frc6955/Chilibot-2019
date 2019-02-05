package frc.robot;

import frc.robot.subsystem.Cargosystem;
import frc.robot.subsystem.Chasis;
import frc.robot.subsystem.HPsystem;

public class Output {

    private static Output instance;

    public static Output getInstance() {
        if (instance == null) {
            instance = new Output();
        }
        return instance;
    }
    public void update(RobotIO Joystick,Cargosystem Cargo,HPsystem hatchPanelIntake,Chasis chassis) {
        
      }
}








