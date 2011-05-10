package me.cr3dos.mobSpawner.Commands;

import me.cr3dos.mobSpawner.mobSpawner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * DebugLevels a = allStuff b = important Stuff other = nothing
 */
public class mobSpawnerDebugCommand implements CommandExecutor
{

	mobSpawner plugin;
	private static String debugLevel;

	public mobSpawnerDebugCommand(mobSpawner plugin)
	{
		debugLevel = null;
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args)
	{
		if (!(sender instanceof Player)) return true;

		Player p = (Player) sender;
		if (!plugin.hasPermission(p, "mobSpawner.debug"))
		{
			p.sendMessage("No permission for debug");
			return true;
		}
		if (args.length == 0)
		{
			p.sendMessage(mobSpawnerDebugCommand.getDebugLevel());
			return true;
		}
		if (args.length != 1) return false;

		if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("b")) debugLevel = args[0];
		else
		{
			debugLevel = null;
			return true;
		}

		if (mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("a"))
			p.sendMessage("Debug level changed to " + debugLevel);

		return true;
	}

	public static String getDebugLevel()
	{
		if (null == debugLevel) return "no Debug Level";
		return debugLevel;
	}

}
