package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;
import cl.loschilis.Constantes;
import edu.wpi.cscore.UsbCamera;


public class Chasis implements SubSystemInterface {

    private static Chasis instance;
    private boolean camFlag;

    public static Chasis getInstance() {
        if (instance == null) {
            instance = new Chasis();
        }
        return instance;
    }

    private Chasis() {
        this.camFlag = false;
    }

    private void driveMode(RobotOutput robotOutput, double left, double right) {
        robotOutput.setCuratureDrive(left, right);
    }

    private void stream(RobotOutput robotOutput, UsbCamera cam) {
        robotOutput.setStream(cam);
    }

    public void update(RobotInput entradas, RobotOutput salidas) {
        driveMode(salidas,
                entradas.driverAxis(Constantes.kJoystickAxisLeftY),
                entradas.driverAxis(Constantes.kJoystickAxisRightX));
        
        // if(camFlag) {
        //     stream(salidas, entradas.Cam(Constantes.kFront));
        // } else {
        //     stream(salidas, entradas.Cam(Constantes.kBack));
        // }

        if(entradas.driverButton(Constantes.kJoystickButtonX)) camFlag = !camFlag;

        if(entradas.driverButton(Constantes.kJoystickButtonB)){
            double error = entradas.getVisionError();
            if (error != -1) {
                double u = -1 * error * Constantes.kVisionAlignKp;
                salidas.setFPSDrive(entradas.driverAxis(Constantes.kJoystickAxisLeftY), u);
            } else {
                salidas.setFPSDrive(0, 0);
            }
        }
    }
}