package me.cr3dos.mobSpawner.listeners;

import java.util.Date;
import java.util.HashMap;

import me.cr3dos.mobSpawner.MobSpawner;
import me.cr3dos.mobSpawner.commands.DebugCommand;
import me.cr3dos.mobSpawner.file.FileHandler;

import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * @author: cr3dos
 * @version: 1.0
 */
public class MobSpawnerPlayerListener extends PlayerListener
{
	MobSpawner plugin;
	HashMap<String, Date> users;

	public MobSpawnerPlayerListener(MobSpawner ms)
	{
		users = new HashMap<String, Date>(); // signpress
		this.plugin = ms;
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent e)
	{

		Player p = e.getPlayer();

		if (users.containsKey(p.getName()))
		{
			int diff = plugin.differenz(new Date(), users.get(p.getName()));
			if (FileHandler.readInt("signTime") >= diff)
			{
				return;
			}
			users.remove(p);
		}

		Block b = e.getClickedBlock();
		Action a = e.getAction();
		if (!plugin.hasPermission(p, "mobSpawner.sign"))
		{
			return;
		}

		if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)
		{
			if (null == b) return;
			if (b.getType() == Material.SIGN_POST)
			{
				BlockState state = b.getState();
				Sign s = null;
				if (state instanceof Sign)
				{
					s = (Sign) state;
					if (DebugCommand.getDebugLevel().equalsIgnoreCase("a") || DebugCommand.getDebugLevel().equalsIgnoreCase("b")) p.sendMessage(p.getName() + "pressed sign");
					Date d1 = new Date();
					users.put(p.getName(), d1);
					signPress(p, s);
				}// if sign
			}// if material sign
		}// if right click

	}// onPlayerInteract

	/**
	 * Check if the sign which was pressed is a ms sign and spawn the mobs
	 * 
	 * @param p: the Player who pressed the sign
	 * @param s: which Sign was pressed
	 */
	public void signPress(Player p, Sign s)
	{
		if (!s.getLine(0).trim().equalsIgnoreCase("ms")) return;
		for (int i = 1; i < s.getLines().length - 1; i++)
		{
			String line = s.getLine(i);
			line = line.trim();
			String[] woerter = line.split(" ");
			if (woerter.length < 1) return;
			else if (woerter.length == 1)
			{
				plugin.spawnMob(woerter[0], 1, plugin.addLineToLocation(s.getBlock().getLocation(), s.getLine(3)), p);
			}
			else if (woerter.length == 2)
			{
				if (MobSpawner.isADigit(woerter[1]))
				{
					plugin.spawnMob(woerter[0], Integer.parseInt(woerter[1]), plugin.addLineToLocation(s.getBlock().getLocation(), s.getLine(3)), p);
				}
				else
				{
					plugin.spawnMob(woerter[0], 1, plugin.addLineToLocation(s.getBlock().getLocation(), s.getLine(3)), p);
				}// if word not a digit
			}// are 2 words on the line
		}// for every line
	}
}
