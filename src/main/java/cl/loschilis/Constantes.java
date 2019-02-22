package cl.loschilis;
 
public class Constantes {
    // Sensors
    public static final int kAnalogMaxbotixBallDetectorPin = 0;
    public static final double kAnalogMaxbotixMilliVoltsPerFiveMillimeters = 4.88;
    public static final double kAnalogMaxbotixVoltsPerCentimeter = Constantes.kAnalogMaxbotixMilliVoltsPerFiveMillimeters * 2 / 1000;
    public static final double kAnalogMaxbotixMinimumThreshold = 35;
    // CAN Peripherals
    public static final int kPDPCANID = 1;
    public static final int kPCMCANID = 2;
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
    public static final int kCameraIndexFront = 0;
    public static final int kCameraIndexBack = 1;
    public static final int kCameraFPS = 15;
    public static final int kCameraLargeWidth = 320;
    public static final int kCameraLargeHeight = 240;
    public static final int kCameraSmallWidth = 160;
    public static final int kCameraSmallHeight = 120;
    // Cargo Arm Speeds
    public static final double kSpeedIntakeIn = -0.6;
    public static final double kSpeedIntakeOut = 0.4;
    public static final double kSpeedIntakeStop = 0.0;
    public static final double kSpeedIntakeHold = 0.1;
    public static final double kSpeedArmUp = 0.25;
    public static final double kSpeedArmDown = -0.25;
    public static final double kSpeedArmStop = 0;
    // MQTTReporter configurations
    public static final long fastMQTTRefreshRate = 50;
    public static final long slowMQTTRefreshRate = 250;
    public static final String brokerHostUrl = "tcp://10.69.55.20:1883";
    // Vision configurations
    public static final double KCenter = 160;
    public static final double KRotateR = 0.2;
    public static final double KRotateL = -0.2;
    public static final double KO = 0;
	public static final double kVisionAlignKp = -1 / (Constantes.kCameraLargeWidth / 2);
    public static final String kVisionNTTable = "vision";
    public static final double kVisionNoLockState = -255;
    // Talon SRX PID Configurations
    public static final int kTalonConfigSlotIdx = 0;
    public static final int kTalonConfigPIDLoopIdx = 0;
    public static final int kTalonConfigTimeoutMs = 100;
    public static final double kTalonConfigkP = 1.7;
    public static final double kTalonConfigkI = 0;
    public static final double kTalonConfigkD = 0;
    public static final double kTalonConfigkF = 0;
    public static final int kTalonConfigEps = 2;
    public static final int kTalonConfigIZone = 0;
    public static final boolean kTalonConfigSensorPhase = false;
    public static final double kTalonConfigPeakForward = 1.0;
    public static final double kTalonConfigPeakReverse = -1.0;
    public static final int kTalonConfigMotionCruise = 80;
    public static final int kTalonConfigMotionAcceleration = 140;
    // PID Setpoints
    public static final int kArmHome = 0;
    public static final int kArmHardStop = -3;
    public static final int kArmRocket = -450;
    public static final int kArmFloor = -675;
    public static final double kArmHomingSpeed = 0.15;
    // LED Driver
    public static final int kLEDPWMFrontRedChannel = 2;
    public static final int kLEDPWMFrontBlueChannel = 3;
    public static final int kLEDPWMRearRedChannel = 4;
    public static final int kLEDPWMRearBlueChannel = 5;
}