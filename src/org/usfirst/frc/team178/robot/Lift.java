package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift implements RunningComponent{
	
	private static final int SAFE_DIST = 0; //meters
	private final static double ENCODER_RATE = 1d; //ticks per meter
	private final static double POS_ONE = 50;
	private final static double POS_TWO = 10;
	public static final double DIRECTION_UP = 1;
	public static final double DIRECTION_STOP = 0;
	
	private Talon motor;
	private Encoder liftDistanceEncoder;
	private DigitalInput zeroLimit;
	private DigitalInput topLimit;
	
	public Lift(Talon motor, Encoder liftDistanceEncoder,
			DigitalInput topLimit, DigitalInput zeroLimit) {
		super();
		this.motor = motor;
		this.liftDistanceEncoder = liftDistanceEncoder;
		this.zeroLimit = zeroLimit;
		this.topLimit = topLimit;
	}

	//for deck-lift safety code
	
	private double whereAreWe() {
		// TODO Auto-generated method stub
		return liftDistanceEncoder.get() * (1/ENCODER_RATE);
	}
	
	@Override
	public void teleop(Joystick joystick, Joystick aux) {
		SmartDashboard.putNumber("LiftValue: " , whereAreWe());
		SmartDashboard.putBoolean("zeroLimit", zeroLimit.get());
		
		double speed = 1;//control speed of the lift, VARIABLE
		double direction;
		
		if (aux.getRawButton(4)) { //going up
			if (topLimit.get()) {
				direction = 0;
			} else {
				direction = 1;
			}
		} else if (aux.getRawButton(3)) { //going down
			if (zeroLimit.get()) {
				direction = 0;
			}else{
				direction = -1;
			}
		} else 
			direction = 0;
		
		direction*=speed;//for speed control
		moveMotor(direction); //speed*direction
		
		
	}

	public void moveMotor(double direction) {
		motor.set(direction);
	}

	@Override
	public void test(Joystick joystick) {
		
		
		/*if (zeroLimit.get()) {
			motor.set(0);
		}else this.motor.set(joystick.getY());
		SmartDashboard.putNumber("RawSpeed", liftDistanceEncoder.get());
		SmartDashboard.putNumber("newValue", whereAreWe());
		if (joystick.getRawButton(1)){
			liftDistanceEncoder.reset();
		}
		if (joystick.getRawButton(2)){
			if(liftDistanceEncoder.get() < POS_ONE){
				this.motor.set(1);
			}
			else if(liftDistanceEncoder.get() > POS_ONE){
				this.motor.set(-1);
			}else{
				this.motor.set(0);
			}
		}
		else if (joystick.getRawButton(3)){
			if(liftDistanceEncoder.get() < POS_TWO){
				this.motor.set(1);
			}
			else if(liftDistanceEncoder.get() > POS_TWO){
				this.motor.set(-1);
			} else{
				this.motor.set(0);
			}
		}*/
	}

}





