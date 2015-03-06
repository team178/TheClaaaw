package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DipSwitches implements RunningComponent {
	private DigitalInput RightSwitch;
	private DigitalInput LeftSwitch;

	public DipSwitches(DigitalInput rightSwitch, DigitalInput leftSwitch) {
		super();
		this.RightSwitch = rightSwitch;
		this.LeftSwitch = leftSwitch;
	}

	public boolean pickUpTote() {
		return this.RightSwitch.get() && this.LeftSwitch.get();
	}
	@Override
	public void teleop(Joystick driver, Joystick aux) {
		// TODO Auto-generated method stub
		SmartDashboard.putBoolean("Right DipSwitch", RightSwitch.get());
		SmartDashboard.putBoolean("Left DipSwitch", LeftSwitch.get());
	}

	@Override
	public void test(Joystick driver) {
		// TODO Auto-generated method stub
		
	}
	
}
