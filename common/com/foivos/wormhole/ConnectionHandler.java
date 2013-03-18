package com.foivos.wormhole;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ConnectionHandler implements IConnectionHandler{

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try
        {
            outputStream.writeInt(33);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
 
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "testChannel";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        PacketDispatcher.sendPacketToPlayer(packet, player);
		/*if(!(pl instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) pl;
		player.playerNetServerHandler.sendPacketToPlayer(packet);*/
		
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			INetworkManager manager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server,
			int port, INetworkManager manager) {
		
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server, INetworkManager manager) {
		
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
		
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
		
	}

}
