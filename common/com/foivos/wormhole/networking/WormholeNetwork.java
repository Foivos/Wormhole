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
import com.foivos.wormhole.transport.TileWormholeTube;

public class WormholeNetwork {
	public List<NetworkedInventory> inventories = new ArrayList<NetworkedInventory>();
	public List<TileNetwork> tiles = new ArrayList<TileNetwork>();
	
	public World world;
	public int x, y, z;
	public boolean activated;
	
	private WormholeNetwork() {}
	
	public WormholeNetwork(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		activate();
	}
	
	public void activate() {
		Set<Coord> explored = new TreeSet<Coord>();
		Queue<Coord> q = new LinkedList<Coord>();
		TileEntity t = world.getBlockTileEntity(x, y, z);
		if(t == null || !(t instanceof TileNetwork))
			return;
		((TileNetwork)t).network = this;
		q.add(new Coord(x,y,z));
		tiles.add((TileNetwork)t);
		world.markBlockForRenderUpdate(x, y, z);
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
				
				if(tile instanceof TileNetwork && (((TileNetwork)tile).network == null || !((TileNetwork)tile).network.activated)) {
					((TileNetwork)tile).network = this;
					q.add(newCoord);
					tiles.add((TileNetwork)tile);
					world.markBlockForRenderUpdate(newCoord.x, newCoord.y, newCoord.z);
					
				}
				
			}
		}
		activated = true;
		
	}
	
	public void deactivate() {
		activated = false;
		for(TileNetwork tile : tiles) {
			tile.network = null;
			world.markBlockForRenderUpdate(x, y, z);
			if(tile instanceof TileWormholeTube)
				((TileWormholeTube)tile).updateConnections();
		}
	}

	

	@Override
	public String toString() {
		return inventories.toString();
	}

	public void toggle() {
		if(activated)
			deactivate();
		else
			activate();
	}

	public void validate() {
		for(NetworkedInventory inventory : inventories) {
			int x = inventory.tile.xCoord, y = inventory.tile.yCoord, z = inventory.tile.zCoord;
			if(world.getBlockTileEntity(x, y, z) != inventory.tile) {
				deactivate();
				return;
			}
		}
		for(TileNetwork tile : tiles) {
			int x = tile.xCoord, y = tile.yCoord, z = tile.zCoord;
			if(world.getBlockTileEntity(x, y, z) != tile) {
				deactivate();
				return;
			}
		}
		
	}
	
}
