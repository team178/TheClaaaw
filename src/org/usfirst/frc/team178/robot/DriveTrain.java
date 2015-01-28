package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class DriveTrain implements RunningComponent {
	private Talon frontLeft;
	private Talon backLeft;
	private Talon frontRight;
	private Talon backRight;
	
	private Joystick joystick;
	
	public DriveTrain(Talon frontLeft, Talon backLeft, Talon frontRight,
			Talon backRight, Joystick joystick) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.joystick = joystick;
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
		frontLeft.set(   yValue - xValue + twistValue);
		frontRight.set(-(yValue + xValue - twistValue));
		backLeft.set(    yValue + xValue + twistValue);
		backRight.set( -(yValue - xValue - twistValue));
	}

	
}
