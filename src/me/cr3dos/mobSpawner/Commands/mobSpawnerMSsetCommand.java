package me.cr3dos.mobSpawner.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.cr3dos.mobSpawner.mobSpawner;
import me.cr3dos.mobSpawner.file.*;

public class mobSpawnerMSsetCommand implements CommandExecutor {
	
	mobSpawner plugin;
	
	public mobSpawnerMSsetCommand(mobSpawner plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!plugin.hasPermission((Player)sender, "mobSpawner.settings"))
		{
			sender.sendMessage(ChatColor.RED + "No permission for changing settings");
			return true;
		}
		
		if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("signTime"))
			{
				if (FileHandler.writeInt("signWait", args[1]) == false)
				{
					sender.sendMessage(ChatColor.RED + "[" + plugin.getDescription().getName() + "]" + "Could not change signTime");
					return true;
				}
			}
		}
		return false;
	}

}
