package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HPsystem extends TimedRobot {
DoubleSolenoid reel;
DoubleSolenoid intake;

  

@Override
public void robotInit() {
    reel = new DoubleSolenoid(0,1);
    intake = new DoubleSolenoid(2,3);
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






