package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Encoder;


public class RobotIO {
 
    private Joystick Joy;
    private Joystick Operator;
    private static RobotIO instance;
    public static RobotIO getInstance() {
        if (instance == null) {
            instance = new RobotIO(0, 1);
        }
        return instance;
    }

    private RobotIO (int puertojoystick, int port) {
        Joy = new Joystick(puertojoystick);
        Operator = new Joystick(port);
    }

    public boolean Boton(int button, int joystick){
        if (joystick==0){
          return Joy.getRawButton(button);  
        }else {
          return Operator.getRawButton(button);
        }
    }

    public double Ejes(int axis, int joystick){
        
        if (joystick==0){
            return Joy.getRawAxis(axis);
          }else {
            return Operator.getRawAxis(axis);
          }
    }

}