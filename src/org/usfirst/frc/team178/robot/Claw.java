package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Claw implements RunningComponent {

	private Talon leftClaw;
	private Talon rightClaw;
	private DigitalInput detectItem;
	private DigitalInput leftFrontRightBack;
	private DigitalInput rightFrontLeftBack;
	private Joystick joystick;

	private int lastRightDirection = -1;
	private int lastLeftDirection = -1;
	private int goPosition = 1;



	public Claw(Talon leftClaw, Talon rightClaw, DigitalInput detectItem,
			DigitalInput leftFrontRightBack, DigitalInput rightFrontLeftBack,
			Joystick joystick) {
		super();
		this.leftClaw = leftClaw;
		this.rightClaw = rightClaw;
		this.detectItem = detectItem;
		this.leftFrontRightBack = leftFrontRightBack;
		this.rightFrontLeftBack = rightFrontLeftBack;
		this.joystick = joystick;
	}
	
	@Override
	public void teleop() {
		if(joystick.getRawButton(5))
			goPosition = -1;
		else if(joystick.getRawButton(6))
			goPosition = 1;
		moveClaw();
	}

	private boolean[] moveClaw() {
		boolean isRightSafe = !( //we are not safe if
				leftFrontRightBack.get() && lastRightDirection == -1 || //rightback is triggered OR
				rightFrontLeftBack.get() && lastRightDirection == 1     //rightfront is triggered
				);
		boolean isLeftSafe = !( //we are not safe if
				leftFrontRightBack.get() && lastLeftDirection == 1 || //leftfront is triggered OR
				rightFrontLeftBack.get() && lastLeftDirection == -1   //leftback is triggered
				);
		if(isRightSafe){
			rightClaw.set(goPosition);
		} else {
			lastRightDirection = goPosition;
		}
		
		if(isLeftSafe){
			leftClaw.set(goPosition);
		} else {
			lastLeftDirection = goPosition;
		}

		//				(d>0 && !this.outerLimit.get()) || //if we're moving out but not too far OR
		//				(d<0 && !this.innerLimit.get()) || //if we're moving in but not too close OR
		//				(d == 0); //if the motor shouldn't move...

		//		if(!isSafe){
		//			direction = 0; //we're not safe; prevent the motor from moving
		//		}
		//		
		//		leftClaw.set(direction); //move the motors (or don't if unsafe)
		//		rightClaw.set(direction); //TODO be more negative
		//		
		//		SmartDashboard.putBoolean("Claw is safe?", isSafe); //notify users of safety
		return new boolean[] {isLeftSafe,isRightSafe};

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
