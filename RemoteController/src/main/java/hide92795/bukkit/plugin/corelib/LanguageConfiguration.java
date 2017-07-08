package hide92795.bukkit.plugin.corelib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import com.google.common.io.Files;

public class LanguageConfiguration extends YamlConfiguration {

	public void load(InputStream stream) throws IOException, InvalidConfigurationException {
		Validate.notNull(stream, "Stream cannot be null");

		InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
		StringBuilder builder = new StringBuilder();
		BufferedReader input = new BufferedReader(reader);


		try {
			String line;

			while ((line = input.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
		} finally {
			input.close();
		}

		loadFromString(builder.toString());
	}

	@Override
	public void save(File file) throws IOException {
		Validate.notNull(file, "File cannot be null");

		Files.createParentDirs(file);

		String data = saveToString();

		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"))) {
			osw.write(data);
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LanguageConfiguration loadConfiguration(File file) {
		Validate.notNull(file, "File cannot be null");

		LanguageConfiguration config = new LanguageConfiguration();

		try {
			config.load(file);
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
		} catch (InvalidConfigurationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
		}
		return config;
	}

	public static LanguageConfiguration loadConfiguration(InputStream stream) {
		Validate.notNull(stream, "Stream cannot be null");

		LanguageConfiguration config = new LanguageConfiguration();

		try {
			config.load(stream);
		} catch (IOException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load language configuration from stream", ex);
		} catch (InvalidConfigurationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load language configuration from stream", ex);
		}

		return config;
	}
}
