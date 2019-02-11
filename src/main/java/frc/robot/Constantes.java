package frc.robot;
 
public class Constantes {
    ///Solenoides
    public static final int kSolenoideHPIntakeIn = 0;
    public static final int kSolenoideHPIntakeOut = 1;
    public static final int kSolenoideHPReelIn = 2;
    public static final int kSolenoideHPReelOut = 3 ;
    ///Chasis
    public static final int kChassisLeft = 0;
    public static final int kChassisRight = 1;
    ///Joysticks
    public static final int kDriver = 0;
    public static final int kOperator = 1;
    //Buttons
    public static final int kButtonA = 1;
    public static final int kButtonB = 2;
    public static final int kButtonX = 3;
    public static final int kButtonY = 4;
    public static final int kButtonLB = 5;
    public static final int kButtonRB = 6;
    //Axis
    public static final int KAxisY_L = 1;
    public static final int KAxisX_R = 4;
    ///CAN
        public static final int kPdp = 39;
        //Brazo
        public static final int KTalonL = 11;
        public static final int KTalonR = 12;
        //Intake
        public static final int KMotorLeft = 9;
        public static final int kMotorRight = 10;
    ///Camara
    public static final int kFront = 0;
    public static final int kBack = 1;
    public static final int kFPS = 15;
    public static final int kWidth = 320;
    public static final int kHeight = 240;
    ///Cargo
    public static final double kCargoIn = -0.6;
    public static final double kCargoOut = 0.8;
    public static final double kCargoStop = 0.0;
    public static final double KCargoDown = -0.2;
    public static final double KCargoUp = 0.2;
    //Vision
    public static final double KCenter = 160;
    public static final double KRotateR = 0.2;
    public static final double KRotateL = -0.2;
    public static final double KO = 0;
	public static final double kVisionAlignKp = 1/(Constantes.kWidth / 2);

}