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
	
	private Talon motor;
	private Encoder liftDistanceEncoder;
	private DigitalInput zeroLimit;
	
	public Lift(Talon motor, DigitalInput zeroLimit, Encoder liftDistanceEncoder) {
		super();
		this.motor = motor;
		this.liftDistanceEncoder = liftDistanceEncoder;
		this.zeroLimit = zeroLimit;
		
		//for deck-lift safety code
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){

					if (zeroLimit.get()) {
						liftDistanceEncoder.reset();
					}
					Message.isLiftSafe = whereAreWe() >= SAFE_DIST;
					if (Message.isLiftSafe) {
						Message.makeLiftSafe = false;
					}
				}
			}
		});*/
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
				direction=1;
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
		
		//for deck-lift safety code
		/*if (Message.makeLiftSafe)                                // fix lift to allow deck movement
			direction = 1;
		if((whereAreWe() < SAFE_DIST) && !Message.isDeckSafe){ // not safe deck OR not safe lift
			direction = 0;                                       
			Message.makeDeckSafe = true;                         // fix deck to allow Lift Access
		}else{
			Message.makeDeckSafe = false;                        // no changes required
		}*/
		motor.set(direction);
	}
	
	//in case we get an int direction
	private void moveMotor(int direction)
	{
		moveMotor((double) direction); //for compatability
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





