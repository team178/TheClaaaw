package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private Joystick driver = new Joystick(0);
	private Joystick aux = new Joystick(1);
	
	//Get the NeworkTable for the Robot
	public final NetworkTable networktable = NetworkTable.getTable("Vision");;

	public static Robot instance;
	
	//objects need to be instanizated in order to use them for autonomous
	private DriveTrain driveTrain= new DriveTrain (
			new Talon(0), //frontLeft
			new Talon(1), //backLeft
			new Talon(2), //frontRight
			new Talon(3), //backRight
			new Gyro(0) //gyro
	);
	private DipSwitches dipSwitches= new DipSwitches(
			new DigitalInput(16), //rightSwitch
			new DigitalInput(17) //leftSwitch
			);
	private Claw claw = new Claw (
			new Talon(6), //leftClaw
			new Talon(7), //rightClaw
			new DigitalInput(4), //toteTouchingLS
			new DigitalInput(5), //leftFrontLS
			new DigitalInput(6), //rightFrontLS
			new DigitalInput(7), //leftBackLS
			new DigitalInput(8) //rightBackLS
	);
	private Lift lift = new Lift(
			new Talon(4) ,  //motor
			new DigitalInput(3), //bottomLimit/zeroLimit
			new Encoder(14, 15) //Encoder 
	);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	private RunningComponent[] components = { 
			driveTrain,
			claw, 
			lift,
			dipSwitches,
			new UltraSonics(
					new AnalogInput(1)), //ultrasonics
			
			//new Camera("cam0"), //cam name on roborio
			
			new TapeShooter(
					new Talon(8))
			
		};
	@Override
	public void robotInit() {
		Robot.instance = this;
	}
	
	public static boolean transportCan=false;
	public static boolean transportTote=false;
	public static boolean isGripped =true; //will change initial condition once we get multiple autonomous code/Dip switches working
	public static boolean liftCan = false;
	/**
	 * This function is called periodically during autonomous
	 */
	
	public static final Timer timer = new Timer();
	public void autonomousInit() {
		
		timer.start();
		timer.reset();
		driveTrain.resetGyro(); //get gyro ready
		if(dipSwitches.turnBackwardsAndPickUp())
			autoPhase = 0;
	}
	
	private int autoPhase = 0;
	
	@Override
	public void autonomousPeriodic() {
		switch(autoPhase){
		case 0: //close the claw
			claw.moveClaw(claw.closing, false);
			descendIfReady(4);
			break;
		case 1:			
			claw.moveClaw(Claw.DIRECTION_STOP, false); //stop the claw
			lift.moveMotor(Lift.DIRECTION_UP); //make lift go up
			descendIfReady(1.5);
			break;
		case 2:
			lift.moveMotor(Lift.DIRECTION_STOP); //stop lift
			driveTrain.PIDdrive(0, 0, -90); //turn with PID
			descendIfReady(2);
			break;
		case 3:
			driveTrain.PIDdrive(0, 0.8, -90); //go backwards
			descendIfReady(1);
			break;
		case 4:
			driveTrain.PIDdrive(0, 0.4, -90); //go back, but slow like
			descendIfReady(1);
			break;
		default:
			break; //do nothing				
		}
		
	}

	private void descendIfReady(double d) {
		if(timer.get() > d){
			autoPhase++;
			timer.reset();
		}
	}
	
	@Override
	public void teleopInit(){
		networktable.putNumber("TO_RUN_OR_NOT_TO_RUN", 0);
	}
	
	@Override
	public void teleopPeriodic() {
		for (int i = 0; i < components.length; i++) {
			components[i].teleop(driver, aux);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		for (int i = 0; i < components.length; i++) {
			components[i].test(driver);
		}
	}

  
}
