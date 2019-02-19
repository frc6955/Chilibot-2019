package cl.loschilis.io;

import cl.loschilis.Constantes;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.json.JSONObject;

import edu.wpi.cscore.MjpegServer;
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
    private PowerDistributionPanel powerDistPanel;
    private UsbCamera frontCam, backCam;
    private MjpegServer serverLarge, serverSmall;
    private NetworkTableInstance networkTableInstance; 
    private NetworkTable visionNTTable;
    private AnalogInput ultraSonicAnalog;
    private ADXRS450_Gyro gyro;
    private TalonSRX armMasterController;

    public static RobotInput getInstance() {
        if (instance == null) {
            instance = new RobotInput();
        }
        return instance;
    }

    private RobotInput() {
        driver = new Joystick(Constantes.kJoystickUSBDriver);
        operator = new Joystick(Constantes.kJoystickUSBOperator);
        powerDistPanel = new PowerDistributionPanel(Constantes.kPDPCANID);
        ultraSonicAnalog = new AnalogInput(Constantes.kAnalogMaxbotixBallDetectorPin);
        gyro = new ADXRS450_Gyro();
        armMasterController = RobotOutput.getArmMotorReference();

        frontCam = new UsbCamera("frontCam", Constantes.kCameraIndexFront);
        frontCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kCameraLargeWidth, Constantes.kCameraLargeHeight, Constantes.kCameraFPS);
        backCam = new UsbCamera("backCam", Constantes.kCameraIndexBack);
        backCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kCameraSmallWidth, Constantes.kCameraSmallHeight, Constantes.kCameraFPS);
        serverLarge = CameraServer.getInstance().addServer("LargeServer", 1181);
        serverSmall = CameraServer.getInstance().addServer("SmallServer", 1182);
        serverLarge.setSource(frontCam);
        serverSmall.setSource(backCam);

        networkTableInstance = NetworkTableInstance.getDefault();
        visionNTTable = networkTableInstance.getTable(Constantes.kVisionNTTable);
    }

    public double getPrimaryJoyAxis(int axis) {
        return driver.getRawAxis(axis);
    }

    public double getSecondaryJoyAxis(int axis) {
        return operator.getRawAxis(axis);
    }

    public boolean getPrimaryJoyButton(int button) {
        return driver.getRawButton(button);  
    }
    
    public boolean getSecondaryJoyButton(int button) {
        return operator.getRawButton(button);
    }

    public boolean getSecondaryJoyButtonOnPress(int button) {
        return operator.getRawButtonPressed(button);
    }

    public double getPrimaryJoyPOVAngle() {
        return driver.getPOV(0);
    }

    public double getChannelCurrent(int canal) {
        return powerDistPanel.getCurrent(canal);
    }

    public double getUltrasonicSensorCm() {
        return ultraSonicAnalog.getAverageVoltage() / Constantes.kAnalogMaxbotixVoltsPerCentimeter;
    }

    public boolean getUltrasonicAdquisition() {
        return this.getUltrasonicSensorCm() < Constantes.kAnalogMaxbotixMinimumThreshold;
    }

    public double armCurrent() {
        return this.getChannelCurrent(Constantes.kPDPChannelArmMaster) + 
        this.getChannelCurrent(Constantes.kPDPChannelArmSlave);
    }

    public double chassisCurrent() {
        return this.getChannelCurrent(Constantes.kPDPChannelChassisRightA) + 
        this.getChannelCurrent(Constantes.kPDPChannelChassisRightB) + 
        this.getChannelCurrent(Constantes.kPDPChannelChassisLeftA) + 
        this.getChannelCurrent(Constantes.kPDPChannelChassisLeftB);
    }

    public double intakeCurrent() {
        return this.getChannelCurrent(Constantes.kPDPChannelIntakeMaster) + 
        this.getChannelCurrent(Constantes.kPDPChannelIntakeSlave);
    }

    public String getAllCurrents() {
        JSONObject jsonData = new JSONObject();
        jsonData.put("arm", this.armCurrent());
        jsonData.put("chassis", this.chassisCurrent());
        jsonData.put("intake", this.intakeCurrent());
        return jsonData.toString(4);
    }

    public double getGyroAngle() {
        return gyro.getAngle();
    }

    // TODO: Implement adequate transformation for angle here
    public double getArmAngle() {
        return -1 * (90.0 / 600.0) * armMasterController.getSelectedSensorPosition(Constantes.kTalonConfigPIDLoopIdx);
    }

    public void switchCameras() {
        VideoSource tempCamLarge = serverLarge.getSource();
        VideoSource tempCamSmall = serverSmall.getSource();

        tempCamLarge.setResolution(Constantes.kCameraSmallWidth, Constantes.kCameraSmallHeight);
        tempCamSmall.setResolution(Constantes.kCameraLargeWidth, Constantes.kCameraLargeHeight);

        serverLarge.setSource(tempCamSmall);
        serverSmall.setSource(tempCamLarge);
    }

    public UsbCamera getUsbCamera(int cam) {
        if(cam == Constantes.kCameraIndexFront) {
            return frontCam;
        } else if(cam == Constantes.kCameraIndexBack) {
            return backCam;
        } else {
            throw new IllegalArgumentException("Incorrect camera index!");
        }
    }

    public double getVisionError() {
        return visionNTTable.getEntry("error").getDouble(Constantes.kVisionNoLockState);
    }
}
