package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.VictorSP;

public class Cargosystem extends TimedRobot {

    Spark intake;
    TalonSRX  der;
    VictorSPX izq;

    @Override
    public void robotInit() {
    intake = new Spark(2);
    der = new TalonSRX(22);
    izq = new VictorSPX(21);
    izq.set(ControlMode.Follower,22);
    }

    public void liftArm(){
        der.set(0.8);
    }
}