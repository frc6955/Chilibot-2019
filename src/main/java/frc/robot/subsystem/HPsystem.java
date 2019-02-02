package frc.robot.subsystem;

import frc.robot.RobotIO;
import frc.robot.Constantes;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class HPsystem implements Subsystem {
    private static HPsystem instance;
    
    public static HPsystem getInstance() {
        if (instance == null) {
            instance = new HPsystem(Constantes.kSolenoideHPIntakeIn,Constantes.kSolenoideHPIntakeOut,Constantes.kSolenoideHPReelIn,Constantes.kSolenoideHPReelOut);
        }
        return instance;
    }

    DoubleSolenoid reel;
    DoubleSolenoid intake;

    private HPsystem(int p0, int p1, int p2, int p3) {
        reel = new DoubleSolenoid(p0, p1);
       intake = new DoubleSolenoid(p2, p3);
    }

    public void openThingy (){
        intake.set(Value.kForward);
    }
    public void closeThingy(){
        intake.set(Value.kReverse);
    }
    public void releaseReel(){
        reel.set(Value.kForward);
    }
    public void contractReel(){
        reel.set(Value.kReverse);
    }
    public void update(RobotIO entradas){
        //Control Riel
        if(entradas.Boton(Constantes.kButtonA, Constantes.kOperator)){
            this.releaseReel();
          }
          else if(entradas.Boton(Constantes.kButtonB, Constantes.kOperator)){
            this.contractReel();
          }
          
          //Control HP Intake
          if(entradas.Boton(Constantes.kButtonLB, Constantes.kOperator)){
            this.openThingy();
          }
          else if(entradas.Boton(Constantes.kButtonRB, Constantes.kOperator)){
            this.closeThingy();
          }

    }
    


}

