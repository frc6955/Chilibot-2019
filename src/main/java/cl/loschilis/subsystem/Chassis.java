package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;
import cl.loschilis.Constantes;


public class Chassis implements SubSystemInterface {

    private static Chassis instance;
    private boolean displayForwardCamera;

    public static Chassis getInstance() {
        if (instance == null) {
            instance = new Chassis();
        }
        return instance;
    }

    private Chassis() {
        this.displayForwardCamera = false;
    }

    public void update(RobotInput entradas, RobotOutput salidas) {

        if(entradas.getPrimaryJoyButton(Constantes.kJoystickButtonB)){
            double visionError = entradas.getVisionError();
            if (visionError != Constantes.kVisionNoLockState) {
                double turnSignal = Constantes.kVisionAlignKp * visionError;
                salidas.getRobotChassis().arcadeDrive(entradas.getPrimaryJoyAxis(Constantes.kJoystickAxisLeftY), turnSignal);
            }
        } else {
            salidas.getRobotChassis().arcadeDrive(entradas.getPrimaryJoyAxis(Constantes.kJoystickAxisLeftY), entradas.getPrimaryJoyAxis(Constantes.kJoystickAxisRightX));
        }

        if(entradas.getSecondaryJoyButtonOnPress(Constantes.kJoystickButtonX)) {
            displayForwardCamera = !displayForwardCamera;
            entradas.switchCameras();
        }  
    }

    @Override
    public void stop(RobotInput entradas, RobotOutput salidas) {

    }
}
