package frc.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;

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
    private Spark RightMotor;
    private Victor LeftMotor;
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
        RightMotor = new Spark(Constantes.kMotorCargo);
        LeftMotor = new Victor(Constantes.kMotorIntake);
        //Streaming
        server = CameraServer.getInstance().getServer();
    }

    public void setRollerMotor(double speedLeft, double speedRight) {
        RightMotor.set(speedRight);
        LeftMotor.set(speedLeft);
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








