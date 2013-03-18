package com.foivos.wormhole.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Coord;

public class NetworkManager {
	
	public static Map<Integer, WormholeNetwork> networks = new TreeMap<Integer, WormholeNetwork>();
	private static int nextID = 0;
	
	private static WormholeNetwork getNetwork(int network) {
		return networks.get(network);
	}

	private static void removeNetwork(int key) {
		networks.remove(key);
		saveNetworks();
		
	}

	private static void addNetwork(World world, int x, int y, int z) {
		networks.put(nextID, new WormholeNetwork(nextID, world.getWorldInfo().getDimension(), x, y, z));
		nextID++;
		saveNetworks();
	}
	
	public static void loadNetworks() {
		
	}
	
	public static void saveNetworks() {
		
	}

	public static void toggleNetwork(World world, int x, int y, int z) {
		if(world.isRemote)
			return;
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity == null || !(tileEntity instanceof TileNetwork))
			return;
		TileNetwork tile = (TileNetwork) tileEntity;
		WormholeNetwork network = getNetwork(tile.network);	
		if(!tile.activated || network == null) {
			addNetwork(world, x, y, z);
			nextID++;
		}
		else {			
			network.deactivate();
			removeNetwork(tile.network);
		}
	}

	public static boolean isActiveNetwork(int id) {
		WormholeNetwork network = getNetwork(id);
		return network != null && network.activated;
	}
	
	public static void validate(int id, World world, int x, int y, int z) {
		WormholeNetwork network = getNetwork(id);
		if(network == null)
			return;
		for(Coord coord : network.inventories) {
			if(coord.distance2(x, y, z) != 1)
				continue;
			TileEntity tile = world.getBlockTileEntity(coord.x, coord.y, coord.z);
			if(tile == null || !(tile instanceof IInventory)) {
				network.deactivate();
				return;
			}
		}
		
		for(Coord coord : network.tiles) {
			if(coord.distance2(x, y, z) != 1)
				continue;
			TileEntity tile = world.getBlockTileEntity(coord.x, coord.y, coord.z);
			if(tile == null || !(tile instanceof TileNetwork)) {
				network.deactivate();
				return;
			}
		}
		
		
		
			
	}

	public static void readNetworks(DataInputStream stream) throws IOException {
		nextID = stream.readInt();
		int size = stream.readInt();
		for(int i=0;i<size;i++) {
			int id = stream.readInt();
			networks.put(i, new WormholeNetwork(id, stream));
		}
				
	}

	public static void writeNetworks(DataOutputStream stream) throws IOException {
		stream.writeInt(nextID);
		for(WormholeNetwork network : networks.values()) {
			if(!network.activated)
				networks.remove(network.id);
		}
		stream.writeInt(networks.size());
		for(WormholeNetwork network : networks.values()) {
			network.write(stream);
		}
		
	}
			

	
}
