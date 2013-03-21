package com.foivos.wormhole.networking;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.foivos.wormhole.Coord;
import com.foivos.wormhole.Spot;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.transport.TileWormhole;
import com.foivos.wormhole.transport.TileWormholeManipulator;

public class NetworkedInventory extends Spot{

	public int side;
	
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

	public ItemStack put(ItemStack stack, int i) {
		IInventory invTile = (IInventory) TileManager.getTile(this, IInventory.class, false);
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
		IInventory invTile = (IInventory) TileManager.getTile(this, IInventory.class, false);
		if(invTile == null)
			return;
		for(int i=0;i<invTile.getSizeInventory();i++) {
			ItemStack stack = invTile.getStackInSlot(i);
			if(stack != null && stack.stackSize == 0)
				invTile.setInventorySlotContents(i, null);
		}
		
	}
	
	public InventoryData getData() {
		IInventory invTile = (IInventory) TileManager.getTile(this, IInventory.class, false);
		if(invTile == null)
			return null;
		TileWormhole wormTile = (TileWormhole) TileManager.getTile(this.move(ForgeDirection.getOrientation(side)), TileWormhole.class, false);
		if(wormTile == null)
			return null;
		return new InventoryData(invTile, wormTile, side);
	}


}
