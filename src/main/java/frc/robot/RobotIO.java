package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

//import edu.wpi.first.wpilibj.Encoder;


public class RobotIO {
 
    private Joystick driver;
    private Joystick operator;
    private PowerDistributionPanel PDP;
    private UsbCamera frontCam, backCam;
    private static RobotIO instance;
    public static RobotIO getInstance() {
        if (instance == null) {
            instance = new RobotIO(Constantes.kDriver, Constantes.kOperator);
        }
        return instance;
    }

    private RobotIO (int joystickDriver, int joystickOperator) {
        driver = new Joystick(joystickDriver);
        operator = new Joystick(joystickOperator);
        PDP = new PowerDistributionPanel();
        
        frontCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kFront);
        backCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kBack);
        frontCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);
        backCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);
        frontCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        backCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
    }


    public boolean driverButton(int button){
        return driver.getRawButton(button);  
        
    }
    
    public boolean operatorButton(int button){
        return operator.getRawButton(button);
    
    }

    public double driverAxis(int axis){
        return driver.getRawAxis(axis);
        
    }

    public double operatorAxis(int axis){
        return operator.getRawAxis(axis);
        
    }

    public double Corriente(int canal){
        return PDP.getCurrent(canal);
    }

    public UsbCamera Cam(int cam){
        if(cam == Constantes.kFront){
            return frontCam;
        }
        else if(cam == Constantes.kBack){
            return backCam;
        }
        else{
            return null;
        }
    }

}