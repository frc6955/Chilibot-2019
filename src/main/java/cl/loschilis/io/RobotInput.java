package cl.loschilis.io;

import cl.loschilis.Constantes;

import com.ctre.phoenix.motorcontrol.SensorCollection;

import org.json.JSONObject;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;


public class RobotInput {

    private static RobotInput instance;
    private RobotOutput salidas;
    private Joystick driver;
    private Joystick operator;
    private PowerDistributionPanel PDP;
    private UsbCamera frontCam, backCam;
    private NetworkTableInstance inst; 
    private NetworkTable table;
    private AnalogInput ultraSonicAnalog;
    private ADXRS450_Gyro gyro;
    private SensorCollection armSensors;

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
        ultraSonicAnalog = new AnalogInput(Constantes.kAnalogMaxbotixBallDetector);
        gyro = new ADXRS450_Gyro();
        salidas = RobotOutput.getInstance();
        armSensors = salidas.getArmSensors();
        
        frontCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kFront);
        // backCam = CameraServer.getInstance().startAutomaticCapture(Constantes.kBack);

        frontCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);
        // backCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);

        frontCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        // backCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable(Constantes.kVisionNTTable);
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

    public double driverPOV() {
        return driver.getPOV(0);
    }

    public double getChannelCurrent(int canal){
        return PDP.getCurrent(canal);
        
    }

    public double analogInputCount(){
        double dist = (ultraSonicAnalog.getVoltage() / 5) * 1023 / 2;
        return dist;
    }

    public double armCurrent () {
        return this.getChannelCurrent(Constantes.kPDPChannelArmMaster) + this.getChannelCurrent(Constantes.kPDPChannelArmSlave);
    }

    public double chassisCurrent(){
        return this.getChannelCurrent(Constantes.kPDPChannelChassisRightA) + 
        this.getChannelCurrent(Constantes.kPDPChannelChassisRightB) + 
        this.getChannelCurrent(Constantes.kPDPChannelChassisLeftA) + 
        this.getChannelCurrent(Constantes.kPDPChannelChassisLeftB);
    }

    public double intakeCurrent(){
        return this.getChannelCurrent(Constantes.kPDPChannelIntakeMaster) + this.getChannelCurrent(Constantes.kPDPChannelIntakeSlave);
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

    public double getArmAngle() {
        return -1 * (90/600) * salidas.armMaster.getSelectedSensorPosition(Constantes.kTalonConfigPIDLoopIdx);
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
        return table.getEntry("error").getDouble(-250.0);
    }
}