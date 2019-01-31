package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constantes;
import frc.robot.RobotIO;

public class Chasis implements Subsystem {
    private static Chasis instance;

    public static Chasis getInstance() {
        if (instance == null) {
            instance = new Chasis(Constantes.kChassisLeft, Constantes.kChassisRight);
        }
        return instance;
    }

    Spark leftDriver, rightDriver;
    DifferentialDrive chassis;

    private Chasis(int ChasisLeft, int ChasisRight) {
        leftDriver = new Spark(ChasisLeft);
        rightDriver = new Spark(ChasisRight);
        chassis = new DifferentialDrive(leftDriver, rightDriver);

    }

    public void driveMode(double left, double right) {
        this.chassis.arcadeDrive(left, right);
    }

    public void update(RobotIO entradas) {
        this.driveMode(entradas.Ejes(Constantes.KAxisY_L, Constantes.kJoystick), entradas.Ejes(Constantes.KAxisX_R, Constantes.kJoystick));
    }
}