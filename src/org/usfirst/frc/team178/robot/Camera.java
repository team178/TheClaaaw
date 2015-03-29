package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera implements RunningComponent{
	private CameraServer cameraServer = CameraServer.getInstance();
	private USBCamera camera = null;
	private boolean capStarted = false;
	private int tryCount;
	
	public Camera(String cam) {
		super();
		
		grabCamera();
	}

	private void grabCamera() {
		if(camera != null) return;
		
		try {
			camera = new USBCamera("cam0");
		} catch (Exception e) {
			try {
				camera = new USBCamera("cam1");
			} catch (Exception e2) {
				System.out.println("We didn't find a camera. Try again.");
			}
			
		}
		
		if(camera != null && !capStarted){
			cameraServer.startAutomaticCapture(camera);
			capStarted = true;
		}
	}
	
	@Override
	public void teleop(Joystick driver, Joystick aux) {
		if(tryCount++ % 500 == 0)
			/*grabCamera()*/;
	}

	@Override
	public void test(Joystick driver) {
	}

}
