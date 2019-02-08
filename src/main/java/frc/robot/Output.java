package frc.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;



public class Output {
    //Instancia
    private static Output instance;
    //Definir Chassis
    private Spark ChassisLeft,ChassisRight;
    private DifferentialDrive Chassis;
    private Compressor compresor;
    //Definir HPsystem
    private DoubleSolenoid Reel,Intake;
    //Definir Cargosystem
    private VictorSPX RightMotor;
    private VictorSPX LeftMotor;
    //Streaming
    private VideoSink server;
    //Arm
    private TalonSRX RightArm;
    private TalonSRX LeftArm;

    public static Output getInstance() {
        if (instance == null) {
            instance = new Output();
        }
        return instance;
    }

    private Output () {
        //Chassis
        ChassisLeft = new Spark(Constantes.kChassisLeft);
        ChassisRight = new Spark(Constantes.kChassisRight);
        Chassis = new DifferentialDrive(ChassisLeft, ChassisRight);
        compresor = new Compressor(1);
        compresor.setClosedLoopControl(true);
        //HPsystem
        Reel = new DoubleSolenoid(Constantes.kSolenoideHPReelIn, Constantes.kSolenoideHPReelOut);
        Intake = new DoubleSolenoid(Constantes.kSolenoideHPIntakeIn, Constantes.kSolenoideHPIntakeOut);
        //Cargosystem
        RightMotor = new VictorSPX(Constantes.kMotorCargo);
        LeftMotor = new VictorSPX(Constantes.kMotorIntake);
        //Streaming
        server = CameraServer.getInstance().getServer();
        //Arm
        RightArm = new TalonSRX(Constantes.kRightArm);
        LeftArm = new TalonSRX(Constantes.kLeftArm);
    }

    public void setRollerMotor(double speedLeft, double speedRight) {
        RightMotor.set(ControlMode.PercentOutput,speedRight);
        LeftMotor.set(ControlMode.PercentOutput,speedLeft);
    }
    public void setArmMotor (double speedL, double speedR){
        RightArm.set(ControlMode.PercentOutput, speedL);
        LeftArm.set(ControlMode.Follower, speedR);
    }
    public void setSolenoidIntake(Value solenoidIntake) {
        Reel.set(solenoidIntake);
    }
    public void setSolenoidReel(Value solenoidReel){
        Intake.set(solenoidReel);
    }
    public void setDifferencialDrive(double Y,double X){
        Chassis.arcadeDrive(Y, X);
    }

    public void setStream(UsbCamera cam){
        server.setSource(cam);
    }

}








