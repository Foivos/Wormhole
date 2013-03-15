package com.foivos.wormhole;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.foivos.wormhole.transport.ContainerWormholeManipulator;

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
	 
			try
			{
				byte selected = stream.readByte();
				if(player instanceof EntityPlayer && ((EntityPlayer) player).openContainer instanceof ContainerWormholeManipulator) {
					((ContainerWormholeManipulator)((EntityPlayer) player).openContainer).setSelectedSide(selected);
				}
			}
			catch (IOException e)
			{
				System.out.println("Failed at reading client packet for TConstruct.");
				e.printStackTrace();
				return;
			}
			
		}
	}

}
