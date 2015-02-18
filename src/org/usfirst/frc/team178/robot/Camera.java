package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

public class Camera implements RunningComponent {
	
	private CameraServer camserv;


	public Camera() {
		super();
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
	public void teleop(Joystick joystick, Joystick aux) {/*
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
