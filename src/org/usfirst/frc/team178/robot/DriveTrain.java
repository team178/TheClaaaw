package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Talon;

public class DriveTrain {
	Talon frontLeft;
	Talon frontRight;
	Talon backLeft;
	Talon backRight;
	
	DriveTrain() {
		frontRight= new Talon(0);
		frontLeft = new Talon(1);
		backRight= new Talon(2);
		backLeft= new Talon(3);
	}
	
	public void drive(double yValue, double xValue, double twistValue){
		frontLeft.set(0.5*(yValue-xValue+twistValue));
		frontRight.set(0.5*(-(yValue+xValue-twistValue)));
		backLeft.set(0.5*(yValue+xValue+twistValue));
		backRight.set(0.5*(-(yValue-xValue-twistValue)));
		
	}
}
