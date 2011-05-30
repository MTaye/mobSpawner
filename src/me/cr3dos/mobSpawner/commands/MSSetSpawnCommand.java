package me.cr3dos.mobSpawner.commands;

import me.cr3dos.mobSpawner.MobSpawner;
import me.cr3dos.mobSpawner.file.FileHandler;
import me.cr3dos.mobSpawner.listeners.MobSpawnerPlayerListenerTwo;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

public class MSSetSpawnCommand implements CommandExecutor
{

	MobSpawnerPlayerListenerTwo pl;
	MobSpawner plugin;

	public MSSetSpawnCommand(MobSpawner plugin, MobSpawnerPlayerListenerTwo pl)
	{
		this.plugin = plugin;
		this.pl = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player p = (Player) sender;
		if (args.length != 1) return false;
		if (!plugin.hasPermission(p, "mobSpawner.setSpawn")) return true;

		Location l = pl.getUserLocation(p);
		if (null == l) return true;
		Block mobSpawner = p.getWorld().getBlockAt(l);
		if (mobSpawner.getType() != Material.MOB_SPAWNER) return true;
		CreatureSpawner spawner = (CreatureSpawner)mobSpawner.getState();

		CreatureType ct = getTyp(args[0]);
		if (null == ct) return true;

		spawner.setCreatureType(ct);

		pl.removeUser(p);

		p.sendMessage("Changed spawn to " + args[0]);
		return true;
	}

	public CreatureType getTyp(String name)
	{
		if (name.length() < 2 || null == name) return null;

		if (name.equalsIgnoreCase("PigZombie"))
		{
			name = "PigZombie";
		}
		else
		{
			name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		}

		if (FileHandler.read("mob." + name) == null || FileHandler.read("mob." + name).equalsIgnoreCase("false"))
		{
			return null;
		}

		return CreatureType.fromName(name);
	}

}
