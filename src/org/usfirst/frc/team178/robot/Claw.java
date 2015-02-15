package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Claw implements RunningComponent {

	private Talon leftClaw;
	private Talon rightClaw;
	private DigitalInput noItem;
	private DigitalInput leftFrontRightBack;
	private DigitalInput rightFrontLeftBack;
	private boolean leftFrontSafe;
	private boolean leftBackSafe;
	private boolean rightFrontSafe;
	private boolean rightBackSafe;
	private Joystick joystick;

	private int lastRightDirection = -1;
	private int lastLeftDirection = -1;
	private int goPosition = 1;

	
	public Claw(Talon leftClaw, Talon rightClaw, DigitalInput noItem,
			DigitalInput leftFrontSafeRightBack, DigitalInput rightFrontLeftBack,
			Joystick joystick) {
		super();
		this.leftClaw = leftClaw;
		this.rightClaw = rightClaw;
		this.noItem = noItem;
		this.leftFrontRightBack = leftFrontRightBack;
		this.rightFrontLeftBack = rightFrontLeftBack;
		this.joystick = joystick;
	}
		
	@Override
	public void teleop() {
		setLimitSwitches();
		if(joystick.getRawButton(1)){ // close gripper
			goPosition = 1;
			if(noItem.get()){
				moveClaw(rightFrontSafe, rightClaw, lastRightDirection);
				moveClaw(leftFrontSafe, leftClaw, lastLeftDirection);
			}
			if (rightBackSafe){
				lastRightDirection=goPosition;
			}
			if (leftBackSafe){
				lastLeftDirection=goPosition;
			}
		}
		else if(joystick.getRawButton(2)){ // open gripper
			goPosition = -1;
			moveClaw(rightBackSafe, rightClaw, lastRightDirection);
			moveClaw(leftBackSafe, leftClaw, lastLeftDirection);
			if (rightFrontSafe){
				lastRightDirection=goPosition;
			}
			if (leftFrontSafe){
				lastLeftDirection=goPosition;
			}
		}

	}

	
	private void moveClaw(boolean limitSwitch, Talon claw, int lastDirection) {
		if(limitSwitch) {
			claw.set(goPosition);
		} else {
			claw.set(0);
		}
	}

	/*
	 * We only need this because the limit switches are being wired in parallel :)
	 */
	public void setLimitSwitches(){
		if(!leftFrontRightBack.get() && lastRightDirection == -1){ //if the right back is triggered and the last right direction is backwards
			rightBackSafe = false; //it's false
		} else { // if right back is not triggered, or if right back is triggered AND we are opening the claw
			rightBackSafe = true;
		}
		if(!rightFrontLeftBack.get() && lastRightDirection == 1){ //if the right front is triggered and the last right direction is forwards
			rightFrontSafe= false; //it's false
		}else{
			rightFrontSafe= true;
		}
		if(!leftFrontRightBack.get() && lastLeftDirection == 1){ //if the left front is triggered and the last left direction is forwards
			leftFrontSafe= false; //it's false
		}else{ 
			leftFrontSafe= true;
		}
		if(!rightFrontLeftBack.get() && lastLeftDirection == -1){ //if the left back is triggered and the last left direction is backwards
			leftBackSafe=false; //it's false
		}else{
			leftBackSafe=true;
		}
	}

	@Override
	public void auto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}
}
