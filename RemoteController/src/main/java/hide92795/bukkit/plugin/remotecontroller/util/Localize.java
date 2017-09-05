package hide92795.bukkit.plugin.remotecontroller.util;

import java.io.File;
import java.io.InputStream;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Localize {
	private final JavaPlugin plugin;
	private File configFile;
	private FileConfiguration config;

	public Localize(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void reload(String lang) throws Exception {
		reload(lang, "jp");
	}

	public void reload(String lang, String default_lang) throws Exception {
		configFile = new File(plugin.getDataFolder(), lang + ".yml");

		config = LanguageConfiguration.loadConfiguration(configFile);
		config.options().copyDefaults(true);

		try (InputStream inJarConfigStream = plugin.getResource(lang + ".yml")) {
			if (inJarConfigStream != null) {
				LanguageConfiguration inJarConfig = LanguageConfiguration.loadConfiguration(inJarConfigStream);
				config.setDefaults(inJarConfig);
			} else {
				try (InputStream defConfigStream = plugin.getResource(default_lang + ".yml")) {
					if (defConfigStream != null) {
						LanguageConfiguration defConfig = LanguageConfiguration.loadConfiguration(defConfigStream);
						config.setDefaults(defConfig);
					} else {
						plugin.getLogger().warning("Can't load Default language.");
					}
				}
			}
		}
		config.save(configFile);
	}

	public String getString(Localizable path) {
		return ChatColor.translateAlternateColorCodes('&', config.getString(path.getName()));
	}
}
