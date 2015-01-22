
package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	DriveTrain drivetrain;
	Joystick joystick;
	
    public void robotInit() {
    	drivetrain = new DriveTrain();
    	joystick = new Joystick(0);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	drivetrain.drive(joystick);
    }
 
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
