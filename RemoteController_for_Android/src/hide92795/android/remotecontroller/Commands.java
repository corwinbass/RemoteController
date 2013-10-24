package hide92795.android.remotecontroller;

import hide92795.android.remotecontroller.command.Command;
import hide92795.android.remotecontroller.command.CommandAuthentication;
import hide92795.android.remotecontroller.command.CommandCharset;
import hide92795.android.remotecontroller.command.CommandChat;
import hide92795.android.remotecontroller.command.CommandConsole;
import hide92795.android.remotecontroller.command.CommandDirectory;
import hide92795.android.remotecontroller.command.CommandDynmap;
import hide92795.android.remotecontroller.command.CommandError;
import hide92795.android.remotecontroller.command.CommandFileOpen;
import hide92795.android.remotecontroller.command.CommandPlayers;
import hide92795.android.remotecontroller.command.CommandServerInfo;
import hide92795.android.remotecontroller.command.CommandSuccess;
import java.util.HashMap;

public class Commands {
	public static final HashMap<String, Command> commands;
	static {
		commands = new HashMap<String, Command>();
		commands.put("AUTH", new CommandAuthentication());
		commands.put("SERVER_INFO", new CommandServerInfo());
		commands.put("CONSOLE", new CommandConsole());
		commands.put("CHAT", new CommandChat());
		commands.put("PLAYERS", new CommandPlayers());
		commands.put("DIRECTORY", new CommandDirectory());
		commands.put("FILE_OPEN", new CommandFileOpen());
		commands.put("CHARSET", new CommandCharset());
		commands.put("SUCCESS", new CommandSuccess());
		commands.put("ERROR", new CommandError());
		commands.put("DYNMAP", new CommandDynmap());
	}
}
