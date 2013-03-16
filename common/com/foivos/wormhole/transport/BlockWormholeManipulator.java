package com.foivos.wormhole.transport;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.Wormhole;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *The Wormhole Manipulator block.
 */
public class BlockWormholeManipulator extends BlockContainer{
	
	public Icon[] icons = new Icon[6];
	
	public BlockWormholeManipulator(int id) {
		super(id, Material.rock);
        setHardness(3.0F);
        setResistance(5.0F);
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
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister register) {
		// TODO Auto-generated method stub
		for(int i=0;i<6;i++){
			icons[i] = register.func_94245_a("Wormhole:manipulator"+i);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		// TODO Auto-generated method stub
		return icons[side];
	}
}
