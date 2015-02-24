package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	private RunningComponent[] components = {
			new DriveTrain(
					new Victor(0), //frontLeft
					new Victor(1), //backLeft
					new Victor(2), //frontRight
					new Victor(3), //backRight
					new Gyro(0)), //gyro
					
			new Claw (
					new Victor(6), //leftClaw
					new Victor(7), //rightClaw
					new DigitalInput(4), //toteTouchingLS
					new DigitalInput(5), //leftFrontLS
					new DigitalInput(6), //rightFrontLS
					new DigitalInput(7), //leftBackLS
					new DigitalInput(8) //rightBackLS
					), 

			new Lift(
					new Victor(4) ,  //motor
					new DigitalInput(3), //bottomLimit
					new Encoder(14, 15)), //Encoder 
			
			new Deck(
					new DigitalInput(0), //frontLimit
					new DigitalInput(1), //backLimit
					new Victor(5)), //motor
			new Camera(), 
			
			new UltraSonics(
					new AnalogInput(1)) //ultrasonics
		};

	@Override
	public void robotInit() {
	};
	

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		for (int i = 0; i < components.length; i++) {
			components[i].auto();
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
