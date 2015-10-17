package de.sirati97.bex_proto.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public interface ISocketFactory {
	Socket createSocket(String host, int port) throws UnknownHostException, IOException;
	ServerSocket createServerSocket(int port) throws IOException;
	ServerSocket createServerSocket(int port, InetAddress address) throws IOException;
	InputStream getSocketInputStream(Socket socket) throws IOException;
}