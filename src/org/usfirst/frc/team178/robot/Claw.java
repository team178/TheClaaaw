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
	private final int opening = 1;
	private final int closing = -1;

	private int direction= 1;
		
	public Claw(Talon leftClaw, Talon rightClaw, DigitalInput toteTouchingLS,
			DigitalInput leftFrontLS, DigitalInput rightFrontLS,
			DigitalInput leftBackLS, DigitalInput rightBackLS) {
		super();
		this.leftClaw = leftClaw;
		this.rightClaw = rightClaw;
		this.toteTouchingLS = toteTouchingLS;
		this.leftFrontLS = leftFrontLS;
		this.rightFrontLS = rightFrontLS;
		this.leftBackLS = leftBackLS;
		this.rightBackLS = rightBackLS;
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
				Message.isToteinAZ = false;
			}
			
		};

		new ActionHelper() {
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isToteHeld=true;
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				moveClaw(closing);
				return rightFrontLS.get() && leftFrontLS.get();
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Message.isBotReadyToGrab;
			}
		};
		
		new ActionHelper() {
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isCanHeld = true;
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				moveClaw(closing);
				return leftFrontLS.get() && rightFrontLS.get();
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Robot.instance.isAutonomous() && !this.finishedRunning;
			}
		};

	}

	@Override
	public void teleop(Joystick joystick, Joystick aux) {
		
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

		if(!leftFrontLS.get() && Direction == opening ){
			leftClaw.set(0);
		}
		
		if (!leftBackLS.get() && Direction == closing){
			leftClaw.set(0);
		}
		
		if (rightFrontLS.get()!=true && Direction == opening){
			rightClaw.set(0);
		}
		if (rightBackLS.get()!=true && Direction == closing){
			rightClaw.set(0);
		}
		if (isTouchingTote && Direction == closing){
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
