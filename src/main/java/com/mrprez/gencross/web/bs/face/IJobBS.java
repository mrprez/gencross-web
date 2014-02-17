package com.mrprez.gencross.web.bs.face;




public interface IJobBS {
	public static final String SEND_TRIGGER_NAME = "send-personnage-simple-trigger";
	public static final String TRIGGER_GROUP_NAME = "GENCROSS_GROUP";
	public static final String SEND_JOB_NAME = "send-personnage-job";
	public static final String JOB_GROUP_NAME = "GENCROSS_GROUP";
	
	
	
	void scheduleSender() throws Exception;

	boolean isSenderRunning() throws Exception;


}
