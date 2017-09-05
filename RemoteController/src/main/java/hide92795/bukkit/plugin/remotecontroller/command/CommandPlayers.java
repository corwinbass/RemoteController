package hide92795.bukkit.plugin.remotecontroller.command;

import hide92795.bukkit.plugin.remotecontroller.ClientConnection;
import hide92795.bukkit.plugin.remotecontroller.RemoteController;
import org.bukkit.entity.HumanEntity;

import java.util.stream.Collectors;

public class CommandPlayers implements Command {

	@Override
	public void doCommand(RemoteController plugin, ClientConnection connection, int pid, String arg) {
		try {
			if (connection.isAuthorized()) {
				String data = "";
				if (plugin.getServer().getOnlinePlayers().size()>0) {
					data = plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.joining(":"));
				}
				connection.send("PLAYERS", pid, data);
			} else {
				connection.send("ERROR", pid, "NOT_AUTH");
			}
		} catch (Exception e) {
			plugin.getLogger().severe("An error has occurred in CommandPlayers!");
			connection.send("ERROR", pid, "EXCEPTION:" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public boolean mustRunOnMainThread() {
		return true;
	}

}
