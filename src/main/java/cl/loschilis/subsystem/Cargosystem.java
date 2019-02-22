package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;

import cl.loschilis.Constantes;


public class CargoSystem implements SubSystemInterface {

    private static CargoSystem instance;

    private boolean armSetpointSet;

    public static CargoSystem getInstance() {
        if (instance == null) {
            instance = new CargoSystem();
        }
        return instance;
    }

    private CargoSystem() {
        this.armSetpointSet = false;
    }

    @Override
    public void update(RobotInput entradas, RobotOutput salidas) {
        if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonLB)) {
            salidas.setIntakeMotor(Constantes.kSpeedIntakeOut);
        } else if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonRB)) {
            salidas.setIntakeMotor(Constantes.kSpeedIntakeIn);
        } else {
            if (entradas.getBallAdquisition()) {
                salidas.setIntakeMotor(Constantes.kSpeedIntakeHold);
            } else {
                salidas.setIntakeMotor(Constantes.kSpeedIntakeStop);
            }
        }
        double operatorPOVAngle = entradas.getSecondaryJoyPOVAngle();
        if (operatorPOVAngle == -1) {
            /**
             * If no setpoints modifications are requested, allow manual control.
             * If manual control is requested, deactivate setpoint protection.
             * Else, if no setpoint is set, allow stopping of motors. If a setpoint is set, stopping is not allowed.
             */ 
            if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonA)) {
                this.armSetpointSet = false;
                salidas.setArmSpeed(Constantes.kSpeedArmDown);
            } else if (entradas.getPrimaryJoyButton(Constantes.kJoystickButtonY)) {
                this.armSetpointSet = false;
                salidas.setArmSpeed(Constantes.kSpeedArmUp);
            } else {
                if (!this.armSetpointSet) {
                    salidas.setArmSpeed(Constantes.kSpeedArmStop);
                }
            }
        } else {
            /**
             * If a setpoint modification is requested, enable setpoint protection and set setpoint.
             */
            this.armSetpointSet = true;
            if (operatorPOVAngle == 0) {
                salidas.setArmPosition(Constantes.kArmHardStop);
            } else if (operatorPOVAngle == 45) {
                salidas.setArmPosition(Constantes.kArmCargoFront);
            } else if (operatorPOVAngle == 90) {
                salidas.setArmPosition(Constantes.kArmRocketLevelOne);
            } else if (operatorPOVAngle == 180) {
                salidas.setArmPosition(Constantes.kArmFloor);
            }
        }
        
    }

	@Override
	public void stop(RobotInput entradas, RobotOutput salidas) {
		
	}
}