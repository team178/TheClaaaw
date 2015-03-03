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
	
	
	/**auto flags
	all are false until the chain is initialized by @Link autonomousPeriodic()*/
	public static boolean inAuto=false,
			isCanHeld=false,
			isCaninAZB=false,
			isCaninAZA=false,
			isBotClearofAZ=false,
			isBotAlligned=false,
			isBotReadyToGrab=false,
			isToteHeld=false,
			isToteinAZ=false,
			isCanReleased=false,
			isBotMovedBack=false;	

	public static boolean isDown;
	
}
