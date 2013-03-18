package com.foivos.wormhole.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Coord;
import com.foivos.wormhole.Place;
import com.foivos.wormhole.transport.TileWormholeTube;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;



public class WormholeNetwork {
	
	public List<NetworkedInventory> inventories = new ArrayList<NetworkedInventory>();
	public List<Place> tiles = new ArrayList<Place>();
	
	public int id;
	public int x, y, z;
	public int world;

	public boolean activated = false;

	
	public WormholeNetwork(int id, int world, int x, int y, int z) {
		this.id = id;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		activate();
	}

	public WormholeNetwork(int id, DataInputStream stream) throws IOException {
		this.id=id;
		System.out.println("Loading network: "+this.id);
		this.world = stream.readInt();
		this.x = stream.readInt();
		this.y = stream.readInt();
		this.z = stream.readInt();
		this.activated = true;
		int tileCount = stream.readInt();
		for(int i=0;i<tileCount;i++) {
			int world = stream.readInt();
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			System.out.println("Loading tile: (" + world+ ", "+z+ ", "+y+ ", "+z+ ")");
			tiles.add(new Place(world, x, y, z));
		}
		int invCount = stream.readInt();
		for(int i=0;i<invCount;i++) {
			int world = stream.readInt();
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			System.out.println("Loading inventory: (" + world+ ", "+x+ ", "+y+ ", "+z+ ")");
			
			inventories.add(new NetworkedInventory(world, x, y, z));
		}
		
	}

	public void activate() {System.out.println("activating");
		World world = DimensionManager.getWorld(this.world);
		Set<Coord> explored = new TreeSet<Coord>();
		Queue<Coord> q = new LinkedList<Coord>();
		TileEntity t = world.getBlockTileEntity(x, y, z);
		if (t == null || !(t instanceof TileNetwork))
			return;
		((TileNetwork) t).network = id;
		((TileNetwork) t).activated = true;
		q.add(new Coord(x, y, z));
		if(!world.isRemote)
			tiles.add(new Place(this.world, x, y, z));
		t.worldObj.markBlockForUpdate(x, y, z);
		explored.add(new Coord(x, y, z));
		Coord coord;
		while ((coord = q.poll()) != null) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				Coord newCoord = coord.move(dir);
				if (explored.contains(newCoord))
					continue;
				explored.add(newCoord);
				TileEntity tile = world.getBlockTileEntity(newCoord.x,
						newCoord.y, newCoord.z);
				if (tile == null)
					continue;
				if (tile instanceof IInventory && !world.isRemote) {
					inventories.add(new NetworkedInventory(this.world, newCoord));
				}

				if (tile instanceof TileNetwork	&& !((TileNetwork) tile).activated) {
					((TileNetwork) tile).network = id;
					((TileNetwork) tile).activated = true;
					q.add(newCoord);
					tile.worldObj.markBlockForUpdate(newCoord.x, newCoord.y, newCoord.z);
					if(!world.isRemote)
						tiles.add(new Place(this.world, newCoord));

				}

			}
		}
		activated = true;

	}

	public void deactivate() {System.out.println("deactivating");
		World world = DimensionManager.getWorld(this.world);
	
		activated = false;
		inventories.clear();
		List<TileWormholeTube> tubeList = new ArrayList<TileWormholeTube>();
		for (Coord coord : tiles) {
			TileEntity tile = world.getBlockTileEntity(coord.x, coord.y, coord.z);
			if(tile != null && tile instanceof TileNetwork) {
				((TileNetwork) tile).activated = false;
				if(tile instanceof TileWormholeTube)
					tubeList.add((TileWormholeTube)tile);
			}
			world.markBlockForUpdate(coord.x, coord.y, coord.z);
		}
		tiles.clear();
		for(TileWormholeTube tile : tubeList) {
			tile.updateConnections();
		}
	}


	public void toggle() {
		if (activated)
			deactivate();
		else
			activate();
	}



	public void validate(int x, int y, int z) {
		World world = DimensionManager.getWorld(this.world);
		for (Coord coord : tiles) {
			if (coord.distance2(x, y, z) > 1)
				continue;
			TileEntity tile = world.getBlockTileEntity(coord.x, coord.y,
					coord.z);
			if (tile == null || !(tile instanceof TileNetwork)) {
				deactivate();
				return;
			}
		}

		for (NetworkedInventory inv : inventories) {
			if (inv.distance2(x, y, z) > 1)
				continue;
			TileEntity tile = world.getBlockTileEntity(inv.x, inv.y, inv.z);
			if (tile == null || !(tile instanceof IInventory)) {
				deactivate();
				return;
			}
		}
	}




	public void readFromNBT(NBTTagCompound tag) {
		id = tag.getInteger("netID");
		world = tag.getInteger("netWorld");
		x = tag.getInteger("netX");
		y = tag.getInteger("netY");
		z = tag.getInteger("netZ");
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("netID", id);
		tag.setInteger("netWorld", world);
		tag.setInteger("netX", x);
		tag.setInteger("netY", y);
		tag.setInteger("netZ", z);
		
	}

	public void write(DataOutputStream stream) throws IOException {
		stream.writeInt(this.id);
		stream.writeInt(this.world);
		stream.writeInt(this.x);
		stream.writeInt(this.y);
		stream.writeInt(this.z);
		stream.writeInt(tiles.size());
		for(Place p : tiles) {
			stream.writeInt(p.world);
			stream.writeInt(p.x);
			stream.writeInt(p.y);
			stream.writeInt(p.z);
			System.out.println("Saving tile: ("+ p.world+ ", "+p.x+ ", "+p.y+ ", "+p.z+ ")");
			
		}
		stream.writeInt(inventories.size());
		for(Place p : inventories) {
			stream.writeInt(p.world);
			stream.writeInt(p.x);
			stream.writeInt(p.y);
			stream.writeInt(p.z);
			System.out.println("Saving inventory: ("+ p.world+ ", "+p.x+ ", "+p.y+ ", "+p.z+ ")");
		}
		
	}


}
