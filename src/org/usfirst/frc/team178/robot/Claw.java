package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

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
		new ActionHelper() {

			@Override
			public boolean shouldRun() {
				if (Message.isToteinAZ)
					return true;
				else return false;
			}

			@Override
			public boolean toRun(int interruptions) {
				moveClaw(opening);
				if (!leftBackLS.get() && !rightBackLS.get())
					return true;
				else return false;
			}

			@Override
			public void whenDone() {
				
			}
			
		};
	}

	@Override
	public void teleop() {
		
		if(joystick.getRawButton(1)){ //opening
			moveClaw(1);
			
		}
		else if(joystick.getRawButton(2)){ //closing
			moveClaw(-1);
		}
		else{
			rightClaw.set(0);
			leftClaw.set(0);
		}
	}
	
	public void moveClaw(int Direction){
		boolean isTouchingTote = !toteTouchingLS.get();
		
		rightClaw.set(Direction);
		leftClaw.set(Direction);

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
