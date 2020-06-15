package com.romel;

public class WatcherRunner {

	public static void main(String[] args) {
		
		//Pass the full path of the directory to watch, file to watch and the control file to the Watcher7 constructor.
		Watcher7 watch7 = new Watcher7("/users/kubi/documents/temp_watch_dir", "employees.xml", "terminateWatch.txt");
		watch7.startWatcher();
		
		System.out.println("-Back in main. Program terminated.");

	}

}
