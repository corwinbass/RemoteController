package hide92795.bukkit.plugin.remotecontroller.command;

import com.google.gson.Gson;
import hide92795.bukkit.plugin.remotecontroller.ClientConnection;
import hide92795.bukkit.plugin.remotecontroller.RemoteController;
import hide92795.bukkit.plugin.remotecontroller.util.Util;
import org.apache.commons.lang3.StringUtils;
import java.util.LinkedHashMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class CommandPluginInfo implements Command {

	@Override
	public void doCommand(RemoteController plugin, ClientConnection connection, int pid, String arg) {
		try {
			if (connection.isAuthorized()) {
				Plugin target = plugin.getServer().getPluginManager().getPlugin(arg);
				PluginDescriptionFile desc = target.getDescription();
				LinkedHashMap<String, Object> datas = new LinkedHashMap<>();
				datas.put("NAME", target.getName());
				datas.put("VERSION", desc.getVersion());
				datas.put("AUTHOR", StringUtils.join(desc.getAuthors(), ", "));
				datas.put("COMMANDS", Util.commandsToHashMap(desc.getCommands()));
				datas.put("PERMISSIONS", Util.permissionsToHashMap(desc.getPermissions()));
				datas.put("WEB", desc.getWebsite());
				datas.put("DESCRIPTION", target.getDescription().getDescription());
				datas.put("STATUS", target.isEnabled());
				Gson gson = new Gson();
				connection.send("PLUGIN_INFO", pid, gson.toJson(datas, LinkedHashMap.class));
			} else {
				connection.send("ERROR", pid, "NOT_AUTH");
			}
		} catch (Exception e) {
			plugin.getLogger().severe("An error has occured in CommandPluginInfo!");
			connection.send("ERROR", pid, "EXCEPTION:" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public boolean mustRunOnMainThread() {
		return true;
	}

}
