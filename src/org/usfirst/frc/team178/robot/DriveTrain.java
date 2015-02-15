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
	
	private Gyro gyroDevice;

	private double gyroPin;

	private double controlAngle;
	private PIDSource gyro = new PIDSource() {

		@Override
		public double pidGet() {
			SmartDashboard.putNumber("joyAngle", controlAngle);
			return (gyroDevice.getAngle() - controlAngle - gyroPin) / 360;
		}
	};
	
	private PIDOutput gyroCorr = new PIDOutput() {
		
		@Override
		public void pidWrite(double output) {
			angleCorrection = output;
		}
	};

	private double angleCorrection = 0d;
	private PIDController pid = new PIDController(Math.PI, .01, 0, gyro, gyroCorr);
	
	public DriveTrain(Talon frontLeft, Talon backLeft, Talon frontRight,
			Talon backRight, Gyro gyroDevice) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.gyroDevice = gyroDevice;
		
		pid.enable();
		//accPID.enable();
	}
//x = 3, y = 4
	@Override
	public void teleop(Joystick driver, Joystick aux) {
		
		double yValue = driver.getY();
		double xValue = driver.getX();
		if(aux.getRawButton(4))
			controlAngle = 0;
		else
			controlAngle = Math.atan2(aux.getRawAxis(5), aux.getRawAxis(4)) * 180/Math.PI;
		double speed = 1-driver.getRawAxis(3);

		
		if (driver.getRawButton(2)) {
			speed*=0.5;
		}
		
		xValue*=speed;
		yValue*=speed;
		
		if (driver.getRawButton(11)) {
			yValue*=0;
		}
		else if (driver.getRawButton(12))
		{
			xValue*=0;
		}
		
		if (driver.getRawButton(8)) {
			gyroPin = gyro.pidGet() * 360;
		}
		
		if (driver.getRawButton(9)){
			frontLeft.set(1);
			frontRight.set(1);
			backRight.set(1);
			backLeft.set(1);
		} else {
			drive(xValue,yValue);
		}
	}

	@Override
	public void auto(Timer autoTime) {
		controlAngle = ((autoTime.get() * 360 * .25)%360)-180;
		drive(0, 0);
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}
	
	private double lastMag;
	private PIDSource accPIDsrc = new PIDSource() {
		@Override
		public double pidGet() {
			// TODO Auto-generated method stub
			return lastMag;
		}
	};
	
	private double nowScale = 1;
	private PIDOutput accPIDout = new PIDOutput() {
		
		@Override
		public void pidWrite(double output) {
			nowScale = output;
		}
	};
	
	@SuppressWarnings("unused")
	private PIDController accPID = new PIDController(.5, 0, 0, accPIDsrc, accPIDout);

	public void drive(double xValue, double yValue) {
		double twistValue = -angleCorrection;
		double theta = -(gyroDevice.getAngle() % 360 ) * Math.PI / 180;
		double xPrime = xValue * Math.cos(theta) - yValue * Math.sin(theta);
		double yPrime = xValue * Math.sin(theta) + yValue * Math.cos(theta);
		
		lastMag = Math.sqrt(Math.abs(xPrime) + Math.abs(yPrime)); //lastMag -> nowScale
		xPrime *= nowScale;
		yPrime *= nowScale;
		
		SmartDashboard.putNumber("Magnitude", lastMag);
		
		//xPrime = 0;
		
		// alternate scheme
		//double xPrime = Math.cos(theta+2*Math.PI/4);
		//double yPrime = Math.sin(theta);
		double normal = 1 / Math.max( // get maximum possible value
			Math.max( 
					Math.abs(-(yPrime - xPrime + twistValue)),
					Math.abs((yPrime + xPrime - twistValue))
			),
			Math.max(
					Math.abs(-(yPrime + xPrime + twistValue)),
					Math.abs((yPrime - xPrime - twistValue))
			)
		); 
//		double normal = 1 / Math.max(Math.abs(yPrime), Math.abs(xPrime));
		if(normal == Double.NaN) normal = 1;
		normal = Math.min(1, normal); //limit normal to 1 so magnitude is not crazy
		
		normal = 1;
		
		SmartDashboard.putNumber("normal", normal);
		
		SmartDashboard.putNumber("frontLeft", (yPrime - xPrime) * normal);
		SmartDashboard.putNumber("frontRight", -(yPrime + xPrime) * normal);
		SmartDashboard.putNumber("backLeft",  (yPrime + xPrime) * normal);
		SmartDashboard.putNumber("backRight", -(yPrime - xPrime) * normal);
		
		SmartDashboard.putNumber("gyro", gyroDevice.getAngle());
		SmartDashboard.putNumber("theta", theta);
		
		
		// multiply all sets by ratio
		frontLeft.set(  -(yPrime - xPrime + twistValue) * normal);  
		frontRight.set(  (yPrime + xPrime - twistValue) * normal);
		backLeft.set(   -(yPrime + xPrime + twistValue) * normal);  
		backRight.set(   (yPrime - xPrime - twistValue) * normal); 
		
		
		
	}

	
}
