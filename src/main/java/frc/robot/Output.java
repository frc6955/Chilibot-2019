package frc.robot;

import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;



public class Output {
    //Brazo
    private TalonSRX TalonLeft;
    private TalonSRX TalonRight; 
    //Instancia
    private static Output instance;
    //Definir Chassis
    private Spark ChassisLeft,ChassisRight;
    public DifferentialDrive Chassis;
    private Compressor compresor;
    //Definir HPsystem
    private DoubleSolenoid Reel,Intake;
    //Definir Cargosystem
    private VictorSPX RightMotor;
    private VictorSPX LeftMotor;
    //Streaming
    private VideoSink server;

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
        RightMotor = new VictorSPX(Constantes.kMotorRight);
        LeftMotor = new VictorSPX(Constantes.KMotorLeft);
        //Streaming
        server = CameraServer.getInstance().getServer();
        //Brazo instancia
        TalonLeft = new TalonSRX(Constantes.KTalonL);
        TalonRight = new TalonSRX(Constantes.KTalonR);

    }
    //Brazo
    public void setRollerArm(double Speed){
        TalonRight.set(ControlMode.Follower,Constantes.KTalonL);
        TalonLeft.set(ControlMode.PercentOutput,Speed);
    }
    //Intake
    public void setRollerMotor(double speed) {
        LeftMotor.set(ControlMode.Follower,Constantes.kMotorRight);
        RightMotor.set(ControlMode.PercentOutput,speed);
    }
    //HatchPanel
    public void setSolenoidIntake(Value solenoidIntake) {
        Reel.set(solenoidIntake);
    }
    public void setSolenoidReel(Value solenoidReel){
        Intake.set(solenoidReel);
    }
    //Chassis
    public void setDifferencialDrive(double Y,double X){
        Chassis.arcadeDrive(Y, X);
    }
    //Camara
    public void setStream(UsbCamera cam){
        server.setSource(cam);
    }

}







