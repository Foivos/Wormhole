package com.foivos.wormhole.transport;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWormholeManipulator extends Slot{
	
	
	public SlotWormholeManipulator(IInventory inv, int slotNumber, int x, int y) {
		super(inv, slotNumber, x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getSlotStackLimit() {
		return ((TileWormholeManipulator) inventory).getSlotStackLimit(getSlotIndex());
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return ((TileWormholeManipulator) inventory).isItemValid(getSlotIndex(), stack);
	}
	
}
