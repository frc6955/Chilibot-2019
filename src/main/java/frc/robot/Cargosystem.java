package frc.robot;



import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Spark;

public class Cargosystem  {

    Spark intake;
    TalonSRX  RightMotor;
    VictorSPX LeftMotor;

    public Cargosystem(int c1,int c2, int c3) {
        intake = new Spark(c1);
        RightMotor = new TalonSRX(c2);
        LeftMotor = new VictorSPX(c3);
        LeftMotor.set(ControlMode.Follower,c2);
    }

        public void LiftArm(){
            RightMotor.set(ControlMode.PercentOutput,0.8);
        }
        public void DropArm(){
            RightMotor.set(ControlMode.PercentOutput,-0.8);
        }
        public void RollerOut(){
            intake.set(0.8);
        }
        public void RollerIn(){
            intake.set(-0.8);
        }
        public void RollerStop(){
            intake.set(0);
        }
}