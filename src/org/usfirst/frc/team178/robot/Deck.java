package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Deck implements RunningComponent {
	
	private final static double ENCODER_RATE = 30d; //ticks per meter
	private final static double SAFE_DIST = 1d; //meters
	
	private final static double bottomLimit = 1d;
	
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
		//for testing purposes
		SmartDashboard.putNumber("DeckValue: " , deckDistanceEncoder.get());
		
		int direction;
		
		if(joystick.getRawButton(3)) // towards the front
			direction= -1;
		else if(joystick.getRawButton(4)) // towards the back
			direction = 1;
		else
			direction=0;
		
		setDirection(direction);
		
		//this.motor.set(joystick.getY()*.25);
		
		SmartDashboard.putBoolean("Front Limit", this.frontLimit.get());
		SmartDashboard.putBoolean("Back Limit", this.backLimit.get());
		
		 
	}

	private void setDirection(int direction) {
		
		
		if (Message.makeDeckSafe)
			direction = 1;
		if (whereAreWe() < SAFE_DIST && !Message.isLiftSafe  )  // Deck is in danger limit
		{
			direction = 0;                                       
			Message.makeLiftSafe = true;
			// fix lift to open Deck Access
		}
		else
		{
			Message.makeLiftSafe = false;                        // no changes required
		}
		if(whereAreWe() < bottomLimit){
			Message.isDown = true;
			direction = 0;
		}
		
		SmartDashboard.putNumber("Direction",direction);// for testing purposes
		
		motor.set(direction);
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
