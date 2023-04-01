package httpserver.itf.impl;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import httpserver.itf.HttpSession;

public class Session implements HttpSession{
	private String id;
	private HttpServer hs;
	protected HashMap<String, Object> sessions = new HashMap<String, Object>();
	private Timer timer;
	private TimerTask task;
	
	public Session(String id, HttpServer hs) { 
		this.id= id;
		this.hs = hs;
		Session sessionCourante = this;
		task = new TimerTask() {
			@Override
			public void run() {
				hs.sessionList.remove(sessionCourante);
			}
		};
	    timer = new Timer("Timer");
	    timer.schedule(task, 10000); //10 seconds
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Object getValue(String key) {
		return sessions.get(key);
	}

	@Override
	public void setValue(String key, Object value) {
		sessions.put(key, value);
		
	}
	
	public void newTimer() {
		task.cancel();
	    timer.cancel();
	    timer.purge();
	    Session sessionCourante = this;
	    task = new TimerTask() {
			@Override
			public void run() {
				hs.sessionList.remove(sessionCourante);
			}
		};
	    timer = new Timer("Timer");
	    timer.schedule(task, 10000); //10 seconds
	}

}