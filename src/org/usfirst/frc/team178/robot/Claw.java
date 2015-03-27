package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Claw implements RunningComponent {

	private Talon leftClaw;
	private Talon rightClaw;
	private DigitalInput toteTouchingLS; //is item when limit switch is pressed
	private DigitalInput leftFrontLS;
	private DigitalInput rightFrontLS;
	private DigitalInput leftBackLS;
	private DigitalInput rightBackLS;
	
	//to help human comprehension
	public static final int DIRECTION_OPEN = 1;
	public static final int DIRECTION_CLOSE = -1;
	public static final int DIRECTION_STOP = 0;
		
	public Claw(Talon leftClaw, Talon rightClaw, DigitalInput toteTouchingLS,
			DigitalInput leftFrontLS, DigitalInput rightFrontLS,
			DigitalInput leftBackLS, DigitalInput rightBackLS) {
		super();
		this.leftClaw = leftClaw;
		this.rightClaw = rightClaw;
		this.toteTouchingLS = toteTouchingLS;
		this.leftFrontLS = leftFrontLS;
		this.leftBackLS = leftBackLS;
		this.rightBackLS = rightBackLS;
		this.rightFrontLS = rightFrontLS;
				
	}

	public boolean isTouchingTote() {
 		return !toteTouchingLS.get();
	}
	
	@Override
	public void teleop(Joystick joystick, Joystick aux) {
		SmartDashboard.putBoolean("toteTouchingLS",toteTouchingLS.get());
		SmartDashboard.putBoolean("leftFrontLS", leftFrontLS.get());
		SmartDashboard.putBoolean("rightFrontLS",rightFrontLS.get());
		SmartDashboard.putBoolean("rightBackLS", rightBackLS.get());
		SmartDashboard.putBoolean("leftBackLS", leftBackLS.get());
		
		// http://en.wikipedia.org/wiki/%3F:#Java // just in case you have no idea what's going on with the ?s
		this.moveClaw(
				aux.getRawButton(1) ? DIRECTION_OPEN : //open the claw if A pressed, otherwise
					aux.getRawButton(2) ? DIRECTION_CLOSE : //close the claw if B pressed, otherwise 
						DIRECTION_STOP //STOP THE CLAWWW
				,
				aux.getRawAxis(3)>=.9 && aux.getRawAxis(2)>=.9 //override safeties if triggers are pressed
		);
		/* //Equivalent code:
		 * boolean override = aux.getRawAxis(3)>=.9 && aux.getRawAxis(2)>=.9;
		 * int direction;
		 * if(aux.getRawButton(1)){ 
		 *     direction = DIRECTION_OPEN;
		 * } else if(aux.getRawButton(2)){ 
		 *     direction = DIRECTION_CLOSE;
		 * } else {
		 *     direction = DIRECTION_STOP;
		 * }
		 * this.moveClaw(direction, override);
		 * // it's a ternary teaching moment
		 */
	}
	
	public void moveClaw(int direction) {
		moveClaw(direction, false);
	}
	
	public void moveClaw(int direction, boolean override){
		boolean isTouchingTote = !toteTouchingLS.get();
		double clawSpeed = SmartDashboard.getNumber("clawSpeed", 0.5d); //claw speed modifier
		
		if (isTouchingTote && direction == DIRECTION_CLOSE && //if it's closing and is touching a tote
				!override){ //and there's no override
			rightClaw.set(DIRECTION_STOP); // stop the
			leftClaw.set(DIRECTION_STOP);  // motors please
		} else {
			if(
					(!leftFrontLS.get() && direction == DIRECTION_OPEN) || //if we're trying to open too far on the left side
					(!leftBackLS.get() && direction == DIRECTION_CLOSE)	)  //or close too far, for that matter
			{
				leftClaw.set(DIRECTION_STOP); //put a stop to it
			} else 
				leftClaw.set(direction * clawSpeed); //otherwise we're prolly good to go
			if(
					(!rightFrontLS.get() && direction == DIRECTION_OPEN) || //if we're trying to open too far on the right side
					(!rightBackLS.get() && direction == DIRECTION_CLOSE) )  //or close too far, for that matter
			{
				rightClaw.set(DIRECTION_STOP); //put a stop to it
			} else 
				rightClaw.set(direction * clawSpeed); //otherwise we're prolly good to go
			
		}
	}
		

	
	/*
	 * We only need this because the limit switches are being wired in parallel :)
	 */

	@Override
	public void test(Joystick driver) {
	}
}
