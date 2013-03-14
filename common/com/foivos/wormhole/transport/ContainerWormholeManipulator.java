package com.foivos.wormhole.transport;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.foivos.wormhole.Wormhole;

/**
 *The Container that handles the Wormhole Manipulator.
 *This Container needs to handle the fact that slots need to change often depending on the selected tab and whether or not an Inventory Interactor is placed in the current slot.
 *
 *@see Container
 */
public class ContainerWormholeManipulator extends Container{
	
	public TileWormholeManipulator tile = null;
	
	public ContainerWormholeManipulator (InventoryPlayer inventoryPlayer, TileWormholeManipulator te){
        tile = te;
        
        bindPlayerInventory(inventoryPlayer);
        addSlotToContainer((new SlotWormholeManipulator(tile, 0, 152, 7)));
        updateSlots();
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 154));
		}
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 96 + i * 18));
			}
		}

		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
			Slot invSlot = (Slot) inventorySlots.get(slot);
			ItemStack stackInSlot = invSlot.getStack();
            ItemStack stack = null;

            //null checks and checks if the item can be stacked (maxStackSize > 1)
            if (stackInSlot != null) {
            		stack = stackInSlot.copy();
                    //merges the item into player inventory since its in the tileEntity
                    if (slot >=36) {
                        if (!this.mergeItemStack(stackInSlot, 0, 36, false)) {
                             return null;  	
                        }
                    }
                    //places it into the tileEntity is possible since its in the player inventory
                    else {
                		if(stackInSlot.itemID == Wormhole.inventoryInterractor.itemID && tile.getStackInSlot(0)==null) {
	                    	stackInSlot.stackSize -= 1;
	                    	ItemStack tempStack = stackInSlot.copy();
	                    	tempStack.stackSize = 1;
	                    	tile.setInventorySlotContents(0, tempStack);
                		}
                		if(inventorySlots.size()>37) {
                			
	                		if(!this.mergeItemStack(stackInSlot, 39, 57, false)) {
	                			return null;
	                		}
                		}
                    }

                    

            		if (stackInSlot.stackSize == 0) {
                        invSlot.putStack(null);
            		}
                    if (stackInSlot.stackSize == stack.stackSize) {
                        return null;
                    }
            }
            return stack;
    }

	public void updateSlots() {
		ItemStack stack = tile.getStackInSlot(0);
		if(stack != null && stack.itemID == Wormhole.inventoryInterractor.itemID) {
			if(inventorySlots.size() == 57) 
				return;
			addSlotToContainer(new SlotWormholeManipulator(tile, 1, 8, 7));
			addSlotToContainer(new SlotWormholeManipulator(tile, 2, 26, 7));
			for(int i=0;i<9;i++) {
				addSlotToContainer(new Slot(tile, 3+i, 8+(i%3)*18, 28+(i/3)*18));
			}
			for(int i=0;i<9;i++) {
				addSlotToContainer(new Slot(tile, 12+i, 116+(i%3)*18, 28+(i/3)*18));
			}
			if(tile.worldObj.isRemote)
				tile.updateSelected();
		}
		else {
			if(inventorySlots.size() == 37)
				return;
			
			Random rand = new Random();
			for (int i = 1; i < 21; i++) {
                ItemStack item = tile.getStackInSlot(i);

                if (item != null && item.stackSize > 0) {
                    float rx = rand.nextFloat() * 0.8F + 0.1F;
                    float ry = rand.nextFloat() * 0.8F + 0.1F;
                    float rz = rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(tile.worldObj,
                                    tile.xCoord + rx, tile.yCoord + ry, tile.zCoord + rz,
                                    new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));
                    if (item.hasTagCompound()) {
                            entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                    }

                    float factor = 0.05F;
                    entityItem.motionX = rand.nextGaussian() * factor;
                    entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                    entityItem.motionZ = rand.nextGaussian() * factor;
                    if(!tile.worldObj.isRemote)
                    	tile.worldObj.spawnEntityInWorld(entityItem);
                    
                    item.stackSize = 0;
                    tile.setInventorySlotContents(i, null);
                }
                
                inventorySlots = inventorySlots.subList(0, 37);
    			
			}
			if(tile.worldObj.isRemote)
				tile.updateSelected();
		}
		
	}



}
