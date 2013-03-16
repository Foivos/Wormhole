package com.foivos.wormhole.transport;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.foivos.wormhole.Wormhole;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 *The Container that handles the Wormhole Manipulator.
 *This Container needs to handle the fact that slots need to change often depending on the selected tab and whether or not an Inventory Interactor is placed in the current slot.
 *
 *@see Container
 */
public class ContainerWormholeManipulator extends Container{
	
	public final static int SIZE = TileWormholeManipulator.SIZE;
	
	public TileWormholeManipulator tile = null;
	
	
	private byte selectedSide=0;
	
	public ContainerWormholeManipulator (InventoryPlayer inventoryPlayer, TileWormholeManipulator te){
        tile = te;
        
        bindPlayerInventory(inventoryPlayer);
        updateadditionalSlots();
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
                     stack = null;  	
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else {
        		if(stackInSlot.itemID == Wormhole.inventoryInterractor.itemID && ((Slot)inventorySlots.get(36)).getStack()==null) {
                	stackInSlot.stackSize -= 1;
                	ItemStack tempStack = stackInSlot.copy();
                	tempStack.stackSize = 1;
                	((Slot)inventorySlots.get(36)).putStack(tempStack);
                	stack = null;
        		}
        		if(inventorySlots.size()>37) {
        			
            		if(!this.mergeItemStack(stackInSlot, 39, 57, false)) {
            			stack = null;
            		}
        		}
            }

            

    		if (stackInSlot.stackSize == 0) {
                invSlot.putStack(null);
    		}
            if (stack != null && stackInSlot.stackSize == stack.stackSize) {
                return null;
            }
        }
        return stack;
    }
	
	/**
	 *Updates the slots that are bound to this container.
	 */
	public void updateadditionalSlots() {
		inventorySlots = inventorySlots.subList(0, 36);
		addSlotToContainer((new SlotInventoryInterractor(tile, SIZE*selectedSide, 152, 7)));
		ItemStack stack = getSlot(36).getStack();
		if(stack != null && stack.itemID == Wormhole.inventoryInterractor.itemID) {
			addSlotToContainer(new SlotUpgrade(tile, SIZE*selectedSide+1, 8, 7, -1));
			addSlotToContainer(new SlotUpgrade(tile, SIZE*selectedSide+2, 26, 7, -1));
			for(int i=0;i<9;i++) {
				addSlotToContainer(new Slot(tile, SIZE*selectedSide+3+i, 8+(i%3)*18, 28+(i/3)*18));
			}
			for(int i=0;i<9;i++) {
				addSlotToContainer(new Slot(tile, SIZE*selectedSide+12+i, 116+(i%3)*18, 28+(i/3)*18));
			}
		}
		
		
	}
	
	public byte getSelectedSide() {
		return selectedSide;
	}

	public void setSelectedSide(byte selectedSide) {
		if(this.selectedSide == selectedSide)
			return;
		this.selectedSide = selectedSide;
		sendSelectedSideToServer();
		updateadditionalSlots();
	}

	private void sendSelectedSideToServer() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try
        {
            outputStream.writeByte(selectedSide);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
 
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "WHmanipulator";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        PacketDispatcher.sendPacketToServer(packet);
		
	}

	private class SlotInventoryInterractor extends Slot{
		
		
		public SlotInventoryInterractor(IInventory inv, int slotNumber, int x, int y) {
			super(inv, slotNumber, x, y);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return ((TileWormholeManipulator) inventory).isItemValid(getSlotIndex(), stack);
		}
		
		@Override
		public void onSlotChanged() {
			super.onSlotChanged();
			if(getStack() == null || getStack().itemID != Wormhole.inventoryInterractor.itemID) {
				tile.dropItems(SIZE*selectedSide, SIZE*(selectedSide+1));
			}
			updateadditionalSlots();
		}
	}
	
	private class SlotUpgrade extends Slot{
		
		private int acceptID;
		
		public SlotUpgrade(IInventory inv, int slotNumber, int x, int y, int acceptID) {
			super(inv, slotNumber, x, y);
			this.acceptID = acceptID;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return acceptID == stack.itemID;
		}
		
	}




}
