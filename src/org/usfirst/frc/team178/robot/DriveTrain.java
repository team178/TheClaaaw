package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain implements RunningComponent {
	private Talon frontLeft;
	private Talon backLeft;
	private Talon frontRight;
	private Talon backRight;
	
	//for Gyro
	private Gyro gyroDevice;
	private double angleCorrection = 0d;
	protected double PIDangleToCorrectTo; // setpoint may be a better name --Brandon

	//for Autonomous
	protected static final double BACK_DRIVE_TIME = 1; //seconds
	static final double TWIST_MULITPLER = .6;

	public void resetGyro() {
		gyroDevice.reset();
	}
	public double getGyroAngle(){
		return gyroDevice.getAngle();
	}
	
	private PIDSource gyro = new PIDSource() {
		@Override
		public double pidGet() {
			return (gyroDevice.getAngle() - PIDangleToCorrectTo) / 360  ;
		}
	};
	
	private PIDOutput gyroCorr = new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			angleCorrection = output;
		}
	};
	
	private PIDController pid = new PIDController(3.1, 0, 0, gyro, gyroCorr);
	
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

		double percentSpeed = (1 - joystick.getRawAxis(3)) / 2; // convert from scale of -1 to 1 to 0 to 1 and invert
		double speed = percentSpeed * .5 /* width of range */ + .3 /* offset */; // convert to scale .3 to .8

		if (joystick.getRawButton(2)) {
			speed *= 0.5;
		}

		SmartDashboard.putNumber("Gyro Angle", gyroDevice.getAngle());
		double yValue = joystick.getY() * speed;
		double xValue = joystick.getX() * speed;
		double twistValue= joystick.getTwist() * speed;
		
		//push-to-strafe (fixed speed) code
		if (joystick.getRawButton(9)) { 
			yValue*=0;
			twistValue*=0;
			xValue = -.6;
		} else if (joystick.getRawButton(10)) {
			yValue*=0;
			twistValue*=0;
			xValue = .6;
		}
		
		//snap-to-axis code
		else if(joystick.getRawButton(11)) {
			twistValue*=0;
			if(Math.abs(xValue)>=Math.abs(yValue))
				yValue*=0;
			else // |yValue| > |xValue|
				xValue*=0;
		}
		

		if (joystick.getRawButton(4)) {
			gyroDevice.reset();
			PIDangleToCorrectTo = gyroDevice.getAngle();
		}
		
		if(joystick.getRawButton(5)){
			drive(0, -Math.min((UltraSonics.scaledDistanceFromTote)-1,.5), 0);
		} else if(!oneEighty.tick(joystick, xValue, yValue)) {
			if (joystick.getRawButton(3)) {
				PIDdrive(xValue, yValue, PIDangleToCorrectTo);
			} else 
				drive(xValue, yValue, twistValue);
		}
		
	}
	
	private OnceRunner oneEighty = new OnceRunner(){
		@Override
		public boolean canRun(Object[] objects) {
			// TODO Auto-generated method stub
			return ((Joystick)objects[0]).getRawButton(6);
		}

		@Override
		public void once(Object[] objects) {
			gyroDevice.reset();
			PIDangleToCorrectTo = 180 - gyroDevice.getAngle();
		}

		@Override
		public void then(Object[] objects) {
			PIDdrive((double)objects[1], (double)objects[2], PIDangleToCorrectTo);
		}

		@Override
		public void end(Object[] objects) {}
	};
	
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
	public void PIDdrive(double xValue, double yValue, double angle, double speed) {
		PIDangleToCorrectTo = angle;
		double twistValue = angleCorrection;
		frontLeft.set(  - (yValue - xValue - twistValue) * speed);
		frontRight.set((yValue + xValue + twistValue) * speed);
		backLeft.set(   -( yValue + xValue - twistValue) * speed);
		backRight.set( (yValue - xValue + twistValue) * speed);
	}
	public void PIDdrive(double xValue, double yValue, double angle) {
		PIDdrive(xValue, yValue, angle, 1);
	}
	
}
