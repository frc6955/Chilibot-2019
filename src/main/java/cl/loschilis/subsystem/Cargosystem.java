package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;

import com.ctre.phoenix.motorcontrol.ControlMode;

import cl.loschilis.Constantes;


public class CargoSystem implements SubSystemInterface {

    private static CargoSystem instance;

    public static CargoSystem getInstance() {
        if (instance == null) {
            instance = new CargoSystem();
        }
        return instance;
    }

    @Override
    public void update(RobotInput entradas, RobotOutput salidas) {
        if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonLB)) {
            salidas.setIntakeMotor(Constantes.kSpeedIntakeOut);
        } else if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonRB)) {
            salidas.setIntakeMotor(Constantes.kSpeedIntakeIn);
        } else {
            if (entradas.getUltrasonicAdquisition()) {
                salidas.setIntakeMotor(Constantes.kSpeedIntakeHold);
            } else {
                salidas.setIntakeMotor(Constantes.kSpeedIntakeStop);
            }
        }
        if (entradas.getSecondaryJoyPOVAngle() == -1) {
            if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonA)) {
                salidas.setArmSpeed(Constantes.kSpeedArmDown);
            } else if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonY)) {
                salidas.setArmSpeed(Constantes.kSpeedArmUp);
            } else {
                salidas.setArmSpeed(Constantes.kSpeedArmStop);
            }
        } else {
            // Protect setpoint setting with a flag
            if (entradas.getSecondaryJoyPOVAngle() == 0) {
                salidas.setArmPosition(Constantes.kArmHardStop);
            } else if (entradas.getSecondaryJoyPOVAngle() == 45) {
                salidas.setArmPosition((Constantes.kArmRocket + Constantes.kArmHardStop) / 2);
            } else if (entradas.getSecondaryJoyPOVAngle() == 90) {
                salidas.setArmPosition(Constantes.kArmRocket);
            } else if (entradas.getSecondaryJoyPOVAngle() == 180) {
                salidas.setArmPosition(Constantes.kArmFloor);
            }
        }
        
    }

	@Override
	public void stop(RobotInput entradas, RobotOutput salidas) {
		
	}
}