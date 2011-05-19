package me.cr3dos.mobSpawner.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.cr3dos.mobSpawner.mobSpawner;
import me.cr3dos.mobSpawner.file.*;

public class mobSpawnerMSsetCommand implements CommandExecutor
{

	mobSpawner plugin;

	public mobSpawnerMSsetCommand(mobSpawner plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (!plugin.hasPermission((Player) sender, "mobSpawner.command.setting"))
		{
			return true;
		}

		if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("signTime"))
			{
				if (FileHandler.writeInt("signTime", args[1]) == false)
				{
					sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "]" + "Could not change signTime");

				}
				return true;
			}
			else if (args[0].equalsIgnoreCase("cmdTime"))
			{
				if (FileHandler.writeInt("cmdTime", args[1]) == false)
				{
					sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "]" + "Could not change cmdTime");

				}
				return true;
			}
		}
		else if (args.length == 1)
		{
			if (mobSpawner.isASupportedMob(args[0]))
			{
				if (plugin.hasPermission((Player) sender, "mobSpawner.command.toggleMob")) if (args[0].length() < 2 || null == args[0]) return true;
				if (args[0].equalsIgnoreCase("PigZombie")) args[0] = "PigZombie";
				else args[0] = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();

				this.toggleSetting("mob." + args[0]);
				sender.sendMessage(args[0] + " is" + (FileHandler.read("mob." + args[0]).equalsIgnoreCase("true") ? " on" : " off"));
				return true;
			}
		}
		return false;
	}

	public void toggleSetting(String cmd)
	{
		if (FileHandler.read(cmd).equalsIgnoreCase("true"))
		{
			FileHandler.write(cmd, "false");
		}
		else
		{
			FileHandler.write(cmd, "true");
		}
	}

}
