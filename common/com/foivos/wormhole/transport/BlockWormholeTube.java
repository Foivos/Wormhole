package com.foivos.wormhole.transport;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.TileManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWormholeTube extends BlockWormhole{

	public Icon[] tubeIcons = new Icon[144];
	public static final double thickness = 0.5;
	
	
	public BlockWormholeTube(int id) {
		super(id);
		this.minX = (1-thickness)/2;
		this.minY = (1-thickness)/2;
		this.minZ = (1-thickness)/2;
		this.maxX = (1+thickness)/2;
		this.maxY = (1+thickness)/2;
		this.maxZ = (1+thickness)/2;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileWormholeTube();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister register) {
		for(int i=0;i<144;i++) {
			tubeIcons[i] = register.func_94245_a("Wormhole:tube"+i);
		}
		
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getAABBPool().getAABB((1-thickness)/2, (1-thickness)/2, (1-thickness)/2, (1+thickness)/2, (1+thickness)/2, (1+thickness)/2);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		return tubeIcons[0];
	}
	
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		if(tile == null)
			return null;
		byte connections = tile.connections;
		int texture = 0;
		if(side < 2) {
			texture |= (connections & 1<<2);
			texture |= (connections & 1<<3);
			texture |= (connections & 1<<4)>>4;
			texture |= (connections & 1<<5)>>4;
		}
		else {
			//an array for the side on the left of the current side-2.
			byte[] sides = {4, 5, 3, 2};
			texture |= (connections & 2)<<1 | (connections & 1)<<3;
			int prevSide = sides[side-2];
			texture |= (connections & (1<<prevSide))>>prevSide<<1;
			int nextSide = prevSide ^ 1;
			texture |= (connections & (1<<nextSide))>>nextSide;
		}
		
		return tubeIcons[(15-texture)*9 + tile.color];
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
	
	/*@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		super.onNeighborBlockChange(world, x, y, z, id);
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		if(tile == null)
			return;
		tile.updateConnections();
	}*/
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		if(tile == null)
			return;
		tile.updateConnections();
	}
	
	

}
