package com.velocitypowered.proxy.network;

import com.velocitypowered.proxy.util.concurrent.VelocityNettyThreadFactory;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.incubator.channel.uring.IOUring;
import io.netty.incubator.channel.uring.IOUringDatagramChannel;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;
import io.netty.incubator.channel.uring.IOUringSocketChannel;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;

enum TransportType {
  NIO("NIO", NioServerSocketChannel::new,
      NioSocketChannel::new,
      NioDatagramChannel::new,
      (name, type) -> new NioEventLoopGroup(0, createThreadFactory(name, type))),
  EPOLL("epoll", EpollServerSocketChannel::new,
      EpollSocketChannel::new,
      EpollDatagramChannel::new,
      (name, type) -> new EpollEventLoopGroup(0, createThreadFactory(name, type))),
  IO_URING("io_uring", IOUringServerSocketChannel::new,
      IOUringSocketChannel::new,
      IOUringDatagramChannel::new,
      (name, type) -> new IOUringEventLoopGroup(0, createThreadFactory(name, type)));

  final String name;
  final ChannelFactory<? extends ServerSocketChannel> serverSocketChannelFactory;
  final ChannelFactory<? extends SocketChannel> socketChannelFactory;
  final ChannelFactory<? extends DatagramChannel> datagramChannelFactory;
  final BiFunction<String, Type, EventLoopGroup> eventLoopGroupFactory;

  TransportType(final String name,
      final ChannelFactory<? extends ServerSocketChannel> serverSocketChannelFactory,
      final ChannelFactory<? extends SocketChannel> socketChannelFactory,
      final ChannelFactory<? extends DatagramChannel> datagramChannelFactory,
      final BiFunction<String, Type, EventLoopGroup> eventLoopGroupFactory) {
    this.name = name;
    this.serverSocketChannelFactory = serverSocketChannelFactory;
    this.socketChannelFactory = socketChannelFactory;
    this.datagramChannelFactory = datagramChannelFactory;
    this.eventLoopGroupFactory = eventLoopGroupFactory;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public EventLoopGroup createEventLoopGroup(final Type type) {
    return this.eventLoopGroupFactory.apply(this.name, type);
  }

  private static ThreadFactory createThreadFactory(final String name, final Type type) {
    return new VelocityNettyThreadFactory("Netty " + name + ' ' + type.toString() + " #%d");
  }

  public static TransportType bestType() {
    if (Boolean.getBoolean("velocity.disable-native-transport")) {
      return NIO;
    }

    if (IOUring.isAvailable()) {
      return IO_URING;
    } else if (Epoll.isAvailable()) {
      return EPOLL;
    } else {
      return NIO;
    }
  }

  public enum Type {
    BOSS("Boss"),
    WORKER("Worker");

    private final String name;

    Type(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}
