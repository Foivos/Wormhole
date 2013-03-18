package com.foivos.wormhole;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.foivos.wormhole.networking.NetworkManager;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;

public class WormholeSaveHandler {
	
	@ForgeSubscribe
	public void onWorldLoad(Load event)
	{
		if(!(event.world instanceof WorldServer))
			return;
		File dir = getSaveDir((WorldServer)event.world);
		try {
			File file = new File(dir, "data.dat");
			FileInputStream fin = new FileInputStream(file);
			DataInputStream stream = new DataInputStream(fin);
			NetworkManager.readNetworks(stream);
			stream.close();
			fin.close();
			
			
		} catch (Exception e) {
			System.out.println("Failed to read Wormhole data.");
		}
		
	}
	
	@ForgeSubscribe
	public void onWorldSave(Save event)
	{
		if(!(event.world instanceof WorldServer))
			return;
		File dir = getSaveDir((WorldServer)event.world);
		try {
			File file = new File(dir, "data.dat");
			file.createNewFile();
			FileOutputStream fout = new FileOutputStream(file);
			DataOutputStream stream = new DataOutputStream(fout);
			NetworkManager.writeNetworks(stream);
			stream.close();
			fout.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getSaveDir(WorldServer world) {
		File dir = world.getChunkSaveLocation();
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
