package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Deck implements RunningComponent {
	
	private final static double ENCODER_RATE = 30d; //ticks per meter
	private final static double SAFE_DIST = 1d; //meters
	
	private Joystick joystick;
	private DigitalInput frontLimit;
	private DigitalInput backLimit;
	private Encoder deckDistanceEncoder;
	private Talon motor;

	public Deck(Joystick joystick, DigitalInput frontLimit,
			DigitalInput backLimit, Encoder deckDistanceEncoder, Talon motor) {
		super();
		this.joystick = joystick;
		this.frontLimit = frontLimit;
		this.backLimit = backLimit;
		this.deckDistanceEncoder = deckDistanceEncoder;
		this.motor = motor;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					if (frontLimit.get()) {
						deckDistanceEncoder.reset();
					}
					Message.isDeckSafe = whereAreWe() >= SAFE_DIST;
					if (Message.isDeckSafe) {
						Message.makeDeckSafe = false;
					}
				}
			}
		});
	}

	@Override
	public void teleop() {
		
	//	SmartDashboard.putNumber("DeckValue: " , deckDistanceEncoder.get());
		
		int direction;
		
		if(joystick.getRawButton(3)) // towards the front
			direction= -1;
		else if(joystick.getRawButton(4)) // towards the back
			direction = 1;
		else
			direction=0;
		
		//this.motor.set(joystick.getY()*.25);
		
		SmartDashboard.putBoolean("Front Limit", this.frontLimit.get());
		SmartDashboard.putBoolean("Back Limit", this.backLimit.get());
		
		 
	}

	private boolean setDirection(int direction) {
		
		if (Message.makeDeckSafe)
			direction = 1;
		if (whereAreWe() < SAFE_DIST && !Message.makeDeckSafe  )  // Deck is in danger limit
		{
			direction = 0;                                       
			Message.makeLiftSafe = true;							// fix lift to open Deck Access
		}
		else
		{
			Message.makeLiftSafe = false;                        // no changes required
		}
		                    
		boolean isNotSafe = 
				(direction<0 && !this.frontLimit.get()) || //if we're moving out but not too far OR
				(direction>0 && !this.backLimit.get()); //if we're moving in but not too close OR
		
		if(isNotSafe){
			direction = 0; //we're not safe; prevent the motor from moving
		}
		
		
		//move the motor (or don't if unsafe)
		SmartDashboard.putBoolean("Motor is not safe?", isNotSafe); //notify users of safety
		SmartDashboard.putNumber("Direction",direction);// for testing purposes
		
		if(!(whereAreWe() >= SAFE_DIST || Message.isLiftSafe)){
			direction = 0;
			Message.makeLiftSafe = true;
		}
		
		this.motor.set(direction);
		
		return isNotSafe;
		
	}
	
	public double whereAreWe(){
		return deckDistanceEncoder.get() * (1 / ENCODER_RATE);
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
