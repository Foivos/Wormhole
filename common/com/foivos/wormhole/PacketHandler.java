package com.foivos.wormhole;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.foivos.wormhole.transport.ContainerWormholeManipulator;
import com.foivos.wormhole.transport.TileWormholeManipulator;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
/**
 *The packet handler for this mod.
 */
public class PacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals("WHmanipulator")) {
			DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int dimension;
	 
			try
			{
				dimension = stream.readInt();
				World world = DimensionManager.getWorld(dimension);
				int x = stream.readInt();
				int y = stream.readInt();
				int z = stream.readInt();
				byte selected = stream.readByte();
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if(tile instanceof TileWormholeManipulator) {
					((TileWormholeManipulator)tile).setSelected(selected);
				}
			}
			catch (IOException e)
			{
				System.out.println("Failed at reading client packet for TConstruct.");
				e.printStackTrace();
				return;
			}
			if(player instanceof EntityPlayer && ((EntityPlayer) player).openContainer instanceof ContainerWormholeManipulator) {
				((ContainerWormholeManipulator)((EntityPlayer) player).openContainer).updateSlots();
			}
		}
	}

}
