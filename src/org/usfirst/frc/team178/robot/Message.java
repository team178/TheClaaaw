package org.usfirst.frc.team178.robot;

public class Message {
	//booleans for Deck-Lift Safety
	/*
	public static boolean makeLiftSafe;
	public static boolean makeDeckSafe;
	public static boolean isLiftSafe;
	public static boolean isDeckSafe;
	*/
	
	//booleans for Autonomous Commands
	
	public static boolean 
			isBotReadyToGrab=false, //ultrasonics in range
			isCanHeld= false, //ToteTouchingLS are triggered
			isItemLifted= false, //Lift tote off the ground; move lift up
			isBotEnteringAuto= false,
			isCanReleased=false,
			isBotAlligned=false;
	
	public static boolean auto1Init=false;
	/*
	 * 1. Move Can
	 * 2. Move Tote
	 * 3. End in Autozone
	 */
	
	public static boolean auto2Init=false;
	
	/**auto flags
	all are false until the chain is initialized by @Link autonomousPeriodic()*/
	public static boolean 
			isCaninAZB=false,
			isCaninAZA=false,
			isBotClearofAZ=false,
			isToteHeld=false,
			isToteinAZ=false,
			isBotMovedBack=false;	

	public static boolean isDown;
	
}
