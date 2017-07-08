package hide92795.bukkit.plugin.corelib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class CoreLib extends JavaPlugin {
	public static CoreLib getCoreLib() {
		return (CoreLib) Bukkit.getServer().getPluginManager().getPlugin("hide92795CoreLib");
	}
}
