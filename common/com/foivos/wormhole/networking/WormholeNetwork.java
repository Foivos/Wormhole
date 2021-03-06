package com.foivos.wormhole.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Place;
import com.foivos.wormhole.Spot;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.transport.TileWormhole;



public class WormholeNetwork {
	
	public List<NetworkedInventory> inventories = new ArrayList<NetworkedInventory>();
	public List<Spot> tiles = new ArrayList<Spot>();

	public Spot base;
	public byte color;
	public long lastUpdate=-1;

	
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


	public WormholeNetwork(Spot base, DataInputStream stream) throws IOException {
		this.base = base;
		this.color = stream.readByte();
		int tileSize = stream.readInt();
		for(int i=0;i<tileSize;i++) {
			int world = stream.readInt();
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			tiles.add(new Spot(world, x, y, z));
		}
		int invSize = stream.readInt();
		for(int i=0;i<invSize;i++) {
			int world = stream.readInt();
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			byte side = stream.readByte();
			inventories.add(new NetworkedInventory(world, x, y, z, side));
		}
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
		world.getChunkFromBlockCoords(base.x, base.z).isModified=true;
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
					world.getChunkFromBlockCoords(newPlace.x, newPlace.z).isModified=true;
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
			tile.markForUpdate();
		}
		tiles.clear();
		NetworkManager.removeNetwork(base);
	}





	public void changed(Place place) {
		World world = place.world;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			Place p = place.move(dir);
			TileEntity tile = TileManager.getTile(place.move(dir), IInventory.class, true);
			if(tile == null) {
				inventories.remove(place.toSpot());
				continue;
			}
			boolean add = true;
			for(NetworkedInventory inv : inventories) {
				if(inv.equals(place.toSpot()) && (dir.ordinal() ^ 1) == inv.side) {
					add = false;
					break;
				}	
			}
			if(add)
				inventories.add(new NetworkedInventory(place.toSpot(), dir.ordinal() ^ 1));
		}
	}




	public void writeData(DataOutputStream stream) throws IOException {
		stream.writeByte(color);
		stream.writeInt(tiles.size());
		for(Spot tile : tiles) {
			stream.writeInt(tile.world);
			stream.writeInt(tile.x);
			stream.writeInt(tile.y);
			stream.writeInt(tile.z);
		}
		stream.writeInt(inventories.size()); 
		for(NetworkedInventory inv : inventories) {
			stream.writeInt(inv.world);
			stream.writeInt(inv.x);
			stream.writeInt(inv.y);
			stream.writeInt(inv.z);
			stream.writeByte(inv.side);
		}
		
	}


	public boolean update() {
		long time = System.currentTimeMillis();
		if(time-lastUpdate < 500)
			return false;
		lastUpdate = time;
		List<InventoryData> invData = new ArrayList<InventoryData>();
		for(NetworkedInventory inv : inventories) {
			InventoryData data = inv.getData();
			if(data == null)
				continue;
			invData.add(data);
		}
		for(InventoryData data : invData) {
			if(!data.isPulling)
				continue;
			for(Entry<Integer, ItemStack> entry : data.pulled.entrySet()) {
				int index = entry.getKey();
				ItemStack stack = entry.getValue();
				outerLoop:
				for(InventoryData target : invData) {
					if(target.isPulling(stack))
						continue;
					for(int i=target.start; i<target.end; i++) {
						ItemStack targetStack = target.tile.getStackInSlot(i);
						if(targetStack == null || !stack.isItemEqual(targetStack))
							continue;
						int transaction = Math.min(stack.getMaxStackSize() - stack.stackSize, targetStack.stackSize);
						targetStack.stackSize -= transaction;
						stack.stackSize += transaction;
						if(targetStack.stackSize <= 0){
							target.tile.setInventorySlotContents(i, null);
							target.slotChanged(i);
						}
						if(stack.stackSize >= stack.getMaxStackSize())
							break outerLoop;
							
					}
				}
			}
			outerLoop:
			for(InventoryData target : invData) {
				for(int i=target.start; i<target.end; i++) {
					if(data.emptySlots.isEmpty())
						break outerLoop;
					ItemStack targetStack = target.tile.getStackInSlot(i);
					if(targetStack == null || target.isPulling(targetStack) || !data.isPulling(targetStack))
						continue;
					int index = data.emptySlots.get(data.emptySlots.size() - 1);
					data.tile.setInventorySlotContents(index, targetStack);
					data.slotChanged(index);
					target.tile.setInventorySlotContents(i, null);
					target.slotChanged(i);
						
				}
			}
			
		}
		return true;
	}



	




}
