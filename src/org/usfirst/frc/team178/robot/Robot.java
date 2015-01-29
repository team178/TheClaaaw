
package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	private RunningComponent[] components = {
			new DriveTrain(new Talon(3),new Talon(2),new Talon(1),new Talon(0),new Joystick(0),new Gyro(0))
	};
    public void robotInit() {
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	for (int i = 0; i < components.length; i++) {
			components[i].auto();
		}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	for (int i = 0; i < components.length; i++) {
			components[i].teleop();
		}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	for (int i = 0; i < components.length; i++) {
			components[i].test();
		}
    }
    
}
