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
	
	//for Gyro
	private Gyro gyroDevice;
	private double angleCorrection = 0d;
	protected double PIDangleToCorrectTo;
	
	//for Autonomous
	protected static final double BACK_DRIVE_TIME = 1; //seconds
	static final double TWIST_MULITPLER = .6;
	
	public void resetGyro() {
		gyroDevice.reset();
	}
	
	private PIDSource gyro = new PIDSource() {
		@Override
		public double pidGet() {
			return (gyroDevice.getAngle() - PIDangleToCorrectTo)  ;
		}
	};
	
	private PIDOutput gyroCorr = new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			angleCorrection = output;
		}
	};
	
	private PIDController pid = new PIDController(0.01, 0.001, 0, gyro, gyroCorr);
	{
		SmartDashboard.putNumber("P", pid.getP());
		SmartDashboard.putNumber("I", pid.getI());
		SmartDashboard.putNumber("D", pid.getD());
	}
	
	public DriveTrain(Talon frontLeft, Talon backLeft, Talon frontRight,
			Talon backRight, Gyro gyroDevice) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.gyroDevice = gyroDevice;
		
		pid.enable();
	}
		


	@Override
	public void teleop(Joystick joystick, Joystick aux) {

		double percentSpeed = (1 - joystick.getRawAxis(3)) / 2;// convert from scale of -1 to 1 to 0 to 1 and invert
		double speed = percentSpeed/2 + .3; // convert to scale .3 to .8

		if (joystick.getRawButton(2)) {
			speed *= 0.5;
		}

		SmartDashboard.putNumber("Gyro Angle", gyroDevice.getAngle());
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
		if (driver.getRawButton(1)){
			PIDdrive(0,0,90);
		}
		if (driver.getRawButton(2)){
			gyroDevice.reset();
		}
		if (driver.getRawButton(3)){
			pid.setPID(SmartDashboard.getNumber("P"), SmartDashboard.getNumber("I"), SmartDashboard.getNumber("D"));
		}
	}
	
	/**drive(double xValue, double yValue, double twistValue) */
	public void drive(double xValue, double yValue, double twistValue) {
		frontLeft.set(  - (yValue - xValue - twistValue));
		frontRight.set((yValue + xValue + twistValue));
		backLeft.set(   -( yValue + xValue - twistValue));
		backRight.set( (yValue - xValue + twistValue));
	}
	public void PIDdrive(double xValue, double yValue, double angle) {
		PIDangleToCorrectTo = angle;
		double twistValue = angleCorrection;
		frontLeft.set(  - (yValue - xValue - twistValue));
		frontRight.set((yValue + xValue + twistValue));
		backLeft.set(   -( yValue + xValue - twistValue));
		backRight.set( (yValue - xValue + twistValue));
	}
	
}
