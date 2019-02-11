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
        output.setRollerMotor(Constantes.kCargoIn);
    }
    public void RollerOut(){
        output.setRollerMotor(Constantes.kCargoOut);
    }
    
    public void RollerStop(){
        output.setRollerMotor(Constantes.kCargoStop);
    }
    public void ArmUp(){
        output.setRollerArm(Constantes.KCargoUp);
    }

    public void ArmDown(){
        output.setRollerArm(Constantes.KCargoDown);
    }

    public void update(RobotIO entradas) {
        //Control Cargo Intake
        if(entradas.driverButton(Constantes.kButtonLB)) {
            this.RollerOut();
        } else if(entradas.driverButton(Constantes.kButtonRB)) {
            this.RollerIn();
        } else {
            this.RollerStop();
        }
        if(entradas.driverButton(Constantes.kButtonA)){
            this.ArmDown();
        }
        else if(entradas.driverButton(Constantes.kButtonY)){
            this.ArmUp();
        }
    }
}