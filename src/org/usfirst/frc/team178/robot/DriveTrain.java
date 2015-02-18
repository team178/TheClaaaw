package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;


public class DriveTrain implements RunningComponent {
	private Talon frontLeft;
	private Talon backLeft;
	private Talon frontRight;
	private Talon backRight;
	
	private Joystick joystick;
	
	private Gyro gyroDevice;
	
	private double angleCorrection = 0d;
	

	
	private PIDSource gyro = new PIDSource() {
		@Override
		public double pidGet() {
			double joyAngle = joystick.getTwist() * 360;
			return gyroDevice.getAngle() - joyAngle;
		}
	};
	
	private PIDOutput gyroCorr = new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			angleCorrection = output;
		}
	};
	
	@SuppressWarnings("unused")
	private PIDController pid = new PIDController(0.1, 0.001, 0, gyro, gyroCorr);
	
	public DriveTrain(Talon frontLeft, Talon backLeft, Talon frontRight,
			Talon backRight, Joystick joystick, Gyro gyroDevice) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.joystick = joystick;
		this.gyroDevice = gyroDevice;
		
		new ActionHelper() {

			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isBotAlligned=true;
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				double deviation = Robot.networktable.getNumber("DEVIATION_IN_PIXELS");
				if(deviation>15){
					drive(1,0,0);
				}
				else if(deviation<-15){
					drive(-1,0,0);
				}
				else{
					return true;
				}
				return false;
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Message.isBotClearofAZ;
			}
		};
		
		new ActionHelper() {
			private Timer timer = new Timer(); 
			
			{
				timer.start();
			}
			
			
			private boolean Blah = false; 
			@Override
			public void whenDone() {
				// When done, send message, isToteinA2
				Message.isToteinAZ = true; 
			}
			@Override
			public boolean toRun(int interruptions) {
				//Move backwards, check if in Auto zone, if true, return true, if false, return false
				drive(0, -1, 0);
				if (timer.get() >= 3)
					return true; 
				else return false;
			}
			@Override
			public boolean shouldRun() {
				if (!Blah && Message.isToteHeld) {
					Blah = Message.isToteHeld;
					timer.reset();
				}
				if (Message.isToteHeld)
					return true;
				else return false;
			}
		};
		new ActionHelper() {
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isBotReadyToGrab=true;
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				drive(0,1,0);
				return timer.get()>3;
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Message.isBotAlligned;
			}
		};
	}

	@Override
	public void teleop() {
		
		double yValue = joystick.getY();
		double xValue = joystick.getX();
		double twistValue = joystick.getTwist();
		
		double speed = 1-joystick.getRawAxis(3);

		
		if (joystick.getRawButton(2)) {
			speed*=0.5;
		}
		
		
		if (joystick.getRawButton(11)) {
			yValue*=0;
			twistValue*=0;
			xValue = -1;
		}
		else if (joystick.getRawButton(12))
		{
			yValue*=0;
			twistValue*=0;
			xValue = 1;
		}
		

		xValue*=speed;
		yValue*=speed;
		twistValue*=speed;
		
		drive(xValue,yValue,twistValue);
	}

	@Override
	public void auto() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}
	
	/**drive(double xValue, double yValue, double twistValue) */
	public void drive(double xValue, double yValue, double twistValue) {
		if (joystick.getRawButton(3)){
				twistValue += angleCorrection;
		}
		if (joystick.getRawButton(4)){
			gyroDevice.reset();
		}
		frontLeft.set(  - (yValue - xValue - twistValue));
		frontRight.set((yValue + xValue + twistValue));
		backLeft.set(   -( yValue + xValue - twistValue));
		backRight.set( (yValue - xValue + twistValue));
	}

	
}
