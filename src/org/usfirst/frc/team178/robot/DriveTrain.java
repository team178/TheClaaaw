package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain implements RunningComponent {
	private Talon frontLeft;
	private Talon backLeft;
	private Talon frontRight;
	private Talon backRight;
	
	//TODO find some way to abstract joystick from this freaking embedded class
	private Joystick joystick;
	
	//for Gyro
	private Gyro gyroDevice;
	private double angleCorrection = 0d;
	
	//for Autonomous
	protected static final double BACK_DRIVE_TIME = 1; //seconds
	static final double TWIST_MULITPLER = .6;
	
	
	private PIDSource gyro = new PIDSource() {
		@Override
		public double pidGet() {
			double joyAngle = joystick.getTwist()* 360;
			return gyroDevice.getAngle() - joyAngle;
		}
	};
	
	private PIDOutput gyroCorr = new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			angleCorrection = output;
		}
	};
	
	@SuppressWarnings("unused")
	private PIDController pid = new PIDController(0.1, 0.001, 0, gyro, gyroCorr);
	
	
	public DriveTrain(Talon frontLeft, Talon backLeft, Talon frontRight,
			Talon backRight, Gyro gyroDevice) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.gyroDevice = gyroDevice;

	}
		


	@Override
	public void teleop(Joystick joystick, Joystick aux) {
		this.joystick = joystick;

		double percentSpeed = (1 - joystick.getRawAxis(3)) / 2;// convert from scale of -1 to 1 to 0 to 1 and invert
		double speed = percentSpeed/2 + .3; // convert to scale .3 to .8

		if (joystick.getRawButton(2)) {
			speed *= 0.5;
		}

		double yValue = joystick.getY() * speed;
		double xValue = joystick.getX() * speed;
		double twistValue= joystick.getTwist() * speed;
		
		if (joystick.getRawButton(9)) { //snap-to-axis code
			yValue*=0;
			twistValue*=0;
			xValue = -.6;
		}
		else if (joystick.getRawButton(10)){
			yValue*=0;
			twistValue*=0;
			xValue = .6;
		}
		
		if (joystick.getRawButton(3)){
			twistValue += angleCorrection;
		}
	
		if (joystick.getRawButton(4)){
			gyroDevice.reset();
		}
		
		drive(xValue,yValue,twistValue);
	}

	@Override
	public void test(Joystick driver) {
		
	}
	
	/**drive(double xValue, double yValue, double twistValue) */
	public void drive(double xValue, double yValue, double twistValue) {
		frontLeft.set(  - (yValue - xValue - twistValue));
		frontRight.set((yValue + xValue + twistValue));
		backLeft.set(   -( yValue + xValue - twistValue));
		backRight.set( (yValue - xValue + twistValue));
	}

	
}
