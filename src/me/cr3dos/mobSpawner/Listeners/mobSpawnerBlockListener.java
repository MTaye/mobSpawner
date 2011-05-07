package me.cr3dos.mobSpawner.Listeners;


import me.cr3dos.mobSpawner.mobSpawner;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class mobSpawnerBlockListener extends BlockListener {
	mobSpawner ms = null;
	public BlockFace[] faces = BlockFace.values();
	
	public mobSpawnerBlockListener(mobSpawner ms)
	{
		this.ms = ms;
	}
	
	public void onBlockRedstoneChange(BlockRedstoneEvent e) 
	{	
		Block block = e.getBlock();
		if(e.getOldCurrent() == 0 && e.getNewCurrent() > 0)
		{
			for(int i=0; i<6; i++)
			{
				Block b = block.getRelative(faces[i]);
				if(b.getType() == Material.SIGN_POST)
				{
					Sign sign = (Sign)b.getState();
					if(sign.getLine(0).equalsIgnoreCase("ms"))	
					{
						for(int ix = 1;ix<sign.getLines().length-1; ix++)
						{
							String line = sign.getLine(ix);
							line = line.trim();
							String[] woerter = line.split(" ");
							if(woerter.length <1) return;
							else if(woerter.length == 1){
								ms.spawnMob(woerter[0],1,sign.getWorld(), ms.addLineToLocation(sign.getBlock().getLocation(),sign.getLine(3)));
							}
							else if(woerter.length == 2){
								if(mobSpawner.isADigit(woerter[1])){
									ms.spawnMob(woerter[0],Integer.parseInt(woerter[1]),sign.getWorld(),ms.addLineToLocation(sign.getBlock().getLocation(),sign.getLine(3)));
								}
								else{
									ms.spawnMob(woerter[0],1,sign.getWorld(),ms.addLineToLocation(sign.getBlock().getLocation(),sign.getLine(3)));
								}
							}
						}
					}
				}
			}
		}
	}
}
