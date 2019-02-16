package cl.loschilis;
 
public class Constantes {
    // CAN Peripherals
    public static final int kPCMCANID = 1;
    public static final int kPDPCANID = 39;
    // PDP Channels
    public static final int kPDPChannelChassisRightA = 0;
    public static final int kPDPChannelChassisRightB = 1;
    public static final int kPDPChannelChassisLeftA = 2;
    public static final int kPDPChannelChassisLeftB = 3;
    public static final int kPDPChannelArmMaster = 15;
    public static final int kPDPChannelArmSlave = 14;
    public static final int kPDPChannelIntakeMaster = 13;
    public static final int kPDPChannelIntakeSlave = 12;
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
    public static final int kMotorCANIntakeLeftID = 9;
    public static final int kMotorCANIntakeRightID = 10;
    // Motor directions
    public static final boolean kMotorDirectionArmMaster = false;
    public static final boolean kMotorDirectionChassisLeft = false;
    public static final boolean kMotorDirectionChassisRight = false;
    public static final boolean kMotorDirectionIntakeLeft = false;
    // Joysticks
    public static final int kJoystickUSBDriver = 0;
    public static final int kJoystickUSBOperator = 1;
    // Buttons
    public static final int kJoystickButtonA = 1;
    public static final int kJoystickButtonB = 2;
    public static final int kJoystickButtonX = 3;
    public static final int kJoystickButtonY = 4;
    public static final int kJoystickButtonLB = 5;
    public static final int kJoystickButtonRB = 6;
    // Axis
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
    // Cargo Arm Speeds
    public static final double kSpeedIntakeIn = -0.6;
    public static final double kSpeedIntakeOut = 0.8;
    public static final double kSpeedIntakeStop = 0.0;
    public static final double kSpeedUltraSonic = 0.05;
    public static final double kSpeedArmUp = 0.4;
    public static final double kSpeedArmDown = -0.4;
    public static final double kSpeedArmStop = 0;
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
    public static final String kVisionNTTable = "vision";
}