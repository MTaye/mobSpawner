package me.cr3dos.mobSpawner.listeners;

import me.cr3dos.mobSpawner.MobSpawner;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class MobSpawnerBlockListener extends BlockListener
{
	MobSpawner plugin;
	public BlockFace[] faces = BlockFace.values();

	public MobSpawnerBlockListener(MobSpawner ms)
	{
		this.plugin = ms;
	}

	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent e)
	{
		Block block = e.getBlock();
		if (e.getOldCurrent() == 0 && e.getNewCurrent() > 0)
		{
			for (int i = 0; i < 6; i++)
			{
				Block b = block.getRelative(faces[i]);
				if (b.getType() == Material.SIGN_POST)
				{
					Sign sign = (Sign) b.getState();
					if (sign.getLine(0).equalsIgnoreCase("ms"))
					{
						for (int ix = 1; ix < sign.getLines().length - 1; ix++)
						{
							String line = sign.getLine(ix);
							line = line.trim();
							String[] woerter = line.split(" ");
							if (woerter.length < 1) return;
							else if (woerter.length == 1)
							{
								plugin.spawnMob(woerter[0], 1, sign.getWorld(), plugin.addLineToLocation(sign.getBlock().getLocation(), sign.getLine(3)));
							}
							else if (woerter.length == 2)
							{
								if (MobSpawner.isADigit(woerter[1]))
								{
									plugin.spawnMob(woerter[0], Integer.parseInt(woerter[1]), sign.getWorld(), plugin.addLineToLocation(sign.getBlock().getLocation(), sign.getLine(3)));
								}
								else
								{
									plugin.spawnMob(woerter[0], 1, sign.getWorld(), plugin.addLineToLocation(sign.getBlock().getLocation(), sign.getLine(3)));
								}
							}
						}
					}
				}
			}
		}
	}
}
