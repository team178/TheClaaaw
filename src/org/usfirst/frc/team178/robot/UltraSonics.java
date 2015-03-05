package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltraSonics implements RunningComponent{
	
	private AnalogInput ultrasonic;
	private double scaler;
	public static double scaledDistanceFromTote; //scaled so 1= correct distance from tote
	
	public UltraSonics(AnalogInput ultrasonic) {
		super();
		this.ultrasonic = ultrasonic;
		ultrasonic.setAverageBits(4);
		scaler = ultrasonic.getAverageVoltage(); //scaled at the start
	}

	
	
	public void getDistanceFromTote(){
		scaledDistanceFromTote= this.ultrasonic.getAverageVoltage() * 1 / scaler;  //scaled so 1 is the distance we want to be from a tote
	}

	@Override
	public void teleop(Joystick driver, Joystick aux) {
		// TODO Auto-generated method stub
		this.getDistanceFromTote();
		SmartDashboard.putNumber("UltrasonicDistance", scaledDistanceFromTote);
	}

	@Override
	public void test(Joystick driver) {
		// TODO Auto-generated method stub
		
	}
}
