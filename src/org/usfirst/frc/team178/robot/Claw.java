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

		boolean override= aux.getRawAxis(3)>=.9 && aux.getRawAxis(2)>=.9;
		if(aux.getRawButton(1)){ //opening
			this.moveClaw(1, override);
		}
		else if(aux.getRawButton(2)){ //closing
			this.moveClaw(-1, override);
		}
		else{
			rightClaw.set(0);
			leftClaw.set(0);
		}
	}
	
	public void moveClaw(int direction) {
		moveClaw(direction, false);
	}
	
	public void moveClaw(int direction, boolean override){
		boolean isTouchingTote = !toteTouchingLS.get();
		double clawSpeed = 1.0; //claw speed modifier
		
		rightClaw.set(direction * clawSpeed);
		leftClaw.set(direction * clawSpeed);
		
		if(!leftFrontLS.get() && direction == DIRECTION_OPEN ){
			leftClaw.set(DIRECTION_STOP);
		}

		if (!leftBackLS.get() && direction == DIRECTION_CLOSE){
			leftClaw.set(DIRECTION_STOP);
		}
		
		if (!rightFrontLS.get() && direction == DIRECTION_OPEN){
			rightClaw.set(DIRECTION_STOP);
		}
		if (!rightBackLS.get() && direction == DIRECTION_CLOSE){
			rightClaw.set(DIRECTION_STOP);
		}
		if (isTouchingTote && direction == DIRECTION_CLOSE && //if it's closing and is touching a tote
				!override){ //and there's no override
			rightClaw.set(DIRECTION_STOP);
			leftClaw.set(DIRECTION_STOP);
		}
	}
		

	
	/*
	 * We only need this because the limit switches are being wired in parallel :)
	 */

	@Override
	public void test(Joystick driver) {
	}
}
