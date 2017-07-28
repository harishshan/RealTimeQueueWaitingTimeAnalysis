package com.harish.rtqwta.util;

import java.util.Date;

public class CommonUtil {
	public static String getAverageTime(long averageTime){
		long seconds = averageTime % 60;
		long minutes = averageTime / 60;
		return minutes+":"+seconds;
	}
	public static String getTreatmentTime(Date treatmentStartTS, Date treamentCompleteTS){
		long treatmentStart=0, treatmentComplete=0,treatmentTime=0;
		if(treatmentStartTS!=null){
			treatmentStart = treatmentStartTS.getTime()/1000;
			if(treamentCompleteTS!=null){
				treatmentComplete=treamentCompleteTS.getTime()/1000;
				treatmentTime=treatmentComplete - treatmentStart;
			}else{
				return "-";
			}
		}else{
			return "-";
		}
		return  getAverageTime(treatmentTime);
	}
	public static long getTreatmentTimeLong(Date treatmentStartTS, Date treamentCompleteTS){
		long treatmentStart=0, treatmentComplete=0,treatmentTime=0;
		if(treatmentStartTS!=null){
			treatmentStart = treatmentStartTS.getTime()/1000;
			if(treamentCompleteTS!=null){
				treatmentComplete=treamentCompleteTS.getTime()/1000;
				treatmentTime=treatmentComplete - treatmentStart;
			}
		}
		return treatmentTime;
	}
}