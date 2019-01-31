package frc.robot.subsystem;

//import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Victor;
import frc.robot.Constantes;
import frc.robot.RobotIO;
import edu.wpi.first.wpilibj.Spark;

public class Cargosystem implements Subsystem {
    private static Cargosystem instance;

    public static Cargosystem getInstance() {
        if (instance == null) {
            instance = new Cargosystem(Constantes.kMotorCargo,Constantes.kVictor);
        }
        return instance;
    }

    Spark RightMotor;
    Victor LeftMotor;


    private Cargosystem(int c1,int c2) {
        RightMotor = new Spark(c1);
        LeftMotor = new Victor(c2);
    }
    public void RollerIn(){
        RightMotor.set(Constantes.kCargoIn);
        LeftMotor.set(Constantes.kCargoIn);
    }
    public void RollerOut(){
        RightMotor.set(Constantes.kCargoOut);
        LeftMotor.set(Constantes.kCargoOut);
    }
    
    public void RollerStop(){
        RightMotor.set(Constantes.kCargoStop);
        LeftMotor.set(Constantes.kCargoStop);
    }

    public void update(RobotIO entradas) {
        //Control Cargo Intake
        if(entradas.Boton(Constantes.kButtonA, Constantes.kOperator)) {
            this.RollerOut();
        } else if(entradas.Boton(Constantes.kButtonB, Constantes.kOperator)) {
            this.RollerIn();
        } else {
            this.RollerStop();
        }
    }
}