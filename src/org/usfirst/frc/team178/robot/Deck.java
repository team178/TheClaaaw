package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Deck implements RunningComponent {
	private DigitalInput frontLimit;
	private DigitalInput backLimit;
	private Victor motor;

	public Deck(DigitalInput frontLimit,
			DigitalInput backLimit,  Victor motor) {
		super();
		this.frontLimit = frontLimit;
		this.backLimit = backLimit;
		this.motor = motor;
		
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					Message.isDeckSafe = frontLimit.get();
					if (Message.isDeckSafe) {
						Message.makeDeckSafe = false;
					}
				}
			}
		});*/
	}

	@Override
	public void teleop(Joystick joystick, Joystick aux) {
		int direction;
		
		if(aux.getRawButton(6)) // towards the front
			direction= -1;
		else if(aux.getRawButton(5)) // towards the back
			direction = 1;
		else
			direction=0;
		
		setDirection(direction);
		
		SmartDashboard.putBoolean("DeckFL", this.frontLimit.get());
		SmartDashboard.putBoolean("DeckBL", this.backLimit.get());
		
		 
	}

	private void setDirection(int direction) {
		
		/*
		if (Message.makeDeckSafe)
			direction = 1;
		if (!frontLimit.get() && !Message.isLiftSafe  )  // Deck is in danger limit
		{
			direction = 0;                                       
			Message.makeLiftSafe = true;
			// fix lift to open Deck Access
		}
		else
		{
			Message.makeLiftSafe = false;                        // no changes required
		}
		if(!frontLimit.get()){
			Message.isDown = true;
			direction = 0;
		}
		
		SmartDashboard.putNumber("Direction",direction);// for testing purposes
		*/
		motor.set(direction);
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
