package me.cr3dos.mobSpawner.Commands;

import java.util.Date;
import java.util.HashMap;

import me.cr3dos.mobSpawner.mobSpawner;
import me.cr3dos.mobSpawner.file.FileHandler;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mobSpawnerMSCommand implements CommandExecutor
{

	mobSpawner plugin;

	HashMap<String, Date> users;

	public mobSpawnerMSCommand(mobSpawner plugin)
	{
		users = new HashMap<String, Date>();
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (!(sender instanceof Player)) return false;

		Player p = (Player) sender;

		if (!plugin.hasPermission(p, "mobSpawner.command"))
		{
			return true;
		}

		if (users.containsKey(p.getName()))
		{
			int diff = plugin.differenz(new Date(), users.get(p.getName()));
			if (FileHandler.readInt("cmdTime") >= diff)
			{
				return true;
			}
			users.remove(p);
		}

		if (args.length == 1)
		{
			String s = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();

			Location l = p.getTargetBlock(null, 150).getLocation();

			plugin.spawnMob(s, 1, l, p);
			users.put(p.getName(), new Date());
			return true;
		}
		if (args.length == 2 || args.length == 3)
		{
			String s = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();

			Location l = null;
			int max = 0;

			if (mobSpawner.isADigit(args[1]))
			{
				// ms Zombie 1
				// ms Zombie 1 player
				max = Integer.parseInt(args[1]);
				if (args.length == 2)
				{
					l = p.getTargetBlock(null, 150).getLocation();
				}
				else if (args.length == 3)
				{
					Player ziel = p.getServer().getPlayer(args[2]);
					if (ziel == null) return true;
					l = ziel.getLocation();
				}
				else return false;

			}
			else
			{
				// ms Zombie player
				max = 1;
				if (args.length == 1)
				{
					l = p.getTargetBlock(null, 150).getLocation();
					max = 1;
				}
				else if (args.length == 2)
				{
					Player ziel = p.getServer().getPlayer(args[1]);
					if (ziel == null) return true;
					l = ziel.getLocation();
				}
				else return false;
			}

			plugin.spawnMob(s, max, l, p);
			users.put(p.getName(), new Date());
			return true;
		}
		return false;
	}

}
