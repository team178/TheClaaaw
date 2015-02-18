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
	protected static final double BACK_DRIVE_TIME = 3; //seconds
	private Talon frontLeft;
	private Talon backLeft;
	private Talon frontRight;
	private Talon backRight;
	
	private Gyro gyroDevice;
	
	private double angleCorrection = 0d;
	

	
	private PIDSource gyro = new PIDSource() {
		@Override
		public double pidGet() {
			double joyAngle = /*joystick.getTwist() * sorry not sorry*/ 360;
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
			Talon backRight, Gyro gyroDevice) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.gyroDevice = gyroDevice;
		
		new ActionHelper() {

			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isBotAlligned=true;
				Message.isBotClearofAZ=false;
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
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isBotMovedBack = true;
				Message.isCaninAZB = false;
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				drive(0,-1,0);
				return timer.get()>.5;
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Message.isCaninAZB;
			}
		};
		new ActionHelper() {
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isBotMovedBack = false;
				Message.isCanReleased = false;
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				drive(-1,0,0);
				return timer.get()>.5;
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Message.isBotMovedBack && Message.isCanReleased;
			}
		};
		new ActionHelper() {
			@Override
			public void whenDone() {
				// When done, send message, isToteinA2

				if (Message.isToteHeld) {
					Message.isToteHeld = false;
					Message.isToteinAZ = true; 	
				} else if(Message.isCanHeld){
					Message.isCanHeld = false;
					Message.isCaninAZA = true;
					Message.isCaninAZB = true;
				} else throw new Error();
			}
			@Override
			public boolean toRun(int interruptions) {
				//Move backwards, check if in Auto zone, if true, return true, if false, return false
				drive(0, -1, 0);
				return timer.get() > BACK_DRIVE_TIME;
			}
			@Override
			public boolean shouldRun() {
				if (Message.isToteHeld || Message.isCanHeld)
					return true;
				else return false;
			}
		};
		new ActionHelper() {
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				Message.isBotReadyToGrab=true;
				Message.isBotAlligned = false; 
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				drive(0,1,0);
				return (UltraSonics.getDistanceFromWall()<=1);
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Message.isBotAlligned;
			}
		};
		new ActionHelper() {
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				//do nothing; we're waiting for isCanHeld anyway
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				drive(0, .2, 0);
				return Message.isCanHeld;
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				return Robot.instance.isAutonomous() && !this.finishedRunning;
			}
		};
	}

	@Override
	public void teleop(Joystick joystick, Joystick aux) {
		
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
		/*if (joystick.getRawButton(3)){
				twistValue += angleCorrection;
		}
		if (joystick.getRawButton(4)){
			gyroDevice.reset();
		}*/
		frontLeft.set(  - (yValue - xValue - twistValue));
		frontRight.set((yValue + xValue + twistValue));
		backLeft.set(   -( yValue + xValue - twistValue));
		backRight.set( (yValue - xValue + twistValue));
	}

	
}
