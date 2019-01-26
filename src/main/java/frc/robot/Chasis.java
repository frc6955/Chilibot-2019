package frc.robot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Chasis {
    Spark leftDriver, rightDriver;
    DifferentialDrive chassi;

    public Chasis(int ChasisLeft, int ChasisRight){
        leftDriver = new Spark(ChasisLeft);
        rightDriver = new Spark(ChasisRight);
        chassi = new DifferentialDrive(leftDriver, rightDriver);
        
    }   
    public void drivemode( double left, double right){
        this.chassi.arcadeDrive(left, right);
        
    }
}