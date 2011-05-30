package me.cr3dos.mobSpawner.commands;

import me.cr3dos.mobSpawner.MobSpawner;
import me.cr3dos.mobSpawner.file.FileHandler;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MSgetItem implements CommandExecutor
{

	MobSpawner plugin;

	public MSgetItem(MobSpawner plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player p = (Player) sender;
		String item;
		int amount = 1;
		if (args.length == 1)
		{
			item = args[0];
		}
		else if (args.length == 2)
		{
			item = args[0];
			if (MobSpawner.isADigit(args[1]))
			{
				amount = Integer.parseInt(args[1]);
			}
		}
		else return false;

		int id;

		if (item.equalsIgnoreCase("spawner"))
		{
			if (!plugin.hasPermission(p, "mobSpawner.Item.getSpawner")) return true;
			id = Material.MOB_SPAWNER.getId();
		}
		else if (item.equalsIgnoreCase("item"))
		{
			if (!plugin.hasPermission(p, "mobSpawner.Item.getItem")) return true;
			id = FileHandler.readInt("spawnItem");
		}
		else return true;

		ItemStack is = new ItemStack(id);
		is.setAmount(amount);
		p.getInventory().addItem(is);

		return true;
	}

}
