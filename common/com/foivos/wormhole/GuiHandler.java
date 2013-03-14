package com.foivos.wormhole;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.foivos.wormhole.transport.ContainerWormholeManipulator;
import com.foivos.wormhole.transport.GuiWormholeManipulator;
import com.foivos.wormhole.transport.TileWormholeManipulator;

import cpw.mods.fml.common.network.IGuiHandler;

/**
 *This is the class that handles the assignement of all the guis to the correct Containers and GuiContainer.
 */
public class GuiHandler implements IGuiHandler{
	/** 
	 * An array that holds all gui handlers
	 */
	private SingleGuiHandler[] guiHandlers = {new WormholeManipulator()};

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return guiHandlers[ID].getServerGuiElement(player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return guiHandlers[ID].getClientGuiElement(player, world, x, y, z);
	}
	
	/**
	 *An interface that is used to mark each Gui hanler.
	 */
	private interface SingleGuiHandler {
		public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z);
		
		public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z);
		
	}
	
	private class WormholeManipulator implements SingleGuiHandler {

		@Override
		public Object getServerGuiElement(EntityPlayer player, World world,
				int x, int y, int z) {
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof TileWormholeManipulator){
                    return new ContainerWormholeManipulator(player.inventory, (TileWormholeManipulator) tileEntity);
            }
            return null;
		}

		@Override
		public Object getClientGuiElement(EntityPlayer player, World world,
				int x, int y, int z) {
			 TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
             if(tileEntity instanceof TileWormholeManipulator){
                     return new GuiWormholeManipulator(player.inventory, (TileWormholeManipulator) tileEntity);
             }
             return null;
		}
		
	}

}
