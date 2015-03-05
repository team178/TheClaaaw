package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

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

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	private RunningComponent[] components = { 
			new DriveTrain(
					new Talon(0), //frontLeft
					new Talon(1), //backLeft
					new Talon(2), //frontRight
					new Talon(3), //backRight
					new Gyro(0)), //gyro
					
			new Claw (
					new Talon(6), //leftClaw
					new Talon(7), //rightClaw
					new DigitalInput(4), //toteTouchingLS
					new DigitalInput(5), //leftFrontLS
					new DigitalInput(6), //rightFrontLS
					new DigitalInput(7), //leftBackLS
					new DigitalInput(8) //rightBackLS
					), 

			new Lift(
					new Talon(4) ,  //motor
					new DigitalInput(3), //bottomLimit/zeroLimit
					new Encoder(14, 15)), //Encoder 
			/*new Deck(
					new DigitalInput(1), //frontLimit
					new DigitalInput(0), //backLimit
					new Talon(5)), //motor
			*/
			new UltraSonics(
					new AnalogInput(1)), //ultrasonics
			
			new Camera("cam0"), //cam name on roborio
			
			new TapeShooter(
					new Talon(8))
			
		};
	@Override
	public void robotInit() {
		Robot.instance = this;
	}
	
	public static boolean transportCan=false;
	public static boolean transportTote=false;
	public static boolean takeCan=false; //using our tapeShooter, from the middle
	
	@Override
	public void autonomousInit() {
		// TODO Auto-generated method stub
		super.autonomousInit();
		ActionHelper.resetAllCompletionFlags();
	}
	
	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		for (ActionHelper actionHelper : ActionHelper.actions) {
			actionHelper.run();
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
