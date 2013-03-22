package com.foivos.wormhole.rendering;

import org.lwjgl.opengl.GL11;

import com.foivos.wormhole.CommonProxy;
import com.foivos.wormhole.TileManager;
import com.foivos.wormhole.transport.BlockWormholeTube;
import com.foivos.wormhole.transport.TileWormholeTube;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class WormholeTubeRenderer implements ISimpleBlockRenderingHandler{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		TileWormholeTube tile = (TileWormholeTube) TileManager.getTile(world, x, y, z, TileWormholeTube.class, true);
		if(tile == null)
			return false;
		if(!(block instanceof BlockWormholeTube))
			return false;
		BlockWormholeTube blockTube = (BlockWormholeTube) block;
		byte connections = tile.connections;
		

		int l = block.colorMultiplier(world, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        
        return renderStandardBlockWithAmbientOcclusion(block, x, y, z, f, f1, f2, renderer);
	}

	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.WORMHOLE_TUBE_RENDER_ID;
	}
	
	public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7, RenderBlocks renderer) {
    float offset = 0.0625F*3;
	renderer.enableAO = true;
    boolean flag = false;
    float f3 = renderer.lightValueOwn;
    float f4 = renderer.lightValueOwn;
    float f5 = renderer.lightValueOwn;
    float f6 = renderer.lightValueOwn;
    boolean flag1 = true;
    boolean flag2 = true;
    boolean flag3 = true;
    boolean flag4 = true;
    boolean flag5 = true;
    boolean flag6 = true;
    renderer.lightValueOwn = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4);
    renderer.aoLightValueXNeg = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
    renderer.aoLightValueYNeg = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
    renderer.aoLightValueZNeg = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
    renderer.aoLightValueXPos = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
    renderer.aoLightValueYPos = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
    renderer.aoLightValueZPos = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
    int l = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4);
    int i1 = l;
    int j1 = l;
    int k1 = l;
    int l1 = l;
    int i2 = l;
    int j2 = l;

    if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4))
    {
        j1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
    }

    if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4))
    {
        i2 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);
    }

    if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4))
    {
        i1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
    }

    if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4))
    {
        l1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
    }

    if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1))
    {
        k1 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
    }

    if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1))
    {
        j2 = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
    }

    Tessellator tessellator = Tessellator.instance;
    tessellator.setBrightness(983055);
    renderer.aoGrassXYZPPC = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
    renderer.aoGrassXYZPNC = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
    renderer.aoGrassXYZPCP = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
    renderer.aoGrassXYZPCN = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
    renderer.aoGrassXYZNPC = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
    renderer.aoGrassXYZNNC = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
    renderer.aoGrassXYZNCN = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
    renderer.aoGrassXYZNCP = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
    renderer.aoGrassXYZCPP = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
    renderer.aoGrassXYZCPN = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
    renderer.aoGrassXYZCNP = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
    renderer.aoGrassXYZCNN = Block.canBlockGrass[renderer.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

    if (renderer.func_94175_b(par1Block).func_94215_i().equals("grass_top"))
    {
        flag6 = false;
        flag5 = false;
        flag4 = false;
        flag3 = false;
        flag1 = false;
    }

    if (renderer.func_94167_b())
    {
        flag6 = false;
        flag5 = false;
        flag4 = false;
        flag3 = false;
        flag1 = false;
    }

    if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 - 1, par4, 0))
    {
        if (renderer.aoType > 0)
        {
            if (renderer.renderMinY <= 0.0D)
            {
                --par3;
            }

            renderer.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);

            if (!renderer.aoGrassXYZCNN && !renderer.aoGrassXYZNNC)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 - 1);
                renderer.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!renderer.aoGrassXYZCNP && !renderer.aoGrassXYZNNC)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 + 1);
                renderer.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!renderer.aoGrassXYZCNN && !renderer.aoGrassXYZPNC)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 - 1);
                renderer.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!renderer.aoGrassXYZCNP && !renderer.aoGrassXYZPNC)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 + 1);
                renderer.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (renderer.renderMinY <= 0.0D)
            {
                ++par3;
            }

            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + renderer.aoLightValueYNeg) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + renderer.aoLightValueYNeg + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (renderer.aoLightValueYNeg + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueYNeg + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, j1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, j1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, j1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, j1);
        }
        else
        {
            f6 = renderer.aoLightValueYNeg;
            f5 = renderer.aoLightValueYNeg;
            f4 = renderer.aoLightValueYNeg;
            f3 = renderer.aoLightValueYNeg;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = renderer.aoBrightnessXYNN;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = (flag1 ? par5 : 1.0F) * 0.5F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = (flag1 ? par6 : 1.0F) * 0.5F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = (flag1 ? par7 : 1.0F) * 0.5F;
        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        tessellator.addTranslation(0.0F, offset, 0.0F);
        renderer.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, ((BlockWormholeTube)par1Block).face);
        tessellator.addTranslation(0.0F, -offset, 0.0F);
        flag = true;
    }

    if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3 + 1, par4, 1))
    {
        if (renderer.aoType > 0)
        {
            if (renderer.renderMaxY >= 1.0D)
            {
                ++par3;
            }

            renderer.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);

            if (!renderer.aoGrassXYZCPN && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 - 1);
                renderer.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 - 1);
            }

            if (!renderer.aoGrassXYZCPN && !renderer.aoGrassXYZPPC)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 - 1);
                renderer.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 - 1);
            }

            if (!renderer.aoGrassXYZCPP && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4 + 1);
                renderer.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4 + 1);
            }

            if (!renderer.aoGrassXYZCPP && !renderer.aoGrassXYZPPC)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4 + 1);
                renderer.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4 + 1);
            }

            if (renderer.renderMaxY >= 1.0D)
            {
                --par3;
            }

            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + renderer.aoLightValueYPos) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + renderer.aoLightValueYPos + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (renderer.aoLightValueYPos + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueYPos + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i2);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i2);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i2);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i2);
        }
        else
        {
            f6 = renderer.aoLightValueYPos;
            f5 = renderer.aoLightValueYPos;
            f4 = renderer.aoLightValueYPos;
            f3 = renderer.aoLightValueYPos;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = i2;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = flag2 ? par5 : 1.0F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = flag2 ? par6 : 1.0F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = flag2 ? par7 : 1.0F;
        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        tessellator.addTranslation(0.0F, -offset, 0.0F);
        renderer.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, ((BlockWormholeTube)par1Block).face);
        tessellator.addTranslation(0.0F, offset, 0.0F);
        flag = true;
    }

    float f7;
    float f8;
    float f9;
    int k2;
    float f10;
    int l2;
    Icon icon = ((BlockWormholeTube)par1Block).face;
    int i3;
    int j3;

    if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 - 1, 2))
    {
        if (renderer.aoType > 0)
        {
            if (renderer.renderMinZ <= 0.0D)
            {
                --par4;
            }

            renderer.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);

            if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZCNN)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 - 1, par4);
                renderer.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZCPN)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 + 1, par4);
                renderer.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!renderer.aoGrassXYZPCN && !renderer.aoGrassXYZCNN)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 - 1, par4);
                renderer.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!renderer.aoGrassXYZPCN && !renderer.aoGrassXYZCPN)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 + 1, par4);
                renderer.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (renderer.renderMinZ <= 0.0D)
            {
                ++par4;
            }

            if (renderer.field_98189_n && renderer.field_94177_n.gameSettings.ambientOcclusion >= 2)
            {
                f7 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN) / 4.0F;
                f9 = (renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
                f8 = (renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
                f10 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg) / 4.0F;
                f3 = (float)((double)f7 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
                f4 = (float)((double)f7 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
                f5 = (float)((double)f7 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
                f6 = (float)((double)f7 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
                k2 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, k1);
                i3 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, k1);
                j3 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, k1);
                l2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, k1);
                renderer.brightnessTopLeft = renderer.func_96444_a(k2, i3, j3, l2, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
                renderer.brightnessBottomLeft = renderer.func_96444_a(k2, i3, j3, l2, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
                renderer.brightnessBottomRight = renderer.func_96444_a(k2, i3, j3, l2, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
                renderer.brightnessTopRight = renderer.func_96444_a(k2, i3, j3, l2, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            }
            else
            {
                f3 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN) / 4.0F;
                f4 = (renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
                f5 = (renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
                f6 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg) / 4.0F;
                renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, k1);
                renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, k1);
                renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, k1);
                renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, k1);
            }
        }
        else
        {
            f6 = renderer.aoLightValueZNeg;
            f5 = renderer.aoLightValueZNeg;
            f4 = renderer.aoLightValueZNeg;
            f3 = renderer.aoLightValueZNeg;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = k1;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = (flag3 ? par5 : 1.0F) * 0.8F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = (flag3 ? par6 : 1.0F) * 0.8F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = (flag3 ? par7 : 1.0F) * 0.8F;
        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        tessellator.addTranslation(0.0F, 0.0F, offset);
        renderer.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, icon);
        tessellator.addTranslation(0.0F, 0.0F, -offset);
        flag = true;
    }

    if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2, par3, par4 + 1, 3))
    {
        if (renderer.aoType > 0)
        {
            if (renderer.renderMaxZ >= 1.0D)
            {
                ++par4;
            }

            renderer.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3, par4);
            renderer.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3, par4);
            renderer.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZCNP)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 - 1, par4);
                renderer.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 - 1, par4);
            }

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZCPP)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 - 1, par3 + 1, par4);
                renderer.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 - 1, par3 + 1, par4);
            }

            if (!renderer.aoGrassXYZPCP && !renderer.aoGrassXYZCNP)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 - 1, par4);
                renderer.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 - 1, par4);
            }

            if (!renderer.aoGrassXYZPCP && !renderer.aoGrassXYZCPP)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2 + 1, par3 + 1, par4);
                renderer.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2 + 1, par3 + 1, par4);
            }

            if (renderer.renderMaxZ >= 1.0D)
            {
                --par4;
            }

            if (renderer.field_98189_n && renderer.field_94177_n.gameSettings.ambientOcclusion >= 2)
            {
                f7 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP) / 4.0F;
                f9 = (renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
                f8 = (renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
                f10 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos) / 4.0F;
                f3 = (float)((double)f7 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
                f4 = (float)((double)f7 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
                f5 = (float)((double)f7 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
                f6 = (float)((double)f7 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
                k2 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, j2);
                i3 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, j2);
                j3 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, j2);
                l2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, j2);
                renderer.brightnessTopLeft = renderer.func_96444_a(k2, l2, j3, i3, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
                renderer.brightnessBottomLeft = renderer.func_96444_a(k2, l2, j3, i3, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
                renderer.brightnessBottomRight = renderer.func_96444_a(k2, l2, j3, i3, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
                renderer.brightnessTopRight = renderer.func_96444_a(k2, l2, j3, i3, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);
            }
            else
            {
                f3 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP) / 4.0F;
                f6 = (renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
                f5 = (renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
                f4 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos) / 4.0F;
                renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, j2);
                renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, j2);
                renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, j2);
                renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, j2);
            }
        }
        else
        {
            f6 = renderer.aoLightValueZPos;
            f5 = renderer.aoLightValueZPos;
            f4 = renderer.aoLightValueZPos;
            f3 = renderer.aoLightValueZPos;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = j2;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = (flag4 ? par5 : 1.0F) * 0.8F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = (flag4 ? par6 : 1.0F) * 0.8F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = (flag4 ? par7 : 1.0F) * 0.8F;
        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        
        
        tessellator.addTranslation(0.0F, 0.0F, -offset);
        renderer.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, ((BlockWormholeTube)par1Block).face);
        tessellator.addTranslation(0.0F, 0.0F, -offset);
        

        flag = true;
    }

    if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2 - 1, par3, par4, 4))
    {
        if (renderer.aoType > 0)
        {
            if (renderer.renderMinX <= 0.0D)
            {
                --par2;
            }

            renderer.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);

            if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZNNC)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 - 1);
                renderer.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZNNC)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 + 1);
                renderer.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 - 1);
                renderer.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 + 1);
                renderer.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (renderer.renderMinX <= 0.0D)
            {
                ++par2;
            }

            if (renderer.field_98189_n && renderer.field_94177_n.gameSettings.ambientOcclusion >= 2)
            {
                f7 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP) / 4.0F;
                f9 = (renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
                f8 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
                f10 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg) / 4.0F;
                f3 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMaxZ + (double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f7 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
                f4 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMinZ + (double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f10 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f7 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
                f5 = (float)((double)f9 * renderer.renderMinY * renderer.renderMinZ + (double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f7 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
                f6 = (float)((double)f9 * renderer.renderMinY * renderer.renderMaxZ + (double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f10 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f7 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
                k2 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
                i3 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
                j3 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
                l2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
                renderer.brightnessTopLeft = renderer.func_96444_a(i3, j3, l2, k2, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
                renderer.brightnessBottomLeft = renderer.func_96444_a(i3, j3, l2, k2, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
                renderer.brightnessBottomRight = renderer.func_96444_a(i3, j3, l2, k2, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
                renderer.brightnessTopRight = renderer.func_96444_a(i3, j3, l2, k2, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
            }
            else
            {
                f6 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP) / 4.0F;
                f3 = (renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
                f4 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
                f5 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg) / 4.0F;
                renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
                renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
                renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
                renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
            }
        }
        else
        {
            f6 = renderer.aoLightValueXNeg;
            f5 = renderer.aoLightValueXNeg;
            f4 = renderer.aoLightValueXNeg;
            f3 = renderer.aoLightValueXNeg;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = i1;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = (flag5 ? par5 : 1.0F) * 0.6F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = (flag5 ? par6 : 1.0F) * 0.6F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = (flag5 ? par7 : 1.0F) * 0.6F;
        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        tessellator.addTranslation(offset, 0.0F, 0.0F);
        renderer.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, icon);
        tessellator.addTranslation(-offset, 0.0F, 0.0F);
        
      

        flag = true;
    }

    if (renderer.renderAllFaces || par1Block.shouldSideBeRendered(renderer.blockAccess, par2 + 1, par3, par4, 5))
    {
        if (renderer.aoType > 0)
        {
            if (renderer.renderMaxX >= 1.0D)
            {
                ++par2;
            }

            renderer.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4);
            renderer.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4);
            renderer.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 - 1);
            renderer.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4 + 1);
            renderer.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4);

            if (!renderer.aoGrassXYZPNC && !renderer.aoGrassXYZPCN)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 - 1);
                renderer.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 - 1);
            }

            if (!renderer.aoGrassXYZPNC && !renderer.aoGrassXYZPCP)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 - 1, par4 + 1);
                renderer.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 - 1, par4 + 1);
            }

            if (!renderer.aoGrassXYZPPC && !renderer.aoGrassXYZPCN)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 - 1);
                renderer.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 - 1);
            }

            if (!renderer.aoGrassXYZPPC && !renderer.aoGrassXYZPCP)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(renderer.blockAccess, par2, par3 + 1, par4 + 1);
                renderer.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3 + 1, par4 + 1);
            }

            if (renderer.renderMaxX >= 1.0D)
            {
                --par2;
            }

            if (renderer.field_98189_n && renderer.field_94177_n.gameSettings.ambientOcclusion >= 2)
            {
                f7 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP) / 4.0F;
                f9 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos) / 4.0F;
                f8 = (renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
                f10 = (renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
                f3 = (float)((double)f7 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMinY * renderer.renderMaxZ);
                f4 = (float)((double)f7 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMinY * renderer.renderMinZ);
                f5 = (float)((double)f7 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMaxY * renderer.renderMinZ);
                f6 = (float)((double)f7 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMaxY * renderer.renderMaxZ);
                k2 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, l1);
                i3 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, l1);
                j3 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, l1);
                l2 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, l1);
                renderer.brightnessTopLeft = renderer.func_96444_a(k2, l2, j3, i3, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
                renderer.brightnessBottomLeft = renderer.func_96444_a(k2, l2, j3, i3, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
                renderer.brightnessBottomRight = renderer.func_96444_a(k2, l2, j3, i3, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
                renderer.brightnessTopRight = renderer.func_96444_a(k2, l2, j3, i3, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);
            }
            else
            {
                f3 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP) / 4.0F;
                f4 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos) / 4.0F;
                f5 = (renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
                f6 = (renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
                renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, l1);
                renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, l1);
                renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, l1);
                renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, l1);
            }
        }
        else
        {
            f6 = renderer.aoLightValueXPos;
            f5 = renderer.aoLightValueXPos;
            f4 = renderer.aoLightValueXPos;
            f3 = renderer.aoLightValueXPos;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = l1;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = (flag6 ? par5 : 1.0F) * 0.6F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = (flag6 ? par6 : 1.0F) * 0.6F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = (flag6 ? par7 : 1.0F) * 0.6F;
        renderer.colorRedTopLeft *= f3;
        renderer.colorGreenTopLeft *= f3;
        renderer.colorBlueTopLeft *= f3;
        renderer.colorRedBottomLeft *= f4;
        renderer.colorGreenBottomLeft *= f4;
        renderer.colorBlueBottomLeft *= f4;
        renderer.colorRedBottomRight *= f5;
        renderer.colorGreenBottomRight *= f5;
        renderer.colorBlueBottomRight *= f5;
        renderer.colorRedTopRight *= f6;
        renderer.colorGreenTopRight *= f6;
        renderer.colorBlueTopRight *= f6;
        tessellator.addTranslation(-offset, 0.0F, 0.0F);
        renderer.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, icon);
        tessellator.addTranslation(offset, 0.0F, 0.0F);
        
        

        flag = true;
    }

    renderer.enableAO = false;
    return flag;
}

}
