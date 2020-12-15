package com.velocitypowered.proxy.network.packet.clientbound;

import com.velocitypowered.proxy.network.packet.AbstractKeepAlivePacket;
import com.velocitypowered.proxy.network.packet.Packet;
import com.velocitypowered.proxy.network.packet.PacketHandler;
import com.velocitypowered.proxy.network.packet.PacketReader;

public class ClientboundKeepAlivePacket extends AbstractKeepAlivePacket implements Packet {
  public static final PacketReader<ClientboundKeepAlivePacket> DECODER = decoder(ClientboundKeepAlivePacket::new);

  public ClientboundKeepAlivePacket(final long randomId) {
    super(randomId);
  }

  @Override
  public boolean handle(PacketHandler handler) {
    return handler.handle(this);
  }
}