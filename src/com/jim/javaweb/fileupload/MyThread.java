package com.jim.javaweb.fileupload;

public class MyThread extends Thread {

    private UploadProgress p;

    public MyThread(UploadProgress p) {
	// TODO Auto-generated constructor stub
	this.p = p;
    }

    public void run() {
	System.out.println("begin run");
	while (p.getPercent() < 1) {
	    System.out.println(p.getPercent());
	    try {
		sleep(500);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
}
