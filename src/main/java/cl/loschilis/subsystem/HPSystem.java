package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;
import cl.loschilis.Constantes;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public class HPSystem implements SubSystemInterface {
    
    private static HPSystem instance;
    
    public static HPSystem getInstance() {
        if (instance == null) {
            instance = new HPSystem();
        }
        return instance;
    }

    private void openFinger(RobotOutput robotOutput) {
        robotOutput.setSolenoidFinger(Value.kReverse);
    }

    private void closeFinger(RobotOutput robotOutput) {
        robotOutput.setSolenoidFinger(Value.kForward);
    }

    private void extendIntake(RobotOutput robotOutput) {
        robotOutput.setHPIntakeRail(Value.kForward);
    }

    private void contractIntake(RobotOutput robotOutput) {
        robotOutput.setHPIntakeRail(Value.kReverse);
    }

    @Override
    public void update(RobotInput entradas, RobotOutput salidas) {
        if(entradas.operatorButton(Constantes.kJoystickButtonA)){
            this.extendIntake(salidas);
        }
        else if(entradas.operatorButton(Constantes.kJoystickButtonY)){
            this.contractIntake(salidas);
        }

        if(entradas.operatorButton(Constantes.kJoystickButtonLB)){
            this.openFinger(salidas);
        }
        else if(entradas.operatorButton(Constantes.kJoystickButtonRB)){
            this.closeFinger(salidas);
        }
    }
}
