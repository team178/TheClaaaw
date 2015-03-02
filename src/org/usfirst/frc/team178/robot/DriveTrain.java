package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Victor;

public class DriveTrain implements RunningComponent {
	private Victor frontLeft;
	private Victor backLeft;
	private Victor frontRight;
	private Victor backRight;
	private Joystick joystick;
	protected static final double BACK_DRIVE_TIME = 1; //seconds
	private Gyro gyroDevice;
	
	private double angleCorrection = 0d;
	

	
	private PIDSource gyro = new PIDSource() {
		@Override
		public double pidGet() {
			double joyAngle = joystick.getTwist()* 360;
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
	
	public DriveTrain(Victor frontLeft, Victor backLeft, Victor frontRight,
			Victor backRight, Gyro gyroDevice) {
		super();
		this.frontLeft = frontLeft;
		this.backLeft = backLeft;
		this.frontRight = frontRight;
		this.backRight = backRight;
		this.gyroDevice = gyroDevice;
		
//		Step 4
//		Aligns the robot after clearing it of the AZ zone
//			Activated by isBotClearofAZ
//			Activates isBotAlligned
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
				double deviation = Robot.instance.networktable.getNumber("DEVIATION_IN_PIXELS");
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
		
//		Step 3
//		Drives back for a set amount of time
//			Activated by isCaninAZB
//			Activates isBotMovedBack
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
		
//		Step 
//		Drives left for a set amount of time
//			Activated by isBotMovedBack AND isCanReleased
//			Activates nothing??
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
		
//		Steps 2 and 6
//		Drives back for a set amount of time
//			Activated by isToteHeld OR isCanHeld
//			Activates isToteinAZ OR isCaninAZA AND isCaninAZB
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
		
//		Step 5
//		Drives forward until ultrasonics says we're in range
//			Activated by isBotReadytoGrab
//			Activates isBotAlligned
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
		
//		Step 1
//		Drives forward slowly until isCanHeld is true (until we hold the can)
//			Activated by isAutonomous (start of autonomous) AND if this.finishedRunning isn't true
//			Activates nothing
		/*new ActionHelper() {
			
			@Override
			public void whenDone() {
				// TODO Auto-generated method stub
				//do nothing; we're waiting for isCanHeld anyway
			}
			
			@Override
			public boolean toRun(int interruptions) {
				// TODO Auto-generated method stub
				drive(0, .1, 0);
				return Message.isCanHeld;
			}
			
			@Override
			public boolean shouldRun() {
				// TODO Auto-generated method stub
				System.out.println("I'M BEING TESTED YO");
				if(Robot.instance == null) return false;
				return Robot.instance.isAutonomous() && !this.finishedRunning;
			}
		};*/
	}

	@Override
	public void teleop(Joystick joystick, Joystick aux) {
		this.joystick = joystick;
		
		double yValue = joystick.getY();
		double xValue = joystick.getX();
		double twistValue = joystick.getTwist();
		
		double speed = 1-joystick.getRawAxis(3);

		
		if (joystick.getRawButton(2)) {
			speed*=0.5;
		}
		
		
		if (joystick.getRawButton(11)) { //snap-to-axis code
			yValue*=0;
			twistValue*=0;
			xValue = -1;
			/*
			speed=1;
			twistValue*=0;
			if(Math.abs(xValue) > Math.abs(yValue))
				yValue*=0;
			else // x<=y
				xValue*=0;*/
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
