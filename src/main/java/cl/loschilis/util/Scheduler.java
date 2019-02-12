package cl.loschilis.util;

import java.util.Vector;

import cl.loschilis.io.RobotOutput;
import cl.loschilis.io.RobotInput;
import cl.loschilis.subsystem.SubSystemInterface;;

public class Scheduler {
    private Vector <SubSystemInterface> listsubs;
    private static Scheduler instance;

    public static Scheduler getInstance() {
        if (instance == null) {
            instance = new Scheduler();
        }
        return instance;
    }

    private Scheduler(){
        this.listsubs = new Vector<SubSystemInterface>();
    }

    public void addSubsystem(SubSystemInterface sub){
        this.listsubs.add(sub);
    }

    public void update(RobotInput entradas, RobotOutput salidas){
        for(SubSystemInterface subsystem : listsubs){
            subsystem.update(entradas, salidas);
        }
    }
}

