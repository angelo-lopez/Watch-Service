package com.romel;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;

/**
 * A Java program that monitors a path/directory and detects whether a relevant file has been created or deleted. The watch service terminates
 * when it detects the creation of a designated file or when the path/directory has been deleted.
 * @author kubi
 *
 */
public class Watcher7 {

	private String watchDir;
	private String watchFile;
	private String watchTerminateFile;
	
	public Watcher7(String watchDir, String watchFile, String watchTerminateFile) {
		this.watchDir = watchDir; //The directory to watch.
		this.watchFile = watchFile; //The file to watch.
		this.watchTerminateFile = watchTerminateFile; //Control file. The watch service terminates when the creation of this file is detected
													  // within the directory that is being watched.
	}
	
	public void startWatcher() {
	
		try {
			URI uri = new URI(watchDir);
			Path path = Paths.get(uri.getPath());
			WatchService watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
			
			boolean isValid = true;
			
			System.out.println("-Watch service started for -> Path: " + path.toString() + ", File: " + watchFile);
			
			for(;;) {
				WatchKey watchKey = watchService.take();
				
				for(WatchEvent<?> watchEvent : watchKey.pollEvents() ) {
					if(StandardWatchEventKinds.ENTRY_CREATE.equals(watchEvent.kind())) {
						if(watchEvent.context().toString().contentEquals(watchTerminateFile)) {
							System.out.println("-Terminating the watch service...");
							watchKey.cancel(); //Invalidate the watch key to exit the infinite loop.
						}
						else if(watchEvent.context().toString().contentEquals(watchFile)) {
							System.out.println("-Watch file creation detected -> " + watchFile);
						}
						else {
							System.out.println("-Invalid file creation detected -> " + watchEvent.context().toString());
						}
					}
					else if(StandardWatchEventKinds.ENTRY_DELETE.equals(watchEvent.kind())) {
						System.out.println("-File deletion detected -> " + watchEvent.context().toString());
					}
				}
				
				isValid = watchKey.reset();
				
				if(!isValid) {
					System.out.println("-Watch service terminated.");
					break;
				}
			}
		}
		catch(URISyntaxException uriEx) {
			uriEx.printStackTrace();
		}
		catch(IOException ioEx) {
			ioEx.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	
	}
	
}
