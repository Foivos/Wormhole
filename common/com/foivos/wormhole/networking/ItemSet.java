package com.foivos.wormhole.networking;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class ItemSet {
	public List<ItemStack> items = new ArrayList<ItemStack>();
	public boolean incl = true;
	public ItemSet(boolean incl) {
		this.incl = incl;
	}
	public boolean contains(ItemStack stack) {
		for(ItemStack stack2 : items) {
			if(stack2.isItemEqual(stack))
				return incl;
		}
		return !incl;
	}
}
