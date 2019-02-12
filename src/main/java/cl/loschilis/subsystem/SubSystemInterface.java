package cl.loschilis.subsystem;

import cl.loschilis.io.RobotInput;
import cl.loschilis.io.RobotOutput;

public interface SubSystemInterface {
    void update(RobotInput entradas, RobotOutput salidas);
}