package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Encoder;

public class Encoders {
	Encoder motoren;
	final double rate= 5; //pulses for rotation
	final double radius = 5; //radius of wheel
	double distance;
	double pulse;
	
	Encoders (int a, int b){
		motoren= new Encoder (a,b); //aChannel, bChannel
		motoren.reset();
		//start encoder/ start counting pulses
	}
	
	double GetDistance(){
		distance = motoren.get()/rate;
		distance = distance*radius*2*3.141592653589;
		return distance;
	}
	
	
	//for testing purposes
	double GetPulse(){
		pulse= motoren.get();
		return pulse;
	}
	//free(); <- Ticking Time Bomb
}