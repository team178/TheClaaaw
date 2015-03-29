package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private Joystick driver = new Joystick(0);
	private Joystick aux = new Joystick(1);
	
	//Get the NeworkTable for the Robot
	public final NetworkTable networktable = NetworkTable.getTable("Vision");;

	public static Robot instance;
	
	//objects need to be instanizated in order to use them for autonomous
	private DriveTrain driveTrain = new DriveTrain (
			new Talon(0), //frontLeft
			new Talon(1), //backLeft
			new Talon(2), //frontRight
			new Talon(3), //backRight
			new Gyro(0) //gyro
	);
	private DipSwitches dipSwitches = new DipSwitches(
			new DigitalInput(16), //rightSwitch
			new DigitalInput(17) //leftSwitch
			);
	private Claw claw = new Claw (
			new Talon(6), //leftClaw
			new Talon(7), //rightClaw
			new DigitalInput(4), //toteTouchingLS
			new DigitalInput(5), //leftFrontLS
			new DigitalInput(6), //rightFrontLS
			new DigitalInput(7), //leftBackLS
			new DigitalInput(8) //rightBackLS
	);
	private Lift lift = new Lift(
			new Talon(4),  //motor
			new Talon(5),  //motor2
			new Encoder(14, 15), //Encoder 
			new DigitalInput(2), //topLimit
			new DigitalInput(3) //bottomLimit/zeroLimit
	);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	private RunningComponent[] components = { 
			driveTrain,
			claw, 
			lift,
			dipSwitches,
			new UltraSonics(
					new AnalogInput(1)), //ultrasonics
			
			new Camera("cam0"), //cam name on roborio

			new TapeShooter(
					new Talon(8))
			
		};
	@Override
	public void robotInit() {
		Robot.instance = this;
	}
	
	public static boolean transportCan=false;
	public static boolean transportTote=false;
	public static boolean isGripped =true; //will change initial condition once we get multiple autonomous code/Dip switches working
	public static boolean liftCan = false;
	/**
	 * This function is called periodically during autonomous
	 */
	
	public static final Timer timer = new Timer();
	@Override
	public void autonomousInit() {
		
		timer.start();
		timer.reset();
		driveTrain.resetGyro(); //get gyro ready
		
		angle = 90;
		turnStrafeSpeed = -.55;
		if(dipSwitches.turnBackwardsAndPickUpCan() || dipSwitches.turnBackwardsAndPickUpTote()){
			autoPhase = 0;
		} else if(dipSwitches.none())
			autoPhase = Integer.MIN_VALUE; //hi Brandon Cheng
		else if (dipSwitches.getBoth()) 
			autoPhase = -5;
		
		if (dipSwitches.turnBackwardsAndPickUpTote()){
			angle *= -1; //swap the stuff
			turnStrafeSpeed *= -1;
		}
		totalTime = 0;
	}
	
	private int autoPhase = 0;
	private int angle;
	
	private double totalTime = 0;
	private double turnStrafeSpeed;
	@Override
	public void autonomousPeriodic() {
		//this is supposed to have dumb indentation.
		switch(autoPhase) {
		case -5: //.3
			claw.moveClaw(Claw.DIRECTION_CLOSE);
			descendIfReady(.3);
			if(claw.isTouchingTote())
				descendIfReady(0);
			break;
		case -4: //3.8
			claw.moveClaw(Claw.DIRECTION_STOP); //stop the claw
			lift.moveMotor(Lift.DIRECTION_UP); //make lift go up
			descendIfReady( 7.4 /* inches */ * (Lift.INCHES_PER_SECOND) );
			break;
		case -3: //4.2
			lift.moveMotor(Lift.DIRECTION_STOP); //stop lift
			driveTrain.drive(0, -.65, 0); //go forward the length of .75 totes (hopefully)
			descendIfReady(.4);
			break;
		case -2:
			//driveTrain.drive(0, .4, 0);
			//descendIfReady(.2);
			descendIfReady(0);
			break;
		case -1: //9.2
			driveTrain.drive(0, 0, 0); //stop drive
			lift.moveMotor(Lift.DIRECTION_DOWN); //put the lift down
			claw.moveClaw(Claw.DIRECTION_OPEN); //open the claw too
			descendIfReady(8); //go down for 5
			if(lift.zeroLimit.get()) 
				descendIfReady(0); //or if we're at the bottom, we're done
			break;
		case 0: //.3 || 9.5
			claw.moveClaw(Claw.DIRECTION_CLOSE); //close the claw
			descendIfReady(.5);
			if(claw.isTouchingTote())
				descendIfReady(0);
			break;
		case 1: //1.3 || 10.5
			claw.moveClaw(Claw.DIRECTION_STOP); //stop the claw
			lift.moveMotor(Lift.DIRECTION_UP); //make lift go up
			descendIfReady(1.5);
			break;
		case 2: //3.3 || 12.5
			lift.moveMotor(Lift.DIRECTION_STOP); //stop lift
			driveTrain.PIDdrive(turnStrafeSpeed, 0, angle, .8); //turn with PID
			descendIfReady(2);
			break;
		case 3: //4.8 || 14
			driveTrain.PIDdrive(0, .8, angle); //go backwards
			descendIfReady(1.5);
			break;
		case 4: //5.3 || 14.5
			driveTrain.PIDdrive(0, .4, angle); //go back, but slow like
			descendIfReady(.5);
			break;
		case 5: // =========
			driveTrain.PIDdrive(0, 0, angle);
			break;
		default:
			break; //do nothing				
		}
		
	}

	private void descendIfReady(double d) {
		if (timer.get() > d){
			autoPhase++;
			System.out.println(String.format("%f0.3 | %f0.3", (totalTime += timer.get()), timer.get()));
			timer.reset();
		}
	}
	
	@Override
	public void teleopPeriodic() {
		for (int i = 0; i < components.length; i++) {
			components[i].teleop(driver, aux);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		for (int i = 0; i < components.length; i++) {
			components[i].test(driver);
		}
	}

  
}
