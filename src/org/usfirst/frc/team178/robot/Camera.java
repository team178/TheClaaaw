package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera implements RunningComponent {
	
	private Joystick joystick;
	private CameraServer camserv;
	private USBCamera cam0;
	private USBCamera cam1;
	
	@Override
	public void teleop() {
		// TODO Auto-generated method stub
		
	}

	public Camera(Joystick joystick) {
		super();
		this.joystick = joystick;
		this.cam0 = new USBCamera("cam0");
		this.cam1 = new USBCamera("cam1");
		this.cam0.startCapture();
    	//this.camserv = CameraServer.getInstance();
    	//this.camserv.setQuality(100);
  		//this.camserv.startAutomaticCapture("cam0");
	}

	@Override
	public void auto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void test() {
		System.out.println(joystick.getButton(ButtonType.kTop));
		System.out.println(joystick.getButton(ButtonType.kNumButton));
		System.out.println(joystick.getButton(ButtonType.kTrigger));
		if (joystick.getRawButton(3)) {
			System.out.println("Button 3");
			cam0.stopCapture();
			cam1.startCapture();
		}
		if (joystick.getRawButton(4)) {
			System.out.println("Button 4");
			cam1.stopCapture();
			cam0.startCapture();
		}
	}

}
