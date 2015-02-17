package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltraSonics implements RunningComponent{
	
	private static final AnalogInput ultrasonic = new AnalogInput(0);
	
	public UltraSonics(){
		ultrasonic.setAverageBits(4);
	}
	
	public static double getDistanceFromWall(){
		return ultrasonic.getAverageVoltage() * 1 / 0.813;
	}

	@Override
	public void teleop() {
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
