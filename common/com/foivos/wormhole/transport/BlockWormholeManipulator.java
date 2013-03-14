package com.foivos.wormhole.transport;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.Wormhole;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 *The Wormhole Manipulator block.
 */
public class BlockWormholeManipulator extends BlockContainer{
	public BlockWormholeManipulator(int id) {
		super(id, Material.rock);
        setHardness(3.0F);
        setResistance(5.0F);
        setBlockName("wormholeManipulator");
        setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {	
		return new TileWormholeManipulator();
	}
	
	 @Override
     public boolean onBlockActivated(World world, int x, int y, int z,
                     EntityPlayer player, int idk, float what, float these, float are) {
             TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
             if (tileEntity == null || player.isSneaking()) {
                     return false;
             }
             player.openGui(Wormhole.instance, 0, world, x, y, z);
             return true;
     }
	 
	@Override
	public String getTextureFile() {
		return CommonProxy.BLOCK_PNG;
	}
	
	@Override
	public int getBlockTextureFromSide(int side) {
		return side;
	}
}
