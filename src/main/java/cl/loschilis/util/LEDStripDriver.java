package cl.loschilis.util;

import cl.loschilis.Constantes;
import edu.wpi.first.wpilibj.PWM;

public class LEDStripDriver {

    private static LEDStripDriver instance;
    private PWM forwardRedChannel, forwardBlueChannel;
    private PWM backwardRedChannel, backwardBlueChannel;

    public static LEDStripDriver getInstance() {
        if (instance == null) {
            instance = new LEDStripDriver();
        }
        return instance;
    }

    private LEDStripDriver() {
        this.forwardRedChannel = new PWM(Constantes.kLEDPWMFrontRedChannel);
        this.forwardBlueChannel = new PWM(Constantes.kLEDPWMFrontBlueChannel);
        this.backwardRedChannel = new PWM(Constantes.kLEDPWMRearRedChannel);
        this.backwardBlueChannel = new PWM(Constantes.kLEDPWMRearBlueChannel);
    }

}