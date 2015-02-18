package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltraSonics implements RunningComponent{
	
	private static AnalogInput ultrasonic;
	private static double scaler;
	
	public UltraSonics(AnalogInput ultrasonic) {
		super();
		UltraSonics.ultrasonic = ultrasonic;
		ultrasonic.setAverageBits(4);
		scaler = ultrasonic.getAverageVoltage();
	}
	
	public static double getDistanceFromWall(){
		return ultrasonic.getAverageVoltage() * 1 / scaler;  //need to replace the Stolarz with another value
	}

	@Override
	public void teleop(Joystick driver, Joystick aux) {
		// TODO Auto-generated method stub
		SmartDashboard.putNumber("Distance from Wall", getDistanceFromWall());
	}

	@Override
	public void auto() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}
}
