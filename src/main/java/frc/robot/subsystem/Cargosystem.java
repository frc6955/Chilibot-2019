package frc.robot.subsystem;

//import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.RobotIO;
import frc.robot.Constantes;
import frc.robot.Output;

public class Cargosystem implements Subsystem {
    private static Cargosystem instance;

    public static Cargosystem getInstance() {
        if (instance == null) {
            instance = new Cargosystem();
        }
        return instance;
    }
    private Output output;

    private Cargosystem() {
        output = Output.getInstance();
    }
    public void RollerIn(){
        output.setRollerMotor(Constantes.kCargoIn, Constantes.kCargoIn);
    }
    public void RollerOut(){
        output.setRollerMotor(Constantes.kCargoOut, Constantes.kCargoOut);
    }
    
    public void RollerStop(){
        output.setRollerMotor(Constantes.kCargoStop, Constantes.kCargoStop);
    }

    public void update(RobotIO entradas) {
        //Control Cargo Intake
        if(entradas.driverButton(Constantes.kButtonA)) {
            this.RollerOut();
        } else if(entradas.driverButton(Constantes.kButtonB)) {
            this.RollerIn();
        } else {
            this.RollerStop();
        }
    }
}