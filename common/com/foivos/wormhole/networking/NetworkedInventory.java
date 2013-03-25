package com.foivos.wormhole.networking;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.bouncycastle.asn1.isismtt.ISISMTTObjectIdentifiers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Coord;
import com.foivos.wormhole.Spot;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.transport.TileWormholeManipulator;

public class NetworkedInventory extends Spot{

	public int side;
	public ItemStack[] stacks;
	public ItemSet putting = new ItemSet(false);
	public ItemSet getting = new ItemSet(true);
	public boolean isPulling = false;
	
	WeakReference<IInventory> tile;
	
	public Map<NetworkedInventory, Integer> distances = new HashMap<NetworkedInventory, Integer>();
	
	public NetworkedInventory(Spot spot, int side) {
		super(spot);
		this.side = side;
	}

	public NetworkedInventory(int world, Coord coord, int side) {
		super(world, coord);
	}

	public NetworkedInventory(int world, int x, int y, int z, int side) {
		super(world, x, y, z);
	}

	public boolean isLoaded() {
		World world = MinecraftServer.getServer().worldServerForDimension(this.world);
		return world.checkChunksExist(x, y, z, x, y, z);
	}

	public ItemStack put(ItemStack stack) {
		IInventory tile = getTile();
		if(tile == null)
			return null;
		int start = 0, end = tile.getSizeInventory();
		if(tile instanceof ISidedInventory) {
			start = ((ISidedInventory)tile).func_94127_c(side);
			end = start + ((ISidedInventory)tile).func_94128_d(side);
		}
		for(int i=start; i<end && stack.stackSize>0; i++) {
			putStackInSlot(stack, i);
		}
		return stack;
	}
	
	public ItemStack putStackInSlot(ItemStack stack, int i) {
		IInventory invTile = getTile();
		if(invTile == null)
			return stack;
		ItemStack slotStack = invTile.getStackInSlot(i);
		if(slotStack == null) {
			ItemStack placedStack = stack.copy();
			stack.stackSize = 0;
			invTile.setInventorySlotContents(i, placedStack);
		}
		else if(slotStack.isItemEqual(stack)) {
			int size = stack.getMaxStackSize() - slotStack.stackSize;
			size = stack.stackSize > size ? size : stack.stackSize;
			slotStack.stackSize += size;
			stack.stackSize -= size;
		}
		return stack;
	}

	public void cleanup() {
		IInventory invTile = getTile();
		if(invTile == null)
			return;
		for(int i=0;i<invTile.getSizeInventory();i++) {
			ItemStack stack = invTile.getStackInSlot(i);
			if(stack != null && stack.stackSize == 0)
				invTile.setInventorySlotContents(i, null);
		}
		
	}

	public List<Integer> getChangedSlots() {
		boolean stateChanged = updateState();
		IInventory tile = getTile();
		if(tile == null) {
			stacks = null;
			return new ArrayList<Integer>();
		}
		List<Integer> result = new ArrayList<Integer>();
		if(stacks == null || stateChanged) {
			stacks = new ItemStack[tile.getSizeInventory()];
			int start = 0, end = tile.getSizeInventory();
			if(tile instanceof ISidedInventory){
				start = ((ISidedInventory)tile).func_94127_c(side);
				end = start + ((ISidedInventory)tile).func_94128_d(side);
			}
			for(int i=start; i<end; i++) {
				stacks[i] = tile.getStackInSlot(i);
				result.add(i);
			}
			return result;
		}
		int start = 0, end = tile.getSizeInventory();
		if(tile instanceof ISidedInventory){
			start = ((ISidedInventory)tile).func_94127_c(side);
			end = start + ((ISidedInventory)tile).func_94128_d(side);
		}
		for(int i=start; i<end; i++) {
			if(tile.getStackInSlot(i) != stacks[i])
				result.add(i);
			
		}
		return result;
	}
	
	private boolean updateState() {
		TileWormholeManipulator tile = (TileWormholeManipulator) TileManager.getTile(this.move(ForgeDirection.getOrientation(side)), TileWormholeManipulator.class, false);
		if(tile != null) {
			tile.writeSets(putting, getting, side^1);
		}
		return (!putting.equals(this.putting) || !getting.equals(this.getting) || !isPulling == this.isPulling);
	}

	public boolean isGetting(ItemStack stack) {
		return getting.contains(stack);
	}
	
	public boolean isPutting(ItemStack stack) {
		return putting.contains(stack);
	}

	public boolean isPulling(ItemStack stack) {
		return isPutting(stack) && isPulling;
	}
	/**
	 * Returns how much the inventory wants that Item, from 0 to 5;
	 * @param stack
	 * @return
	 */
	public int getLevel(ItemStack stack) {
		if(!isPutting(stack) && !isGetting(stack))
			return 0;
		if(!isPutting(stack) && isGetting(stack))
			return 1;
		return 2 + (isGetting(stack) ? 0 : 2) + (isPulling ? 1 : 0);
	}
	
	public IInventory getTile() {
		if(tile == null || tile.get() == null) {
			IInventory invTile = (IInventory) TileManager.getTile(this, IInventory.class, false);
			tile = new WeakReference<IInventory>(invTile);
		}
		return tile.get();
	}

	public int getDistance(NetworkedInventory target) {
		return distances.get(target);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NetworkedInventory))
			return false;
		return super.equals(obj) && ((NetworkedInventory)obj).side == side;		
	}

	public ItemStack getStack(int slot) {
		IInventory tile = getTile();
		if(tile == null)
			return null;
		return tile.getStackInSlot(slot);
	}

	public List<Integer> findStacks(ItemStack stack) {
		IInventory tile = getTile();
		if(tile == null)
			return null;
		List<Integer> result = new ArrayList<Integer>();
		
		int start = 0, end = tile.getSizeInventory();
		if(tile instanceof ISidedInventory) {
			start = ((ISidedInventory)tile).func_94127_c(side);
			end = start + ((ISidedInventory)tile).func_94128_d(side);
		}
		for(int i=start; i<end && stack.stackSize>0; i++) {
			ItemStack targetStack = tile.getStackInSlot(i);
			if(targetStack == null)
				continue;
			if(targetStack.isItemEqual(stack))
				result.add(i);
		}
		return result;
	}

}
