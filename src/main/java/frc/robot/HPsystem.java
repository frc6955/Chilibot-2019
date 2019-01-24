package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HPsystem {

    DoubleSolenoid reel;
    DoubleSolenoid intake;

    public HPsystem(int p0, int p1, int p2, int p3) {
        reel = new DoubleSolenoid(p0, p1);
       intake = new DoubleSolenoid(p2, p3);
    }

    public void openThingy (){
        intake.set(Value.kForward);
    }
    public void closeThingy(){
        intake.set(Value.kReverse);
    }
    public void releaseReel(){
        reel.set(Value.kForward);
    }
    public void contractReel(){
        reel.set(Value.kReverse);
    }
}






