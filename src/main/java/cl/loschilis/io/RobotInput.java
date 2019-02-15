package cl.loschilis.io;

import cl.loschilis.Constantes;
import org.json.JSONObject;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;


public class RobotInput {

    private static RobotInput instance;
    private Joystick driver;
    private Joystick operator;
    private PowerDistributionPanel PDP;
    private UsbCamera frontCam, backCam;
    private NetworkTableInstance inst; 
    private NetworkTable table;
    private AnalogInput analogInput;
    private ADXRS450_Gyro gyro;

    public static RobotInput getInstance() {
        if (instance == null) {
            instance = new RobotInput();
        }
        return instance;
    }

    private RobotInput() {
        driver = new Joystick(Constantes.kJoystickUSBDriver);
        operator = new Joystick(Constantes.kJoystickUSBOperator);
        PDP = new PowerDistributionPanel(Constantes.kPDPCANID);
        analogInput = new AnalogInput(0);
        gyro = new ADXRS450_Gyro();
        
        frontCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kFront);
        backCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kBack);

        frontCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);
        backCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);

        frontCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        backCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable(Constantes.kVisionNTTable);
        System.out.println(analogInputCount());
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

    public double analogInputCount(){
        double dist = (analogInput.getVoltage() / 5) * 1023 / 2;
        return dist;
    }

    public double armCurrent () {
        return this.getCorrienteCanal(Constantes.kPDPChannelArmMaster) + this.getCorrienteCanal(Constantes.kPDPChannelArmSlave);
    }

    public double chassisCurrent(){
        return this.getCorrienteCanal(Constantes.kPDPChannelChassisRightA) + 
        this.getCorrienteCanal(Constantes.kPDPChannelChassisRightB) + 
        this.getCorrienteCanal(Constantes.kPDPChannelChassisLeftA) + 
        this.getCorrienteCanal(Constantes.kPDPChannelChassisLeftB);
    }

    public double intakeCurrent(){
        return this.getCorrienteCanal(Constantes.kPDPChannelIntakeMaster) + this.getCorrienteCanal(Constantes.kPDPChannelIntakeSlave);
    }

    public String getAllCurrents(){

        JSONObject jsonData = new JSONObject();
        jsonData.put("arm", this.armCurrent());
        jsonData.put("chassis", this.chassisCurrent());
        jsonData.put("intake", this.intakeCurrent());
        return jsonData.toString(4);
    }

    public double getGyroAngle(){
        return gyro.getAngle();
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