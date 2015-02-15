package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public interface RunningComponent {
	public void teleop(Joystick driver, Joystick aux);
	public void auto(Timer timer);
	public void test();
}
