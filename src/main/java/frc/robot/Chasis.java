package frc.robot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Chasis {
    public static Chasis instance;

    public static Chasis getInstance() {
        if (instance == null) {
            instance = new Chasis(Constantes.kChassisLeft, Constantes.kChassisRight);
        }
        return instance;
    }
    Spark leftDriver, rightDriver;
    DifferentialDrive chassis;

    public Chasis(int ChasisLeft, int ChasisRight){
        leftDriver = new Spark(ChasisLeft);
        rightDriver = new Spark(ChasisRight);
        chassis = new DifferentialDrive(leftDriver, rightDriver);
        
    }   
    public void driveMode( double left, double right){
        this.chassis.arcadeDrive(left, right);
        
    }
}