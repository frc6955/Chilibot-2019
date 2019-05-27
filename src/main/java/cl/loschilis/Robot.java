package cl.loschilis;

import cl.loschilis.util.Scheduler;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import cl.loschilis.subsystem.Chassis;
import cl.loschilis.subsystem.HPSystem;
import cl.loschilis.io.RobotOutput;


import cl.loschilis.io.RobotInput;
import cl.loschilis.subsystem.CargoSystem;
import cl.loschilis.util.MQTTReporterManager;
import cl.loschilis.util.MQTTReporterManager.MQTTTransmitRate;

 
public class Robot extends TimedRobot {
    private RobotInput entradas;
    private RobotOutput salidas;
    private Scheduler scheduler;
    private MQTTReporterManager mqttLogger;

    private Chassis chassis;
    private CargoSystem cargo;
    private HPSystem hatchPanelIntake;
    
    @Override
    public void robotInit() {
        // I/O. OUTPUT MUST BE DECLARED BEFORE INPUT
        salidas = RobotOutput.getInstance();
        entradas = RobotInput.getInstance();
        // Subsistemas
        chassis = Chassis.getInstance();
        cargo = CargoSystem.getInstance();
        hatchPanelIntake = HPSystem.getInstance();
        // Add subsystems to scheduler for updating
        scheduler = Scheduler.getInstance();
        scheduler.addSubsystem(chassis);
        scheduler.addSubsystem(hatchPanelIntake);
        scheduler.addSubsystem(cargo);
        // Configure MQTTLogger reportables
        mqttLogger = MQTTReporterManager.getInstance();
        mqttLogger.addValue(()->(RobotController.getBatteryVoltage()), "webui/battery/voltage", MQTTTransmitRate.SLOW);
        mqttLogger.addValue(()->(DriverStation.getInstance().getMatchTime()), "webui/driverstation/matchtime", MQTTTransmitRate.SLOW);
        mqttLogger.addValue(()->(entradas.getGyroAngle()), "webui/sensors/gyro", MQTTTransmitRate.FAST);
        mqttLogger.addValue(()->(entradas.getAllCurrents()), "webui/pdp/all", MQTTTransmitRate.FAST);
        mqttLogger.addValue(()->(entradas.getArmAngle()), "webui/sensors/arm", MQTTTransmitRate.FAST);
        mqttLogger.addValue(()->(entradas.getBallAdquisition()), "webui/sensors/ball", MQTTTransmitRate.SLOW);
    }

    public void initializationRobotRoutine() {
        salidas.homeArm();
        salidas.setHPIntakeFinger(Value.kForward);
    }

    public void periodicRobotRoutine() {
        scheduler.update(entradas, salidas);
    }

    public void disablingRobotRoutine() {

    }



    // WPILib code
    @Override
    public void autonomousInit() {
        initializationRobotRoutine();
    }

    @Override
    public void autonomousPeriodic() {
        periodicRobotRoutine();
    }

    @Override
    public void teleopInit() {
        // TODO: Check if arm is already homed to avoid re-homing when starting teleop
        initializationRobotRoutine();
    }

    @Override
    public void teleopPeriodic() {
        periodicRobotRoutine();
    }

    @Override
    public void disabledInit() {
        disablingRobotRoutine();
    }
}
