package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;

import com.ctre.phoenix.motorcontrol.ControlMode;

import cl.loschilis.Constantes;


public class Cargosystem implements SubSystemInterface {

    private static Cargosystem instance;

    public static Cargosystem getInstance() {
        if (instance == null) {
            instance = new Cargosystem();
        }
        return instance;
    }

    private void rollerIn(RobotOutput robotOutput) {
        robotOutput.setRollerMotor(Constantes.kSpeedIntakeIn);
    }

    private void rollerOut(RobotOutput robotOutput) {
        robotOutput.setRollerMotor(Constantes.kSpeedIntakeOut);
    }
    
    private void rollerStop(RobotOutput robotOutput) {
        robotOutput.setRollerMotor(Constantes.kSpeedIntakeStop);
    }

    private void rollerUltraSonic(RobotOutput robotOutput) {
        robotOutput.setRollerMotor(Constantes.kSpeedUltraSonic);
    }

    private void armUp(RobotOutput robotOutput) {
        robotOutput.setArmMotor(Constantes.kSpeedArmUp);
    }

    private void armDown(RobotOutput robotOutput) {
        robotOutput.setArmMotor(Constantes.kSpeedArmDown);
    }
    private void armStop(RobotOutput robotOutput) {
        robotOutput.setArmMotor(Constantes.kSpeedArmStop);
    }

    @Override
    public void update(RobotInput entradas, RobotOutput salidas) {
        if(entradas.driverButton(Constantes.kJoystickButtonLB)) {
            this.rollerOut(salidas);
        } else if(entradas.driverButton(Constantes.kJoystickButtonRB)) {
            this.rollerIn(salidas);
        } else {
            if(entradas.analogInputCount() < 40){
                this.rollerStop(salidas);
            } else {
                this.rollerUltraSonic(salidas);
            }
        }
        if (entradas.driverPOV() == -1) {
            if (entradas.driverButton(Constantes.kJoystickButtonA)) {
                this.armDown(salidas);
            } else if (entradas.driverButton(Constantes.kJoystickButtonY)) {
                this.armUp(salidas);
            } else {
                this.armStop(salidas);
            }
        } else {
            if (entradas.driverPOV() == 0) {
                salidas.armMaster.set(ControlMode.MotionMagic, Constantes.kArmHardStop);
            } else if (entradas.driverPOV() == 90) {
                salidas.armMaster.set(ControlMode.MotionMagic, Constantes.kArmRocket);
            } else if (entradas.driverPOV() == 180) {
                salidas.armMaster.set(ControlMode.MotionMagic, Constantes.kArmFloor);
            }
        }
        
    }
}