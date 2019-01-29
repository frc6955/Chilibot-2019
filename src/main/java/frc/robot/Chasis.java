package frc.robot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Chasis {
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