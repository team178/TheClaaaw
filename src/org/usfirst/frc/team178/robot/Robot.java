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

	
	//Get the NeworkTable for the Robot
	public static NetworkTable networktable;

	public static Robot instance;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	public Robot() {
		Robot.instance = this;
	}
	
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
					), //joystick

			new Lift(
					new Talon(4) ,  //motor
					new DigitalInput(3), //bottomLimit
					new Encoder(14, 15)), //Encoder 
			new Deck(
					new DigitalInput(0), //frontLimit
					new DigitalInput(1), //backLimit
					new Talon(5)), //motor
					
			new Camera(), //joystick
			
			new UltraSonics(
					new AnalogInput(1)) //ultrasonics
		};

	@Override
	public void robotInit() {
			networktable= NetworkTable.getTable("Vision");
			networktable.putInt("TO_RUN_OR_NOT_TO_RUN", 1);
	}
	
	@Override
	public void autonomousInit() {
		// TODO Auto-generated method stub
		super.autonomousInit();
		ActionHelper.resetAllCompletionFlags();
	}
	
	@Override
	public void teleopInit(){
		networktable.putInt("TO_RUN_OR_NOT_TO_RUN", 0);
		Message.inAuto=false;
		Message.isCanHeld=false;
		Message.isCaninAZB=false;
		Message.isCaninAZA=false;
		Message.isBotClearofAZ=false;
		Message.isBotAlligned=false;
		Message.isBotReadyToGrab=false;
		Message.isToteHeld=false;
		Message.isToteinAZ=false;
		Message.isCanReleased=false;
		Message.isBotMovedBack=false;
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

	/**
	 * This function is called periodically during operator control
	 */
	private Joystick driver = new Joystick(0);
	private Joystick aux = new Joystick(1);
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
			components[i].test();
		}
	}

  
}
