package com.foivos.wormhole.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.isismtt.ISISMTTObjectIdentifiers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

import com.foivos.wormhole.transport.TileWormhole;
import com.foivos.wormhole.transport.TileWormholeManipulator;

public class InventoryData {
	public IInventory tile;
	public int start, end;
	public List<Integer> emptySlots = new ArrayList<Integer>();
	public Map<Integer, ItemStack> pulled = new HashMap<Integer, ItemStack>(50);
	public Map<Integer, ItemStack> stored = new HashMap<Integer, ItemStack>(50);
	public Map<Integer, ItemStack> buffered = new HashMap<Integer, ItemStack>(50);
	public Map<Integer, ItemStack> pushed = new HashMap<Integer, ItemStack>(50);
	public ItemSet putting = new ItemSet(false);
	public ItemSet getting = new ItemSet(true);
	public boolean isPulling = false;
	
	private InventoryData() {}
	
	public InventoryData(IInventory invTile, TileWormhole wormTile, int side) {
		tile = invTile;
		ItemSet putting = new ItemSet(false), getting = new ItemSet(true);
		if(wormTile instanceof TileWormholeManipulator) {
			TileWormholeManipulator manipulator = (TileWormholeManipulator) wormTile;
			manipulator.writeSets(putting, getting, side);
			isPulling = manipulator.isPuller();
		}
		int start = 0, end = invTile.getSizeInventory();
		if(invTile instanceof ISidedInventory) {
			start = ((ISidedInventory)invTile).func_94127_c(side);
			end = start + ((ISidedInventory)invTile).func_94128_d(side);
		}
		for(int i=start; i<end; i++) {
			add(i);
		}
	}
	
	private void add(int i) {
		ItemStack stack = tile.getStackInSlot(i);
		if(stack == null) 
			emptySlots.add(i);
		else if(isBuffering(stack)) 
			buffered.put(i, stack);
		else if(isPushing(stack))
			pushed.put(i, stack);
		else if(stack.stackSize >= stack.getMaxStackSize())
			return;
		else if(isPulling(stack))
			pulled.put(i, stack);
		else if(isStoring(stack))
			stored.put(i, stack);
	}
	
	public boolean isPulling(ItemStack stack) {
		return isPulling && putting.contains(stack) && !getting.contains(stack);
	}
	public boolean isStoring(ItemStack stack) {
		return !isPulling && putting.contains(stack) && !getting.contains(stack);
	}
	public boolean isBuffering(ItemStack stack) {
		return putting.contains(stack) && getting.contains(stack);
	}
	public boolean isPushing(ItemStack stack) {
		return !putting.contains(stack) && getting.contains(stack);
	}
	
	public void slotChanged(int i) {
		emptySlots.remove(i);
		pulled.remove(i);
		pushed.remove(i);
		stored.remove(i);
		buffered.remove(i);
		add(i);
	}
}

