package frc.robot;

import java.util.Vector;
import frc.robot.subsystem.Subsystem;

public class Scheduler {
private Vector <Subsystem> listsubs;
private static Scheduler instance;

public static Scheduler getInstance() {
    if (instance == null) {
        instance = new Scheduler();
    }
    return instance;
}
private Scheduler(){
    listsubs = new Vector<Subsystem>();
}
public void addSubsystem(Subsystem sub){
   this.listsubs.add(sub);
}

public void update(RobotIO entradas){
  for(Subsystem subsystem : listsubs ){
        subsystem.update(entradas);
  }
}

}

