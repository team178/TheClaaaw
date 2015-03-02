package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;

public interface RunningComponent {
	public void teleop(Joystick driver, Joystick aux);
	public void auto();
	public void test(Joystick driver);
}
