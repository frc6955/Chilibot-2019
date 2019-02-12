package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;
import cl.loschilis.Constantes;


public class Cargosystem implements SubSystemInterface {

    private static Cargosystem instance;

    public static Cargosystem getInstance() {
        if (instance == null) {
            instance = new Cargosystem();
        }
        return instance;
    }

    private void rollerIn(RobotOutput robotOutput) {
        robotOutput.setRollerMotor(Constantes.kCargoIn);
    }

    private void rollerOut(RobotOutput robotOutput) {
        robotOutput.setRollerMotor(Constantes.kCargoOut);
    }
    
    private void rollerStop(RobotOutput robotOutput) {
        robotOutput.setRollerMotor(Constantes.kCargoStop);
    }

    @Override
    public void update(RobotInput entradas, RobotOutput salidas) {
        if(entradas.driverButton(Constantes.kJoystickButtonA)) {
            this.rollerOut(salidas);
        } else if(entradas.driverButton(Constantes.kJoystickButtonB)) {
            this.rollerIn(salidas);
        } else {
            this.rollerStop(salidas);
        }
    }
}