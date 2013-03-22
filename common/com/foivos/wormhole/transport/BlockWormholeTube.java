package com.foivos.wormhole.transport;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.foivos.wormhole.CommonProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWormholeTube extends BlockWormhole{

	public Icon face;
	
	public BlockWormholeTube(int id) {
		super(id);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileWormholeTube();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister register) {
		face = register.func_94245_a("Wormhole:tubeFace");
		
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		// TODO Auto-generated method stub
		return face;
	}
	
	public Icon getFaceTecture() {
		return face;
	}
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	@Override
    public int getRenderType()
    {
        return CommonProxy.WORMHOLE_TUBE_RENDER_ID;
    }
	
	

}
