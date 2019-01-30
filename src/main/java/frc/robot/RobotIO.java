package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.AnalogGyro;
//import edu.wpi.first.wpilibj.PowerDistributionPanel;

 
public class RobotIO {
    private static RobotIO instance;
    
    public static RobotIO getInstance() {
        if (instance == null) {
            instance = new RobotIO(0);
        }
        return instance;
    }

Joystick Joy;


public RobotIO (int puertojoystick) {
Joy = new Joystick(puertojoystick);
}

public boolean Boton(int button){
 return Joy.getRawButton(button);
}

public double Ejes(int axis){
 return Joy.getRawAxis(axis);
}




}