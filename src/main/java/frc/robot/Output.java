package frc.robot;


public class Output  {

    private static Output instance;

    public static Output getInstance() {
        if (instance == null) {
            instance = new Output();
        }
        return instance;
    }
    public void update(RobotIO Joystick,Cargosystem Cargo,HPsystem hatchPanelIntake,Chasis chassis) {
        //chassis
        chassis.driveMode(Joystick.Ejes(Constantes.KAxisY_L),Joystick.Ejes(Constantes.KAxisX_R));
            //Control Cargo Intake
        if(Joystick.Boton(Constantes.kButtonA)){
            Cargo.RollerOut();
          }
          else if(Joystick.Boton(Constantes.kButtonB)){
            Cargo.RollerIn();
          }
          else{
            Cargo.RollerStop();
          }
      
          //Control Riel
          if(Joystick.Boton(Constantes.kButtonA)){
            hatchPanelIntake.releaseReel();
          }
          else if(Joystick.Boton(Constantes.kButtonB)){
            hatchPanelIntake.contractReel();
          }
          
          //Control HP Intake
          if(Joystick.Boton(Constantes.kButtonLB)){
            hatchPanelIntake.openThingy();
          }
          else if(Joystick.Boton(Constantes.kButtonRB)){
            hatchPanelIntake.closeThingy();
          }
    }
}








