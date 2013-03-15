package com.foivos.wormhole.transport;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.foivos.wormhole.Wormhole;

public class GuiWormholeManipulator extends GuiContainer {


	public GuiWormholeManipulator(InventoryPlayer inventoryPlayer,
			TileWormholeManipulator tileEntity) {
		super(new ContainerWormholeManipulator(inventoryPlayer, tileEntity));
		this.ySize = 178;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		
		
		fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		// draw your Gui here, only thing you need to change is the path
		int texture = mc.renderEngine.getTexture("/com/foivos/wormhole/manipulator.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		ItemStack stack = inventorySlots.getSlot(36).getStack();
		if(stack == null || stack.itemID != Wormhole.inventoryInterractor.itemID)
			this.drawTexturedModalRect(x+7, y+6, 0, 178, 162, 253-178);
		byte selectedSide = ((ContainerWormholeManipulator)inventorySlots).getSelectedSide();
		this.drawTexturedModalRect(x+61+9*selectedSide,y+72,176,0,9,9);
	}

	@Override
	protected void mouseClicked(int x, int y, int modifiers) {
		int x1 = x-(width - xSize) / 2;
		int y1= y-(height - ySize) / 2;
		if(x1>=61 && x1<69+9*5 && y1>=72 && y1<81) {
			((ContainerWormholeManipulator) inventorySlots).setSelectedSide((byte) ((x1-61)/9));
		}
		super.mouseClicked(x,y,modifiers);
	}

}
