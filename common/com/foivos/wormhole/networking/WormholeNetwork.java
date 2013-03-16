package com.foivos.wormhole.networking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Coord;

public class WormholeNetwork {
	public List<NetworkedInventory> inventories = new ArrayList<NetworkedInventory>();
	
	World world;
	
	private WormholeNetwork() {}
	
	public WormholeNetwork(World world, int x, int y, int z) {
		Set<Coord> explored = new TreeSet<Coord>();
		Queue<Coord> q = new LinkedList<Coord>();
		((TileNetwork)world.getBlockTileEntity(x, y, z)).network = this;
		q.add(new Coord(x,y,z));
		explored.add(new Coord(x,y,z));
		Coord coord;
		while((coord = q.poll()) != null) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				Coord newCoord = coord.move(dir);
				if(explored.contains(newCoord))
					continue;
				explored.add(newCoord);
				TileEntity tile = world.getBlockTileEntity(newCoord.x, newCoord.y, newCoord.z);
				if(tile == null)
					continue;
				if(tile instanceof IInventory) {
					inventories.add(new NetworkedInventory(tile));
				}
				
				if(tile instanceof TileNetwork) {
					((TileNetwork)tile).network = this;
					q.add(newCoord);
					
				}
				
			}
		}
	}
	
	@Override
	public String toString() {
		return inventories.toString();
	}
	
}
