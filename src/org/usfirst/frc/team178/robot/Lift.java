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
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_STOP = 0;
	public static final int DIRECTION_DOWN = -1;
	
	private Talon motor, motor2;
	private Encoder liftDistanceEncoder;
	private DigitalInput zeroLimit;
	private DigitalInput topLimit;
	
	public Lift(Talon motor, Talon motor2, Encoder liftDistanceEncoder,
			DigitalInput topLimit, DigitalInput zeroLimit) {
		super();
		this.motor = motor;
		this.motor2 = motor2;
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
		SmartDashboard.putBoolean("topLimit", topLimit.get());
		
		int direction;
		
		if (aux.getRawButton(4)) { //going up (Y)
			direction = DIRECTION_UP;
		} else if (aux.getRawButton(3)) { //going down (X)
			direction = DIRECTION_DOWN;
		} else 
			direction = DIRECTION_STOP;
		
		moveMotor(direction);
	}

	public void moveMotor(int direction) {

		double speed = SmartDashboard.getNumber("liftSpeed", 1d); //control speed of the lift, VARIABLE
		
		if (topLimit.get() && direction == DIRECTION_UP) {
			direction = DIRECTION_STOP;
		} 
		
		if (zeroLimit.get() && direction == DIRECTION_DOWN) {
			direction = DIRECTION_STOP;
		}
		
		motor.set(direction * speed);
		motor2.set(direction * speed);
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