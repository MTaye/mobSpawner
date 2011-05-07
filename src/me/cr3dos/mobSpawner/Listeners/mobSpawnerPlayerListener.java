package me.cr3dos.mobSpawner.Listeners;

import java.util.Date;
import java.util.HashMap;

import me.cr3dos.mobSpawner.mobSpawner;
import me.cr3dos.mobSpawner.Commands.mobSpawnerDebugCommand;
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
public class mobSpawnerPlayerListener extends PlayerListener {
	mobSpawner ms = null;
	HashMap<String, Date> users = new HashMap<String, Date>(); //signpress
	
	public mobSpawnerPlayerListener(mobSpawner ms)
	{
		this.ms = ms;
	}
	
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		
		Player p = e.getPlayer();
		
		if(users.containsKey(p.getName()))
		{
			if(mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("a") || mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("b")) p.sendMessage("user is in hashmap");
			int diff = ms.differenz(new Date(),users.get(p));
			if(mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("a") || mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("b")) p.sendMessage("Different is:" + diff);
			if(FileHandler.readInt("signWait")>= diff)
			{
				return;
			}
			users.remove(p);
		}
		
		Block b = e.getClickedBlock();
		Action a = e.getAction();
		if (!ms.hasPermission(p, "mobSpawner.sign")) {
			p.sendMessage("No Permission for mobSpawner.sign");
			return;
		}
		if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK){
			
			if(b.getType() == Material.SIGN_POST){
				BlockState state = b.getState();
				Sign s = null;
				if (state instanceof Sign){
					if(mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("a") || mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("b")) p.sendMessage("right click on a sign");
					s = (Sign) state;
					signPress(p, s);
					users.put(p.getName(), new Date());
				}//if sign post
			}//if material sign
		}//if right click
		
	}//onPlayerInteract

	/**
	 * Check if the sign which was pressed is a ms sign and spawn the mobs
	 * 
	 * @param p: the Player who pressed the sign
	 * @param s: which Sign was pressed
	 */
	public void signPress(Player p, Sign s) {
		if(!s.getLine(0).trim().equalsIgnoreCase("ms")) return;
		for(int i = 1;i<s.getLines().length-1; i++)
		{
			String line = s.getLine(i);
			line = line.trim();
			String[] woerter = line.split(" ");
			if(woerter.length <1) return;
			else if(woerter.length == 1){
				ms.spawnMob(woerter[0],1,ms.addLineToLocation(s.getBlock().getLocation(),s.getLine(3)),p);
			}
			else if(woerter.length == 2){
				if(ms.isADigit(woerter[1])){
					ms.spawnMob(woerter[0],Integer.parseInt(woerter[1]),ms.addLineToLocation(s.getBlock().getLocation(),s.getLine(3)),p);
				}
				else{
					ms.spawnMob(woerter[0],1,ms.addLineToLocation(s.getBlock().getLocation(),s.getLine(3)),p);
				}//if word not a digit
			}//are 2 words on the line
		}//for every line
	}
}
