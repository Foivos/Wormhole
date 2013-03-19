package com.foivos.wormhole.networking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Coord;
import com.foivos.wormhole.Place;
import com.foivos.wormhole.Spot;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.transport.TileWormhole;



public class WormholeNetwork {
	
	public List<NetworkedInventory> inventories = new ArrayList<NetworkedInventory>();
	public List<Spot> tiles = new ArrayList<Spot>();

	public Spot base;
	public byte color;

	
	public WormholeNetwork(int world, int x, int y, int z, byte color) {
		this.base = new Spot(world, x, y ,z);
		this.color = color;
		activate();
	}

	
	public WormholeNetwork(Spot spot, byte color) {
		this.base = spot;
		this.color = color;
		activate();
	}


	public WormholeNetwork(Spot base, byte color, NBTTagCompound tag) {
		this.color = color;
		this.base = base;
		readFromNBT(tag);
	}


	public void activate() {System.out.println("activating");
		explore(base.toPlace());
	}
	
	public void explore(Place base) {
		World world = base.world;
		TileWormhole baseTile = (TileWormhole) TileManager.getTile(base, TileWormhole.class, true);
		if(baseTile == null)
			return;
		baseTile.color = this.color;
		baseTile.base = this.base;
		tiles.add(base.toSpot());
		world.markBlockForUpdate(base.x, base.y, base.z);
		Set<Place> explored = new TreeSet<Place>();
		Queue<Place> toSearch = new LinkedList<Place>();
		explored.add(base);
		toSearch.add(base);
		Place place;
		while((place = toSearch.poll()) != null) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				Place newPlace = place.move(dir);
				if(explored.contains(newPlace))
					continue;
				explored.add(newPlace);
				TileEntity tile = TileManager.getTile(newPlace, true);
				if(tile == null)
					continue;
				if(tile instanceof TileWormhole) {
					if(((TileWormhole)tile).color != 0 && ((TileWormhole)tile).color != color) {
						continue;
					}
					((TileWormhole)tile).color = this.color;
					((TileWormhole)tile).base = this.base;
					tiles.add(newPlace.toSpot());
					toSearch.add(newPlace);
					world.markBlockForUpdate(newPlace.x, newPlace.y, newPlace.z);
					continue;
				}
				if(tile instanceof IInventory) {
					inventories.add(new NetworkedInventory(newPlace.toSpot(), dir.ordinal()^1));
				}
				
				
			}
			
		}
		
	}

	public void deactivate() {System.out.println("deactivating");
	
		inventories.clear();
		for (Spot spot : tiles) {
			Place place = spot.toPlace();
			TileWormhole tile = (TileWormhole) TileManager.getTile(place, TileWormhole.class, true);
			if(tile == null)
				continue;
			tile.color = 0;
			place.world.markBlockForUpdate(place.x, place.y, place.z);
		}
		tiles.clear();
	}





	public void update(Place place) {
		World world = place.world;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			Place p = place.move(dir);
			TileEntity tile = TileManager.getTile(place.move(dir), true);
			if(tile == null) {
				inventories.remove(place.toSpot());
			}
		}
	}




	public void readFromNBT(NBTTagCompound tag) {
		int world = tag.getInteger("netWorld");
		int x = tag.getInteger("netX");
		int y = tag.getInteger("netY");
		int z = tag.getInteger("netZ");
		base = new Spot(world, x, y ,z);
		NBTTagList tagList = tag.getTagList("tiles");
		int size = tagList.tagCount();
		for(int i=0;i<size;i++) {
			NBTTagCompound tagCompound = (NBTTagCompound) tagList.tagAt(i);
			world = tagCompound.getInteger("world");
			x = tagCompound.getInteger("x");
			y = tagCompound.getInteger("y");
			z = tagCompound.getInteger("z");
			tiles.add(new Spot(world, x, y, z));
		}
	}

	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("netWorld", base.world);
		tag.setInteger("netX", base.x);
		tag.setInteger("netY", base.y);
		tag.setInteger("netZ", base.z);
		NBTTagList tagList = new NBTTagList();
		for(Spot tile : tiles) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("world", tile.world);
			tagCompound.setInteger("x", tile.x);
			tagCompound.setInteger("y", tile.y);
			tagCompound.setInteger("z", tile.z);
			tagList.appendTag(tagCompound);
		}
		tag.setTag("tiles", tagList);
	}

	




}
