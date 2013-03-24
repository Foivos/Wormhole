package com.foivos.wormhole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.minecraft.server.MinecraftServer;

public class Logger {
	
	static private File file;
	static {
		int i=0;
		for(file = new File(getDir(), "log.txt"); file.exists(); file = new File(getDir(), "log"+ i++ + ".txt"));
		try {
			file.createNewFile();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String s) {
		try {
			PrintWriter fout = new PrintWriter(new FileWriter(file,true));
			fout.append(s);
			fout.print("\n");
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File getDir() {
		MinecraftServer server = MinecraftServer.getServer();
		if(server.worldServers.length < 1)
			return null;
		File dir = server.worldServers[0].getChunkSaveLocation();
		try {
			if(dir.getName().contains("DIM"))
			{
				dir = dir.getParentFile();
			}
			dir = new File(dir, "Wormhole");
			dir.mkdir();
			if(!dir.exists())
				throw new Exception();
			return dir;
		}
		catch(Exception e) {
			System.out.println("Failed to get Wormhole save directory");
		}
		return null;
	}

}
