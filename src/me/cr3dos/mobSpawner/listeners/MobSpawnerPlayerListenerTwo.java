package me.cr3dos.mobSpawner.listeners;

import java.util.HashMap;

import me.cr3dos.mobSpawner.MobSpawner;
import me.cr3dos.mobSpawner.file.FileHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class MobSpawnerPlayerListenerTwo extends PlayerListener
{
	HashMap<String, Location> users;

	MobSpawner plugin;

	public MobSpawnerPlayerListenerTwo(MobSpawner plugin)
	{
		this.plugin = plugin;
		users = new HashMap<String, Location>();
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Action a = e.getAction();
		Material spawnMat = Material.getMaterial(FileHandler.readInt("spawnItem"));

		if (!plugin.hasPermission(p, "mobSpawner.setSpawn")) return;
		
		if (a == Action.RIGHT_CLICK_BLOCK)
		{
			if(null == e.getItem()) return;
			Material itemMaterial = e.getItem().getType();
			if (null == spawnMat) return;
			if (itemMaterial.getId() != spawnMat.getId()) return;
			users.put(p.getName(), e.getClickedBlock().getLocation());
		}
	}

	public Location getUserLocation(Player p)
	{
		return users.get(p.getName());
	}

	public void removeUser(Player p)
	{
		users.remove(p.getName());
	}
}
