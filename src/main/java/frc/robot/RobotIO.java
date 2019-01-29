package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.AnalogGyro;


 
public class RobotIO {
 
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