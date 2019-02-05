package frc.robot.subsystem;

import frc.robot.RobotIO;
import frc.robot.Constantes;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class Chasis implements Subsystem {
    private static Chasis instance;

    public static Chasis getInstance() {
        if (instance == null) {
            instance = new Chasis(Constantes.kChassisLeft, Constantes.kChassisRight);
        }
        return instance;
    }

    UsbCamera frontCam, backCam;
    boolean camFlag;
    VideoSink server;
    Compressor compresor;
    Spark leftDriver, rightDriver;
    DifferentialDrive chassis;
    private Chasis(int ChasisLeft, int ChasisRight) {
        leftDriver = new Spark(ChasisLeft);
        rightDriver = new Spark(ChasisRight);
        chassis = new DifferentialDrive(leftDriver, rightDriver);
        
        //Camaras y parametros
        camFlag = false;
        frontCam = CameraServer.getInstance().startAutomaticCapture(0);
        backCam = CameraServer.getInstance().startAutomaticCapture(1);
        frontCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);
        backCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, Constantes.kWidth, Constantes.kHeight, Constantes.kFPS);
        frontCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        backCam.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        server = CameraServer.getInstance().getServer();

        //Compresor
        compresor = new Compressor();
        compresor.setClosedLoopControl(true);
    }

    public void driveMode(double left, double right) {
        this.chassis.arcadeDrive(left, right);
    }

    public void update(RobotIO entradas) {
        this.driveMode(entradas.Ejes(Constantes.KAxisY_L, Constantes.kJoystick), entradas.Ejes(Constantes.KAxisX_R, Constantes.kJoystick));
        
        if(camFlag){
            server.setSource(backCam);
        }
        else{
            server.setSource(frontCam);
        }

        if(entradas.Boton(Constantes.kButtonLB, Constantes.kJoystick)){
            camFlag = !camFlag;
        }
    }
}