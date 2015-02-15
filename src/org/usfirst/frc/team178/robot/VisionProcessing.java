package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.vision.AxisCamera;



public class VisionProcessing implements RunningComponent {
	Joystick joystick;
	
	AxisCamera camera;
	ColorImage CImage;
	BinaryImage BImage;
	ParticleAnalysisReport[] repo;
	
	public VisionProcessing(Joystick joystick, AxisCamera camera) {
		super();
		this.joystick = joystick;
		this.camera = camera;
	}

	
	
	
	Boolean Capture(){
		return camera.getImage(CImage);
	}
	
	void Filter(int hueLow, int hueHigh, int saturationLow, int saturationHigh, int valueLow, int valueHigh, int erosion){
		try{
			BImage = CImage.thresholdHSV(hueLow, hueHigh, saturationLow, saturationHigh, valueLow, valueHigh);
			if(erosion>0){	
				BImage = BImage.removeSmallObjects(true, erosion);
			}
		}
		catch(NIVisionException ex){
			System.out.println("Error 00011 Type: Binary Fail");
		}
	}
	
	void Report(){
		try{
			repo = BImage.getOrderedParticleAnalysisReports();
			System.out.println("Awsome:  " +  repo[0].center_mass_x);
		}
		catch(NIVisionException ex){
			System.out.println("Error 01111 Type: Report Fail");
		}
	}
	
	// require additional changes::
	//    set 2 systems for... center of max now && later
	void Analysis(int hueLow, int hueHigh, int saturationLow, int saturationHigh, int valueLow, int valueHigh, int erosion){
		try{
			Report();
			int init = repo[0].center_mass_x;
			int initTest = 0;
			for(int i = 1; (initTest != init && (true) )|| i > 20; i++){
				Report();
				if(repo[0].center_mass_x != init){
					saturationLow++;
					Filter(hueLow,  hueHigh, saturationLow, saturationHigh, valueLow, valueHigh, 0);
				}
				else
				{
					Filter(hueLow,  hueHigh, saturationLow, saturationHigh, valueLow, valueHigh, 0);
					Report();
					initTest= repo[0].center_mass_x;
				}
				BImage = CImage.thresholdHSV(hueLow, hueHigh, saturationLow, saturationHigh, valueLow, valueHigh);
			}
			if(erosion>0){	
				BImage = BImage.removeSmallObjects(true, erosion);
			}
		}
		catch(NIVisionException ex){
			System.out.println("Error 11111 Type: Analysis Fail");
		}
	}
    
    void Push(String FileName){
    	try{
			BImage.write(FileName);
		}
		catch(NIVisionException ex){
			System.out.println("Error 00111 Type: Print Fail");
		}
    }
    
    public void teleop(){
    	if(joystick.getRawButton(8)){
    		Capture();
    		Filter(70,100,70,100,70,100,1);
    		Report();
    		Push("Apples");
    		
    	}
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
