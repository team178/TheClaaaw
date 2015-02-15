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
	private Joystick joystick;

	private final int opening = 1;
	private final int closing = -1;

	private int direction= 1;
		
	public Claw(Talon leftClaw, Talon rightClaw, DigitalInput toteTouchingLS,
			DigitalInput leftFrontLS, DigitalInput rightFrontLS,
			DigitalInput leftBackLS, DigitalInput rightBackLS, Joystick joystick) {
		super();
		this.leftClaw = leftClaw;
		this.rightClaw = rightClaw;
		this.toteTouchingLS = toteTouchingLS;
		this.leftFrontLS = leftFrontLS;
		this.rightFrontLS = rightFrontLS;
		this.leftBackLS = leftBackLS;
		this.rightBackLS = rightBackLS;
		this.joystick = joystick;
	}


	@Override
	public void teleop() {

		boolean isTouchingTote = !toteTouchingLS.get();
		
		if(joystick.getRawButton(1)){
			leftClaw.set(opening);
			rightClaw.set(opening);
			direction = opening;
			
		}
		else if(joystick.getRawButton(2)){
			leftClaw.set(closing);
			rightClaw.set(closing);
			direction = closing;
		}
		
		if(!leftFrontLS.get() && direction == opening ){
			leftClaw.set(0);
		}
		
		if (!leftBackLS.get() && direction == closing){
			leftClaw.set(0);
		}
		
		if (rightFrontLS.get()!=true && direction == opening){
			rightClaw.set(0);
		}
		if (rightBackLS.get()!=true && direction == closing){
			rightClaw.set(0);
		}
		if (isTouchingTote && direction == closing){
			rightClaw.set(0);
			leftClaw.set(0);
		}

//		SmartDashboard.putBoolean("RBLF", leftFrontRightBack.get());

	}

	/*
	 * We only need this because the limit switches are being wired in parallel :)
	 */

	@Override
	public void auto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}
}
