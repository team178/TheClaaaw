package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera implements RunningComponent{
	private CameraServer cameraServer = CameraServer.getInstance();
	
	public Camera(String cam) {
		super();
		cameraServer.startAutomaticCapture(new USBCamera(cam));
	}
	
	@Override
	public void teleop(Joystick driver, Joystick aux) {
	}

	@Override
	public void test(Joystick driver) {
	}

}
