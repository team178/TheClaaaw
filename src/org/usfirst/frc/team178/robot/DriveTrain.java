package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class DriveTrain implements RunningComponent {
	private Talon frontLeft;
	private Talon frontRight;
	private Talon backLeft;
	private Talon backRight;
	
	private Joystick joystick;

	public DriveTrain(Talon frontLeft, Talon frontRight, Talon backLeft,
			Talon backRight, Joystick joystick) {
		super();
		this.frontLeft = frontLeft;
		this.frontRight = frontRight;
		this.backLeft = backLeft;
		this.backRight = backRight;
		this.joystick = joystick;
	}
	
	@Override
	public void teleop() {
		double yValue = joystick.getY();
		double xValue = joystick.getX();
		double twistValue = joystick.getTwist();
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
		if (joystick.getRawButton(3) || joystick.getRawButton(11)) {
			strafeLeft();
			return;
		} else if (joystick.getRawButton(4) || joystick.getRawButton(12)) {
			strafeRight();
			return;
		}
		
		if(joystick.getRawButton(2)){  //cut the input values in half for greater precision (Brandon Bald)
			xValue=xValue/2.0;
			yValue=yValue/2.0;
			twistValue= twistValue/2.0;
		}
		
		// Logitech Extreme 3D Pro joysticks seem to have a huge 0.3 Z axis
		// deadzone. - Aneesh & Brandon
		
		double oldTwist = twistValue;
		
		if (twistValue > -0.4 && twistValue < 0.35) {
			twistValue = 0;
		} else if (twistValue >= 0.35) {
			twistValue = twistValue - 0.35;
			// Apply an expansion factor. So all the way right becomes 1.0 again.
			twistValue = twistValue * (1/0.65);
		} else if (twistValue <= -0.4) {
			twistValue= twistValue + 0.4;
			// Apply an expansion factor. So all the way right becomes 1.0 again.
			twistValue = twistValue * (1/0.6);	
		}
	
		// Slowdown twisting
		twistValue = twistValue * 0.7;
		
		//System.out.println("Original: " + oldTwist +  "\tNew: " + twistValue);

		frontLeft.set(   yValue - xValue + twistValue);
		frontRight.set(-(yValue + xValue - twistValue));
		backLeft.set(    yValue + xValue + twistValue);
		backRight.set( -(yValue - xValue - twistValue));
	}
	

	public void strafeLeft() {
		frontLeft.set(       0.6);
		frontRight.set( -1* -0.6); 
		backLeft.set(       -0.6); 
		backRight.set(  -1*  0.6);
	}

	public void strafeRight() {
		frontLeft.set(      -0.6);
		frontRight.set( -1*  0.6);
		backLeft.set(        0.6);
		backRight.set(  -1* -0.6);
	}
	


	public void resetToZero() {
		frontLeft.set(0);
		frontRight.set(0);
		backLeft.set(0);
		backRight.set(0);
	}

	
}
