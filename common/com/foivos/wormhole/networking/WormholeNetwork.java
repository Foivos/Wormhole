package com.foivos.wormhole.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
			addInventory(new NetworkedInventory(world, x, y, z, side));
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
				inventories.remove(new NetworkedInventory(place.toSpot(), dir.ordinal() ^ 1));
				continue;
			}
			NetworkedInventory inv = new NetworkedInventory(p.toSpot(), dir.ordinal() ^1);
			if(!inventories.contains(inv))
				addInventory(inv);
				
		}
	}




	private void addInventory(NetworkedInventory inv) {
		Set<Spot> tileSet = new TreeSet(tiles);
		
		
		Set<Spot> explored = new TreeSet<Spot>();
		Queue<DistSpot> toSearch = new LinkedList<DistSpot>();
		DistSpot distInv = new DistSpot(inv);
		distInv.distance = 0;
		explored.add(inv);
		toSearch.add(distInv);
		DistSpot spot;
		while((spot = toSearch.poll()) != null) {
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				DistSpot newSpot = spot.move(dir);
				
				if(explored.contains(newSpot))
					continue;
				explored.add(newSpot);
			
				if(tileSet.contains(newSpot)) {
					toSearch.add(newSpot);
					continue;
				}
				for(NetworkedInventory target : inventories) {
					if(newSpot.equals(target) && (dir.ordinal() ^ 1) == target.side){
						inv.distances.put(target, newSpot.distance);
						target.distances.put(inv, newSpot.distance);
						break;
					}
				}
				
				
			}
			
		}
		inventories.add(inv);
		
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
		List<Integer>[] changed = new List[inventories.size()];
		for(int i=0; i<inventories.size(); i++) {
			changed[i] = inventories.get(i).getChangedSlots();
		}
		
		PriorityQueue<Transaction> transactions = new PriorityQueue<Transaction>();
		for(int i=0; i<inventories.size(); i++) {
			NetworkedInventory inv = inventories.get(i);
			if(changed[i].size() == 0)
				continue;
			IInventory tile = inv.getTile();
			for(int j : changed[i]) {
				ItemStack stack = tile.getStackInSlot(j);
				for(NetworkedInventory target : inventories) {
					if(stack == null) {
						Transaction transaction = new Transaction(inv, target, j, 0, inv.getDistance(target));
						transactions.add(transaction);
						continue;
					}
					int result = Utils.whoWants(inv, target, stack);
					if(result != 0) {
						Transaction transaction = new Transaction(inv, target, j, result, inv.getDistance(target));
						transactions.add(transaction);
					}
				}
			}
		}
		Transaction transaction;
		while((transaction = transactions.poll()) != null) {
			if(transaction.weight > 0) {
				ItemStack stack = transaction.origin.getStack(transaction.slot);
				if(stack == null)
					continue;
				ItemStack leftover = transaction.target.put(stack);
				if(leftover.stackSize <= 0)
					inv.getTile().setInventorySlotContents(transaction.slot, null);
				continue;
			}
			if(transaction.weight < 0) {
				ItemStack stack = transaction.origin.getStack(transaction.slot);
				if(stack == null || stack.stackSize >= stack.getMaxStackSize())
					continue;
				List<Integer> slots = transaction.target.findStacks(stack);
				for(int slot : slots) {
					if(stack.stackSize >= stack.getMaxStackSize())
						break;
					ItemStack targetStack = transaction.target.getStack(slot);
					targetStack = transaction.origin.put(targetStack);
					if(targetStack.stackSize <= 0);
					transaction.target.getTile().setInventorySlotContents(slot, null);
				}
				continue;
			}
								
			
		}
		
		
		return true;
	}


	private class DistSpot extends Spot { 
		public DistSpot(Spot spot) {
			super(spot);
		}
		
		@Override
		public DistSpot move(ForgeDirection dir) {
			DistSpot result = new DistSpot(super.move(dir));
			result.distance = this.distance + 1;
			return result;
			
		}

		public int distance;
	}
	




}
