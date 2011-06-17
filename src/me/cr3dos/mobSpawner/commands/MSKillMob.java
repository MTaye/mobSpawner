package me.cr3dos.mobSpawner.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MSKillMob implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(args.length == 0)
		{
			//kill all mobs
		}
		if(args.length == 1)
		{
			//kill mob
		}
		return false;
	}
}
