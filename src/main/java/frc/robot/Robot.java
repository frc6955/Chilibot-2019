package frc.robot;


import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;
 
public class Robot extends TimedRobot {

  UsbCamera cam;
  Compressor compresor;

  RobotIO Driver;
  RobotIO Operador;
  Chasis chassis;
  Cargosystem cargoSystem;
  HPsystem hatchPanelIntake;

  @Override
  public void robotInit() {

    //Camara y parametros
    cam = CameraServer.getInstance().startAutomaticCapture();
    cam.setFPS(Constantes.kFPS);
    cam.setResolution(Constantes.kWidth, Constantes.kHeight);
    
    //Compresor
    compresor = new Compressor();
    compresor.setClosedLoopControl(true);

    //Inputs
    Driver = new RobotIO(0);
    Operador = new RobotIO(1);


    //Sistemas
    chassis = new Chasis(Constantes.kChassisLeft, Constantes.kChassisRight);

    cargoSystem = new Cargosystem(3,Constantes.kVictor);
    hatchPanelIntake = new HPsystem(Constantes.kSolenoideHPIntakeIn,Constantes.kSolenoideHPIntakeOut,Constantes.kSolenoideHPReelIn,Constantes.kSolenoideHPReelOut);
    
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {
   
    //Chassis
    chassis.driveMode(Driver.Ejes(Constantes.KAxisY_L),Driver.Ejes(Constantes.KAxisX_R));
    
    //Control Cargo Intake
    if(Driver.Boton(Constantes.kButtonA)){
      cargoSystem.RollerOut();
    }
    else if(Driver.Boton(Constantes.kButtonB)){
      cargoSystem.RollerIn();  
    }
    else{
      cargoSystem.RollerStop();
    }

    //Control Riel
    if(Operador.Boton(Constantes.kButtonA)){
      hatchPanelIntake.releaseReel();
    }
    else if(Operador.Boton(Constantes.kButtonB)){
      hatchPanelIntake.contractReel();
    }
    
    //Control HP Intake
    if(Operador.Boton(Constantes.kButtonLB)){
      hatchPanelIntake.openThingy();
    }
    else if(Operador.Boton(Constantes.kButtonRB)){
      hatchPanelIntake.closeThingy();
    }

  }

  @Override
  public void testPeriodic() {
  }
}
