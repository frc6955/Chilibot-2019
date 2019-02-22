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

    @Override
    public void update(RobotInput entradas, RobotOutput salidas) {
        if(entradas.getSecondaryJoyButton(Constantes.kJoystickButtonA)){
            salidas.setHPIntakeRail(Value.kForward);
        }
        else if(entradas.getSecondaryJoyButton(Constantes.kJoystickButtonY)){
            salidas.setHPIntakeRail(Value.kReverse);
        }

        if(entradas.getSecondaryJoyButton(Constantes.kJoystickButtonLB)){
            salidas.setHPIntakeFinger(Value.kReverse);
        }
        else if(entradas.getSecondaryJoyButton(Constantes.kJoystickButtonRB)){
            salidas.setHPIntakeFinger(Value.kForward);
        }
    }

    @Override
    public void stop(RobotInput entradas, RobotOutput salidas) {

    }
}
