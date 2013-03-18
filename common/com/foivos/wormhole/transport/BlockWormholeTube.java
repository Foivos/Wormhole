package com.foivos.wormhole.transport;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.networking.NetworkManager;
import com.foivos.wormhole.networking.TileNetwork;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *The Wormhole Tube block. 
 */
public class BlockWormholeTube extends BlockContainer
{

	private static final float blockThickness = 0.5f;
	public Icon[] icons = new Icon[32];
	
	public BlockWormholeTube (int id) {
        super(id, Material.rock);
        setHardness(3.0F);
        setResistance(5.0F);
        setCreativeTab(CreativeTabs.tabDecorations);
        this.minX = (1-blockThickness)/2;
	    this.maxX= (1+blockThickness)/2;
	    this.minY = (1-blockThickness)/2;
	    this.maxY= (1+blockThickness)/2;
	    this.minZ = (1-blockThickness)/2;
	    this.maxZ= (1+blockThickness)/2;
	}
	
	@Override
    public TileEntity createNewTileEntity(World world) {
            return new TileWormholeTube();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister register) {
		for(int i=0;i<32;i++) {
			icons[i] = register.func_94245_a("Wormhole:tube"+i);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		return icons[0];
	}
	
	@Override
	public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		TileWormholeTube tile = (TileWormholeTube) blockAccess.getBlockTileEntity(x, y, z);
		byte connections = tile.getConnections();
		int texture=0;
		if(side < 2) {
			texture |= (connections & 1<<2)>>1;
			texture |= (connections & 1<<3)>>3;
			texture |= (connections & 1<<4)>>1;
			texture |= (connections & 1<<5)>>3;
		}
		else {
			//an array for the side on the left of the current side-2.
			byte[] sides = {4, 5, 3, 2};
			texture |= (connections & 2) | (connections & 1);
			int prevSide = sides[side-2];
			texture |= (connections & (1<<prevSide))>>prevSide<<2;
			int nextSide = prevSide ^ 1;
			texture |= (connections & (1<<nextSide))>>nextSide<<3;
		}
		return icons[texture + (tile.activated ? 16 : 0)];
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		super.onNeighborBlockChange(world, x, y, z, blockID);
		TileWormholeTube tile = (TileWormholeTube) world.getBlockTileEntity(x, y, z);
		if(!tile.activated)
			tile.updateConnections();
		else
			NetworkManager.validate(tile.network, tile.worldObj, x, y ,z);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		TileWormholeTube tile = (TileWormholeTube) world.getBlockTileEntity(x, y, z);
		tile.updateConnections();
	}

	
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int x, int y, int z)
    {
        double margin = (1-blockThickness)/2;
        return AxisAlignedBB.getAABBPool().getAABB((double)x + margin, (double)y+margin, (double)z + margin, (double)(x + 1) - margin, (double)(y + 1) - margin, (double)(z + 1) - margin);
    }
	@Override
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
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		System.out.println(((TileNetwork)world.getBlockTileEntity(x, y, z)).network);
		
		return super.onBlockActivated(world, x, y, z, par5EntityPlayer,
				par6, par7, par8, par9);
	}*/
	

}
