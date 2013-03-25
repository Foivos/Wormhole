package com.foivos.wormhole.networking;

import net.minecraft.item.ItemStack;

public class Utils {
	
	public static final int[][] transTable = {
		{  0,  0,  0, 10,  0,  4},
		{  0,  0, 11,  8,  5,  1},
		{  0,-11,  0,  9,  6,  2},
		{-10, -8, -9,  0,  7,  3},
		{  0, -5, -6, -7,  0,  4},
		{ -4, -1, -2, -3, -4,  0},	
	};

	/**
	 * Gets the importance of the transaction.
	 * Negative, positive and zero results mean that the stack will travel to inv1, inv2, or none respectively;
	 */
	public static int whoWants(NetworkedInventory inv1, NetworkedInventory inv2, ItemStack stack) {
		int lvl1 = inv1.getLevel(stack), lvl2 = inv2.getLevel(stack); 
		return(transTable[lvl1][lvl2]);
	}

}
