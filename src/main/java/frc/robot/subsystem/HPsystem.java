package frc.robot.subsystem;

import frc.robot.RobotIO;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constantes;
import frc.robot.Output;

public class HPsystem implements Subsystem {
     
    private static HPsystem instance;
    
    public static HPsystem getInstance() {
        if (instance == null) {
            instance = new HPsystem();
        }
        return instance;
    }

    private Output outputs;

    private HPsystem() {
        outputs = Output.getInstance();
    }

    public void openThingy (){
        outputs.setSolenoidIntake(Value.kReverse);
    }

    public void closeThingy(){
        outputs.setSolenoidIntake(Value.kForward);
    }

    public void releaseReel(){
        outputs.setSolenoidReel(Value.kForward);
    }

    public void contractReel(){
        outputs.setSolenoidReel(Value.kReverse);
    }

    public void update(RobotIO entradas){
        //Control Riel
        if(entradas.operatorButton(Constantes.kButtonA)){
            this.releaseReel();
        }
        else if(entradas.operatorButton(Constantes.kButtonB)){
            this.contractReel();
        }
        //Control HP Intake
        if(entradas.operatorButton(Constantes.kButtonLB)){
            this.openThingy();
        }
        else if(entradas.operatorButton(Constantes.kButtonRB)){
            this.closeThingy();
        }
    }
}
