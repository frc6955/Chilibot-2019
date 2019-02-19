package cl.loschilis.io;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import cl.loschilis.Constantes;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class RobotOutput {

    private static RobotOutput instance;
    private Spark chassisLeft, chassisRight;
    private static TalonSRX armMaster, armFollower;
    private VictorSPX intakeLeft, intakeRight;
    private DoubleSolenoid hpIntakeRail, hpIntakeFinger;
    private DifferentialDrive chassis;
    private Compressor compressor;

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
        // Hatch Panel Intake (Rail and finger) Solenoids. If PCM ID is non-zero, ID must be placed as first argument
        hpIntakeRail = new DoubleSolenoid(Constantes.kPCMCANID, Constantes.kSolenoidChannelIntakeRetract, Constantes.kSolenoidChannelIntakeExtend);
        hpIntakeFinger = new DoubleSolenoid(Constantes.kPCMCANID, Constantes.kSolenoidChannelFingerOpen, Constantes.kSolenoidChannelFingerClose);
        // Arm motors
        armMaster = new TalonSRX(Constantes.kMotorCANArmMasterID);
        armFollower = new TalonSRX(Constantes.kMotorCANArmSlaveID);
        // Intake motors
        intakeLeft = new VictorSPX(Constantes.kMotorCANIntakeLeftID);
        intakeRight = new VictorSPX(Constantes.kMotorCANIntakeRightID);
        // Configure actuators in an initial state
        this.setInitialState();
        // Configure Talon SRX configurations
        this.setTalonSRXInitialStates();
    }

    public void setInitialState() {
        // Set closed loop on compressor for 120 PSI
        compressor.setClosedLoopControl(true);
        // Set directions on drive motor
        chassisLeft.setInverted(Constantes.kMotorDirectionChassisLeft);
        chassisRight.setInverted(Constantes.kMotorDirectionChassisRight);
        // Set follower on intake and configure direction for both motors
        intakeRight.follow(intakeLeft);
        intakeLeft.setInverted(Constantes.kMotorDirectionIntakeLeft);
        intakeRight.setInverted(InvertType.FollowMaster);
    }

    public void setTalonSRXInitialStates() {
        // Set follower on arm and configure direction for both motors
        armFollower.follow(armMaster);
        armMaster.setInverted(Constantes.kMotorDirectionArmMaster);
        armFollower.setInverted(InvertType.FollowMaster);
        // Configure QuadEncoder and phase
        armMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constantes.kTalonConfigPIDLoopIdx, Constantes.kTalonConfigTimeoutMs);
        armMaster.setSensorPhase(Constantes.kTalonConfigSensorPhase);
        // Configure forward and rear limit switches
        armMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constantes.kTalonConfigTimeoutMs);
        armFollower.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, armMaster.getDeviceID(), Constantes.kTalonConfigTimeoutMs);
        armMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constantes.kTalonConfigTimeoutMs);
        armFollower.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, armMaster.getDeviceID(), Constantes.kTalonConfigTimeoutMs);
        // Configure PID Controller and Motion Magic
        armMaster.selectProfileSlot(Constantes.kTalonConfigSlotIdx, Constantes.kTalonConfigPIDLoopIdx);
        armMaster.config_kF(Constantes.kTalonConfigSlotIdx, Constantes.kTalonConfigkF, Constantes.kTalonConfigTimeoutMs);
        armMaster.config_kP(Constantes.kTalonConfigSlotIdx, Constantes.kTalonConfigkP, Constantes.kTalonConfigTimeoutMs);
        armMaster.config_kI(Constantes.kTalonConfigSlotIdx, Constantes.kTalonConfigkI, Constantes.kTalonConfigTimeoutMs);
        armMaster.config_kD(Constantes.kTalonConfigSlotIdx, Constantes.kTalonConfigkD, Constantes.kTalonConfigTimeoutMs);
        armMaster.config_IntegralZone(Constantes.kTalonConfigSlotIdx, Constantes.kTalonConfigIZone, Constantes.kTalonConfigTimeoutMs);
        armMaster.configPeakOutputForward(Constantes.kTalonConfigPeakForward, Constantes.kTalonConfigTimeoutMs);
        armMaster.configPeakOutputReverse(Constantes.kTalonConfigPeakReverse, Constantes.kTalonConfigTimeoutMs);
        armMaster.configMotionAcceleration(Constantes.kTalonConfigMotionAcceleration, Constantes.kTalonConfigTimeoutMs);
        armMaster.configMotionCruiseVelocity(Constantes.kTalonConfigMotionCruise, Constantes.kTalonConfigTimeoutMs);
        armMaster.configAllowableClosedloopError(Constantes.kTalonConfigSlotIdx, Constantes.kTalonConfigEps, Constantes.kTalonConfigTimeoutMs);
    }

    public void homeArm() {
        // Home arm to upper position. The second conditional ensures a negative encoder count, because a positive one means the arm already passed by the encoder home
        while(!armMaster.getSensorCollection().isFwdLimitSwitchClosed() && (armMaster.getSelectedSensorPosition(Constantes.kTalonConfigPIDLoopIdx) <= 0)) {
            armMaster.set(ControlMode.PercentOutput, Constantes.kArmHomingSpeed);
        }
        armMaster.setSelectedSensorPosition(Constantes.kArmHome, Constantes.kTalonConfigPIDLoopIdx, Constantes.kTalonConfigTimeoutMs);
    }

    public void setIntakeMotor(double speed) {
        this.intakeLeft.set(ControlMode.PercentOutput, speed);
    }

    public void setArmSpeed(double speed) {
        armMaster.set(ControlMode.PercentOutput, speed);
    }

    public void setArmPosition(int position) {
        armMaster.set(ControlMode.MotionMagic, position);
    }

    public void setHPIntakeRail(Value railPosition) {
        this.hpIntakeRail.set(railPosition);
    }

    public void setHPIntakeFinger(Value fingerPosition){
        this.hpIntakeFinger.set(fingerPosition);
    }

    public DifferentialDrive getRobotChassis() {
        return this.chassis;
    }

    public static TalonSRX getArmMotorReference() {
        if (instance == null) {
            throw new IllegalStateException("Uninitialized outputs");
        }
        return armMaster;
    }
}
