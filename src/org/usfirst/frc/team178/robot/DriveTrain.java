package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;

public class DriveTrain implements RunningComponent {
	private Talon frontLeft;
	private Talon backLeft;
	private Talon frontRight;
	private Talon backRight;
	
	private Joystick joystick;
	
	private Gyro gyroDevice;
	
	private double angleCorrection = 0d;
	

	
	private PIDSource gyro = new PIDSource() {
		@Override
		public double pidGet() {
			double joyAngle = joystick.getTwist() * 360;
			System.out.println("Gyro angle: "+gyroDevice.getAngle());
			return gyroDevice.getAngle() - joyAngle;
		}
	};
	
	private PIDOutput gyroCorr = new PIDOutput() {
		
		@Override
		public void pidWrite(double output) {
			angleCorrection = output;
		}
	};
	
	private PIDController pid = new PIDController(.02, 0.00001, 0, gyro, gyroCorr);
	
	public DriveTrain(Talon frontLeft, Talon backLeft, Talon frontRight,
			Talon backRight, Joystick joystick, Gyro gyroDevice) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.joystick = joystick;
		this.gyroDevice = gyroDevice;
		
		pid.enable();
	}

	@Override
	public void teleop() {
		
		double yValue = joystick.getY();
		double xValue = joystick.getX();
		double twistValue = joystick.getTwist();
		
		double speed = 1-joystick.getRawAxis(3);

		
		if (joystick.getRawButton(2)) {
			speed*=0.5;
		}
		
		xValue*=speed;
		yValue*=speed;
		twistValue*=speed;
		
		if (joystick.getRawButton(11)) {
			yValue*=0;
			twistValue*=0;
		}
		else if (joystick.getRawButton(12))
		{
			xValue*=0;
			twistValue*=0;
		}
		
		// Slowdown twisting --Bald
		twistValue = twistValue * 0.7;
		
		drive(xValue,yValue,twistValue);
	}

	@Override
	public void auto() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}
	

	public void drive(double xValue, double yValue, double twistValue) {

		twistValue += angleCorrection;
		double theta = gyroDevice.getAngle() * 2 * Math.PI;
		double xPrime = xValue * Math.cos(theta) - yValue * Math.sin(theta);
		double yPrime = xValue * Math.cos(theta) + yValue * Math.cos(theta);
		
		// alternate scheme
		//double xPrime = Math.cos(theta+2*Math.PI/4);
		//double yPrime = Math.sin(theta);
	

		double normal = Math.abs(xPrime)+Math.abs(yPrime); // get maximum possible value
		normal = 1/normal;  // get normal ratio so maximum value*ratio is equal to, 1
		
		// multiply all sets by ratio
		frontLeft.set(   (yPrime - xPrime)*normal );
		frontRight.set(-(yPrime + xPrime )*normal);
		backLeft.set(    (yPrime + xPrime )*normal);
		backRight.set( -(yPrime - xPrime )*normal);
		
		
		
	}

	
}
