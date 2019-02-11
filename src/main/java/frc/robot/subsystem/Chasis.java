package frc.robot.subsystem;

import frc.robot.RobotIO;
import edu.wpi.cscore.UsbCamera;
import frc.robot.Constantes;
import frc.robot.Output;


public class Chasis implements Subsystem {
    private static Chasis instance;

    public static Chasis getInstance() {
        if (instance == null) {
            instance = new Chasis(Constantes.kChassisLeft, Constantes.kChassisRight);
        }
        return instance;
    }

    
    boolean camFlag;
    private Output outputs;
    double entry;
    private Chasis(int ChasisLeft, int ChasisRight) {
        outputs = Output.getInstance();
        //Camaras y parametros
        camFlag = false;
        
    }

    public void driveMode(double left, double right) {
        outputs.setDifferencialDrive(left, right); 
        //IO.GetEntry(); 
    }

    public void Stream(UsbCamera cam){
        outputs.setStream(cam);
    }

    public void update(RobotIO entradas) {

        this.driveMode(entradas.driverAxis(Constantes.KAxisY_L), entradas.driverAxis(Constantes.KAxisX_R));
        
        if(camFlag){
            this.Stream(entradas.Cam(Constantes.kFront));
        }
        else{
            this.Stream(entradas.Cam(Constantes.kBack));
        }

        if(entradas.driverButton(Constantes.kButtonLB)){
            camFlag = !camFlag;
        }
        if(entradas.driverButton(Constantes.kButtonY)){
            double error = entradas.getVisionError();
            if (error != -1) {
                double u = -1 * error * Constantes.kVisionAlignKp;
                this.outputs.Chassis.arcadeDrive(entradas.driverAxis(Constantes.KAxisY_L), u, true);
            }
            else{
                this.outputs.Chassis.arcadeDrive(0, 0);
            }
        }
    }
}
