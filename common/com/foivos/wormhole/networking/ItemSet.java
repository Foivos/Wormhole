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
			if(stack2 != null && stack != null && stack2.isItemEqual(stack))
				return incl;
		}
		return !incl;
	}
	
	public boolean equals(ItemSet set) {
		if(incl != set.incl)
			return false;
		if(items.size() != set.items.size())
			return false;
		for(int i=0; i<items.size(); i++) {
			if(items.get(i) != set.items.get(i))
				return false;
		}
		return true;
	}
}
