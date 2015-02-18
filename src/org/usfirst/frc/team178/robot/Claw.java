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
	private final int opening = 1;
	private final int closing = -1;
		
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
	}

	@Override
<<<<<<< HEAD
	public void teleop() {
		SmartDashboard.putBoolean("toteTouchingLS",toteTouchingLS.get());
		SmartDashboard.putBoolean("leftFrontLS", leftFrontLS.get());
		SmartDashboard.putBoolean("rightFrontLS",rightFrontLS.get());
		SmartDashboard.putBoolean("rightBackLS", rightBackLS.get());
		SmartDashboard.putBoolean("leftBackLS", leftBackLS.get());
=======
	public void teleop(Joystick joystick, Joystick aux) {
>>>>>>> 5708159d5de8f7e505788a6309f5b15eaac21994
		
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
	
	public void moveClaw(int direction){
		boolean isTouchingTote = !toteTouchingLS.get();
		
		rightClaw.set(direction);
		leftClaw.set(direction);

		if(!leftFrontLS.get() && direction == opening ){
			leftClaw.set(0);
		}
		
		if (!leftBackLS.get() && direction == closing){
			leftClaw.set(0);
		}
		
		if (!rightFrontLS.get() && direction == opening){
			rightClaw.set(0);
		}
		if (!rightBackLS.get() && direction == closing){
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
