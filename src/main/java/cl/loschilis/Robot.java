package cl.loschilis;

import cl.loschilis.util.Scheduler;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import cl.loschilis.subsystem.Chasis;
import cl.loschilis.subsystem.HPSystem;
import cl.loschilis.io.RobotOutput;
import cl.loschilis.io.RobotInput;
import cl.loschilis.subsystem.Cargosystem;
import cl.loschilis.util.MQTTReporterManager;
import cl.loschilis.util.MQTTReporterManager.MQTTTransmitRate;

 
public class Robot extends TimedRobot {
    private RobotInput entradas;
    private RobotOutput salidas;
    private Scheduler scheduler;

    private Chasis chassis;
    private Cargosystem Cargo;
    private HPSystem hatchPanelIntake;

    private DriverStation dsinfo;
    private MQTTReporterManager mqttLogger;

    @Override
    public void robotInit() {
        //Inputs
        entradas = RobotInput.getInstance();
        salidas = RobotOutput.getInstance();
        //Sistemas
        chassis = Chasis.getInstance();
        Cargo = Cargosystem.getInstance();
        hatchPanelIntake = HPSystem.getInstance();

        scheduler = Scheduler.getInstance();
        scheduler.addSubsystem(chassis);
        scheduler.addSubsystem(hatchPanelIntake);
        scheduler.addSubsystem(Cargo);

        dsinfo = DriverStation.getInstance();
        mqttLogger = MQTTReporterManager.getInstance();
        mqttLogger.addValue(()->(RobotController.getBatteryVoltage()), "webui/battery/voltage", MQTTTransmitRate.SLOW);
        mqttLogger.addValue(()->(dsinfo.getMatchTime()), "webui/driverstation/matchtime", MQTTTransmitRate.SLOW);
    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopPeriodic() {
        scheduler.update(entradas, salidas);
    }

    @Override
    public void testPeriodic() {

    }
}
