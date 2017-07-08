package hide92795.bukkit.plugin.remotecontroller.compatibility;

import hide92795.bukkit.plugin.remotecontroller.RemoteController;
import hide92795.bukkit.plugin.remotecontroller.notification.ConsoleError;
import hide92795.bukkit.plugin.remotecontroller.notification.ConsoleException;
import hide92795.bukkit.plugin.remotecontroller.util.Util;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

public class LogAppender extends AbstractAppender {
	private RemoteController plugin;
	private SimpleDateFormat date;

	public LogAppender(RemoteController remotecontroller) {
		super("RemoteController", null, null);
		this.plugin = remotecontroller;
		this.date = new SimpleDateFormat("HH:mm:ss");
		start();
	}

	@Override
	public void append(LogEvent event) {
		StringBuilder builder = new StringBuilder();
		Throwable ex = event.getThrown();

		String message = Util.convertColorCode(event.getMessage().getFormattedMessage());

		builder.append(date.format(event.getTimeMillis()));
		builder.append("-[");
		builder.append(event.getLevel().name());
		builder.append("]-");
		builder.append(message);

		if (ex != null) {
			StringWriter writer = new StringWriter();
			ex.printStackTrace(new PrintWriter(writer));
			builder.append(writer);
			ConsoleException exception = new ConsoleException(ex.toString());
			plugin.onNotificationUpdate(exception);
		}

		if (event.getLevel().isMoreSpecificThan(Level.WARN))
		{
			if (event.getLevel().equals(Level.WARN))
			{ if (plugin.config.notification_include_warn_log)
				{
					plugin.onNotificationUpdate(new ConsoleError(message));
				}
			} else {
				plugin.onNotificationUpdate(new ConsoleError(message));
			}
		}
		plugin.onConsoleLogUpdate(builder.toString());
	}
}
