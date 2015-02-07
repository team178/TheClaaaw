package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.CameraServerer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class Camera implements RunningComponent {
	
	private Joystick joystick;
	private CameraServerer camserv;
	
	public Camera(Joystick joystick) {
		super();
		this.joystick = joystick;
    	this.camserv = CameraServerer.getInstance();
  		this.camserv.startAutomaticCapture("cam0");
	}

	@Override
	public void auto() {

	}

	@Override
	public void test() {
		
	}

	@Override
	public void teleop() {
		if (joystick.getRawButton(7)) {
			this.camserv.stopAutomaticCapture();
	  		this.camserv.startAutomaticCapture("cam0");
		}
		if (joystick.getRawButton(9)) {			
			this.camserv.stopAutomaticCapture();
	  		this.camserv.startAutomaticCapture("cam1");
		}
		if (joystick.getRawButton(11)) {
			this.camserv.stopAutomaticCapture();
	  		this.camserv.startAutomaticCapture("cam2");
		}
	}

}
