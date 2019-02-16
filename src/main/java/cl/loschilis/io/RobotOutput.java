package cl.loschilis.io;

import cl.loschilis.Constantes;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;


public class RobotOutput {

    private static RobotOutput instance;
    private Spark chassisLeft, chassisRight;
    private TalonSRX armMaster, armFollower;
    private VictorSPX intakeLeft, intakeRight;
    private DoubleSolenoid hpIntakeRail, hpIntakeFinger;
    private DifferentialDrive chassis;
    private Compressor compressor;
    private VideoSink camServer;

    public static RobotOutput getInstance() {
        if (instance == null) {
            instance = new RobotOutput();
        }
        return instance;
    }

    private RobotOutput() {
        // Chassis and drivetrain motors
        chassisLeft = new Spark(Constantes.kMotorPWMChassisLeft);
        chassisRight = new Spark(Constantes.kMotorPWMChassisRight);
        chassis = new DifferentialDrive(chassisLeft, chassisRight);
        // Compressor
        compressor = new Compressor(Constantes.kPCMCANID);
        // Hatch Panel Intake (Rail and finger) Solenoids
        hpIntakeRail = new DoubleSolenoid(Constantes.kSolenoidChannelIntakeRetract, Constantes.kSolenoidChannelIntakeExtend);
        hpIntakeFinger = new DoubleSolenoid(Constantes.kSolenoidChannelFingerOpen, Constantes.kSolenoidChannelFingerClose);
        // Arm motors
        armMaster = new TalonSRX(Constantes.kMotorCANArmMasterID);
        armFollower = new TalonSRX(Constantes.kMotorCANArmSlaveID);
        // Intake motors
        intakeLeft = new VictorSPX(Constantes.kMotorCANIntakeLeftID);
        intakeRight = new VictorSPX(Constantes.kMotorCANIntakeRightID);
        // Camera server
        camServer = CameraServer.getInstance().getServer();

        // Configure actuators in an initial state
        this.setInitialState();
    }

    public void setInitialState() {
        // Set closed loop on compressor for 120 PSI
        compressor.setClosedLoopControl(true);
        // Set follower on arm and configure direction for both motors
        armFollower.follow(armMaster);
        armMaster.setInverted(Constantes.kMotorDirectionArmMaster);
        armFollower.setInverted(InvertType.FollowMaster);
        // Set directions on drive motor
        chassisLeft.setInverted(Constantes.kMotorDirectionChassisLeft);
        chassisRight.setInverted(Constantes.kMotorDirectionChassisRight);
        // Set follower on intake and configure direction for both motors
        intakeRight.follow(intakeLeft);
        intakeLeft.setInverted(Constantes.kMotorDirectionIntakeLeft);
        intakeRight.setInverted(InvertType.FollowMaster);
    }

    public void setRollerMotor(double speed) {
        intakeLeft.set(ControlMode.PercentOutput, speed);
    }

    public void setArmMotor(double speed) {
        armMaster.set(ControlMode.PercentOutput, speed);
    }

    public void setHPIntakeRail(Value railPosition) {
        this.hpIntakeRail.set(railPosition);
    }

    public void setSolenoidFinger(Value fingerPosition){
        this.hpIntakeFinger.set(fingerPosition);
    }

    public void setDifferentialDrive(double leftSpeed, double rightSpeed){
        this.chassis.tankDrive(leftSpeed, rightSpeed);
    }

    public void setFPSDrive(double forwardSpeed, double rotationSpeed) {
        this.chassis.arcadeDrive(-1*forwardSpeed, rotationSpeed);
    }
    public void setCuratureDrive(double forwardSpeed, double rotationSpeed) {
        this.chassis.curvatureDrive(forwardSpeed, rotationSpeed,true);
    }

    public void setStream(UsbCamera cam){
        this.camServer.setSource(cam);
    }
}








