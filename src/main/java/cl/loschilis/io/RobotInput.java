package cl.loschilis.io;

import cl.loschilis.Constantes;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;


public class RobotInput {

    private static RobotInput instance;
    private Joystick driver;
    private Joystick operator;
    private PowerDistributionPanel PDP;
    private UsbCamera frontCam, backCam;
    private NetworkTableInstance inst; 
    private NetworkTable table;

    public static RobotInput getInstance() {
        if (instance == null) {
            instance = new RobotInput();
        }
        return instance;
    }

    private RobotInput() {
        driver = new Joystick(Constantes.kJoystickUSBDriver);
        operator = new Joystick(Constantes.kJoystickUSBOperator);
        PDP = new PowerDistributionPanel();
        
        frontCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kFront);
        backCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kBack);

        frontCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);
        backCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);

        frontCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        backCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("vision");
        table.getKeys();

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

    public double getCorrienteCanal(int canal){
        return PDP.getCurrent(canal);
        
    }

    public UsbCamera Cam(int cam){
        if(cam == Constantes.kFront){
            return frontCam;
        } else if(cam == Constantes.kBack){
            return backCam;
        } else{
            return null;
        }
    }
    public double getVisionError(){
        return table.getEntry("error").getDouble(-1.0);
    }
}