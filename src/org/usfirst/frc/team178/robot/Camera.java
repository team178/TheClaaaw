package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

public class Camera implements RunningComponent {
	
	private Joystick joystick;
	private CameraServer camserv;


	public Camera(Joystick joystick) {
		super();
		this.joystick = joystick;
    	this.camserv = CameraServer.getInstance();
  		this.camserv.startAutomaticCapture("cam0");
	}

	@Override
	public void auto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void test() {
		
	}

	@Override
	public void teleop() {/*
		if (joystick.getRawButton(7)) {
			this.camserv.stopAutomaticCapture();
	  		this.camserv.startAutomaticCapture("cam0");
		}
		if (joystick.getRawButton(4)) {
			System.out.println("Button 4");
			
			this.camserv.stopAutomaticCapture();
	  		this.camserv.startAutomaticCapture("cam1");
		}*/
	}

}
