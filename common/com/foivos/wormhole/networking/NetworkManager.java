package com.foivos.wormhole.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.server.MinecraftServer;

import com.foivos.wormhole.Spot;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class NetworkManager {
	
	
	//I think this might be accessible from Minecraft 
	public static MinecraftServer server;
	private static Map<Spot, WormholeNetwork> networks = new TreeMap<Spot, WormholeNetwork>();
	
	static byte b;
	static long lastSave=-1;
	
	public NetworkManager(){}
	
	public static void serverStarting(FMLServerStartingEvent event) {
		if(event.getServer() == null)
			return;
		if(event.getServer() == server)
			return;
		server = event.getServer();
		load();
		
	}
	
	public static void serverStopping(FMLServerStoppingEvent event) {
		save();
	}
	
	private static void load() {
		try {
			File file = getSaveFile();
			if(file == null)
				throw new Exception("Unable to locate save file");
			FileInputStream fin = new FileInputStream(file);
			DataInputStream stream = new DataInputStream(fin);
			System.out.print("Reading Wormhole data from "+ file.getCanonicalPath()+"...");
			readNetworks(stream);
			System.out.println(" done");
			fin.close();
			
		} catch(Exception e) {
			System.out.println("Error while loading Wormhole data");
			e.printStackTrace();
		}
	}
	
	private static void save() {
		try {
			swapSaveFile();
			File file = getSaveFile();
			FileOutputStream fout = new FileOutputStream(file);
			DataOutputStream stream = new DataOutputStream(fout);
			System.out.print("Writing Wormhole data to " + file.getCanonicalPath()+"...");
			writeNetworks(stream);
			System.out.println(" done");
			fout.close();
		}
		catch(Exception e) {
			System.out.println("Error while saving Wormhole data");
			e.printStackTrace();
		}
	}
		
	
	private static void swapSaveFile() throws IOException {
		b ^= 1;
		File dir = getSaveDir();
		File saveData = new File(dir, "save.dat");
		FileOutputStream fout = new FileOutputStream(saveData);
		DataOutputStream stream = new DataOutputStream(fout);
		stream.writeByte(b);
		fout.close();
		
	}
	
	private static File getSaveFile() {

		try {
			File dir = getSaveDir();
			File saveData = new File(dir, "save.dat");
			if(!saveData.exists()) {
				saveData.createNewFile();
				return null;
			}
			FileInputStream fin = new FileInputStream(saveData);
			DataInputStream s = new DataInputStream(fin);
			b = s.readByte();
			fin.close();

			File saveFile = new File(dir, "save"+b+".dat");
			if(!saveFile.exists()) {
				saveFile.createNewFile();
			}
			
			return saveFile;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	private static File getSaveDir() {
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
	
	private static void readNetworks(DataInputStream stream) throws IOException {
		int size = stream.readInt();
		for(int i=0;i<size;i++) {
			int world = stream.readInt();
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			Spot base = new Spot(world, x, y, z);
			networks.put(base, new WormholeNetwork(base, stream));
		}
	}
	
	private static void writeNetworks(DataOutputStream stream) throws IOException {
		stream.writeInt(networks.size());
		for(Entry<Spot,WormholeNetwork> entry : networks.entrySet()) {
			Spot base = entry.getKey();
			stream.writeInt(base.world);
			stream.writeInt(base.x);
			stream.writeInt(base.y);
			stream.writeInt(base.z);
			WormholeNetwork network = entry.getValue();
			network.writeData(stream);
		}
	}
	
	public static void updateSaves() {
		long time = System.currentTimeMillis();
		//Save every five minutes
		if(time-lastSave < 300000)
			return;
		save();
		lastSave = time;
	}
	
	public static WormholeNetwork getNetwork(Spot spot) {
		return networks.get(spot);
	}
	
	public static void putNetwork(Spot spot, WormholeNetwork network) {
		networks.put(spot, network);
	}

	public static void deactivate(Spot base) {
		WormholeNetwork network = networks.get(base);
		if(network == null)
			return;
		network.deactivate();
		
		
	}

	public static void removeNetwork(Spot base) {
		networks.remove(base);
		
	}

	public static void update(Spot base) {
		WormholeNetwork network = getNetwork(base);
		if(network != null)
			network.update();
		
	}
	
	
	
}
