package cl.loschilis.io;

import cl.loschilis.Constantes;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.json.JSONObject;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;


public class RobotInput {

    private static RobotInput instance;
    private Joystick driver;
    private Joystick operator;
    private PowerDistributionPanel powerDistPanel;
    private NetworkTableInstance networkTableInstance; 
    private NetworkTable visionNTTable;
    private AnalogInput ultraSonicAnalog;
    private ADXRS450_Gyro gyro;
    private TalonSRX armMasterController;
    private DigitalInput bumperSwitch;

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
        networkTableInstance = NetworkTableInstance.getDefault();
        visionNTTable = networkTableInstance.getTable(Constantes.kVisionNTTable);
        bumperSwitch = new DigitalInput(Constantes.kDigitalBumperSwitchPin);
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

    public double getSecondaryJoyPOVAngle() {
        return operator.getPOV(0);
    }

    public double getSecondaryJoyTriggerAxis() {
        return (operator.getRawAxis(2) - operator.getRawAxis(3));
    }

    public double getChannelCurrent(int canal) {
        return powerDistPanel.getCurrent(canal);
    }

    public double getUltrasonicSensorCm() {
        return ultraSonicAnalog.getAverageVoltage() / Constantes.kAnalogMaxbotixVoltsPerCentimeter;
    }

    public boolean getBumperSwitchState() {
        return !this.bumperSwitch.get();
    }

    public boolean getBallAdquisition() {
        // return this.getUltrasonicSensorCm() < Constantes.kAnalogMaxbotixMinimumThreshold;
        return this.getBumperSwitchState();
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

    public double getVisionError() {
        return visionNTTable.getEntry(Constantes.kNTEntryVisionError).getDouble(Constantes.kVisionNoLockState);
    }

	public void switchCameras(boolean flipCams) {
        visionNTTable.getEntry(Constantes.kNTEntryFlippedCams).setBoolean(flipCams);
	}
}
