package hide92795.bukkit.plugin.remotecontroller;

import hide92795.bukkit.plugin.remotecontroller.notification.Notification;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class RemoteServer extends WebSocketServer {
	private int id = 0;
	private final RemoteController plugin;
	private ConcurrentHashMap<Integer, ClientConnection> clients;

	public RemoteServer(RemoteController plugin) {
		super(new InetSocketAddress(plugin.config.port), 1, Collections.singletonList((Draft) new Draft_17()));
		this.plugin = plugin;
		this.clients = new ConcurrentHashMap<>();
	}

	@Override
	public void onOpen(WebSocket client_socket, ClientHandshake handshake) {
		int client_id = id++;
		client_socket.setID(client_id);
		InetSocketAddress clientAddress = client_socket.getRemoteSocketAddress();
		plugin.getLogger().info("Connection start. (" + clientAddress.getAddress().getHostAddress() + ", ID: " + client_id + ")");
		if (clients.containsKey(client_id)) {
			clients.get(client_id).closed();
		}
		ClientConnection client = new ClientConnection(plugin, clientAddress, client_socket);
		try {
			client.start();
			clients.put(client_id, client);
		} catch (Exception e) {
			plugin.getLogger().severe("An error has occurred in creating RSA key.");
		}
	}

	@Override
	public void onClose(WebSocket client_socket, int code, String reason, boolean remote) {
		int client_id = client_socket.getID();
		ClientConnection client = clients.remove(client_id);
		if (client != null) {
			client.closed();
		} else {
			plugin.getLogger().warning("ClientConnection didn't found in #onClose.");
		}
	}

	@Override
	public void onError(WebSocket client_socket, Exception ex) {
		if (client_socket != null) {
			plugin.getLogger().warning("An error has occured in the connection. : " + client_socket);
		} else {
			plugin.getLogger().severe("An internal error has occurred.");
		}
		ex.printStackTrace();
		client_socket.close();
	}

	@Override
	public void onMessage(WebSocket client_socket, String message) {
		try {
			int id = client_socket.getID();
			if (clients.containsKey(id)) {
				clients.get(id).receive(message);
			} else {
				plugin.getLogger().severe("Client not found. ID:" + id);
				client_socket.close();
			}
		} catch (Exception e) {
			client_socket.close();
		}
	}

	public void stopServer() {
		clients.forEach( (id, client) -> { try { client.close(); } catch (Exception | Error e) { e.printStackTrace(); }});

		try {
			stop();
		} catch (Exception | Error e) {
			plugin.getLogger().severe("An error has occured closing server!");
			e.printStackTrace();
		}
	}

	public void removeConnection(InetSocketAddress address) {
		clients.remove(address);
	}

	public void sendConsoleLog(String message) {
		clients.forEach((id,client) -> { if (client.isSendConsoleLog()) { client.send("CONSOLE", 0, message);}});
    }

	public void sendChatLog(String message) {
	    clients.forEach((id,client) -> { if (client.isSendChatLog()) { client.send("CHAT", 0, message);}});
	}

	public void sendNotificationLog(Notification notification) {
		clients.forEach((id,client) -> { if (client.isSendNotificationLog()) { client.send("NOTIFICATION", 0, notification.toString());}});
	}
}
