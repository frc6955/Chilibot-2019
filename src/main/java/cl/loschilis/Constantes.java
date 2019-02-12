package cl.loschilis;
 
public class Constantes {
    // Compresor
    public static final int kCompressorCANID = 1;
    // Solenoides
    public static final int kSolenoidChannelFingerClose = 0;
    public static final int kSolenoidChannelFingerOpen = 1;
    public static final int kSolenoidChannelIntakeRetract = 2;
    public static final int kSolenoidChannelIntakeExtend = 3 ;
    // Motores
    public static final int kMotorPWMChassisLeft = 0;
    public static final int kMotorPWMChassisRight = 1;
    public static final int kMotorCANArmMasterID = 12;
    public static final int kMotorCANArmSlaveID = 11;
    public static final int kMotorCANIntakeLeftID = 10;
    public static final int kMotorCANIntakeRightID = 9;
    // Motor directions
    public static final boolean kMotorDirectionArmMaster = false;
    public static final boolean kMotorDirectionChassisLeft = false;
    public static final boolean kMotorDirectionChassisRight = false;
    public static final boolean kMotorDirectionIntakeLeft = false;
    // Joysticks
    public static final int kJoystickUSBDriver = 0;
    public static final int kJoystickUSBOperator = 1;
    //Buttons
    public static final int kJoystickButtonA = 1;
    public static final int kJoystickButtonB = 2;
    public static final int kJoystickButtonX = 3;
    public static final int kJoystickButtonY = 4;
    public static final int kJoystickButtonLB = 5;
    public static final int kJoystickButtonRB = 6;
    //Axis
    public static final int kJoystickAxisLeftY = 1;
    public static final int kJoystickAxisLeftX = 2;
    public static final int kJoystickAxisRightY = 3;
    public static final int kJoystickAxisRightX = 4;
    // Camara
    public static final int kFront = 0;
    public static final int kBack = 1;
    public static final int kFPS = 15;
    public static final int kWidth = 320;
    public static final int kHeight = 240;
    // Cargo
    public static final double kCargoIn = -0.6;
    public static final double kCargoOut = 0.8;
    public static final double kCargoStop = 0.0;
    // MQTTReporter configurations
    public static final long fastMQTTRefreshRate = 50;
    public static final long slowMQTTRefreshRate = 250;
    public static final String brokerHostUrl = "tcp://chilivision.local:1883";
    // Vision configurations
    public static final double KCenter = 160;
    public static final double KRotateR = 0.2;
    public static final double KRotateL = -0.2;
    public static final double KO = 0;
	public static final double kVisionAlignKp = 1/(Constantes.kWidth / 2);

}