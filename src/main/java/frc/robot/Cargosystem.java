package frc.robot;

//import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Spark;

public class Cargosystem  {
    private static Cargosystem instance;

    public static Cargosystem getInstance() {
        if (instance == null) {
            instance = new Cargosystem(Constantes.kMotorCargo,Constantes.kVictor);
        }
        return instance;
    }

    Spark RightMotor;
    Victor LeftMotor;


    public Cargosystem(int c1,int c2) {
        RightMotor = new Spark(c1);
        LeftMotor = new Victor(c2);
    }
    public void RollerIn(){
        RightMotor.set(-0.4);
        LeftMotor.set(-0.4);
    }
    public void RollerOut(){
        RightMotor.set(0.4);
        LeftMotor.set(0.4);
    }
    public void RollerStop(){
        RightMotor.set(0);
        LeftMotor.set(0);
    }
}