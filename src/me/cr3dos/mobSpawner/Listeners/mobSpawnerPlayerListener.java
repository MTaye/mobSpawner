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
	HashMap<String, Date> users;
	
	public mobSpawnerPlayerListener(mobSpawner ms)
	{
		users = new HashMap<String, Date>(); //signpress
		this.ms = ms;
	}
	
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		
		Player p = e.getPlayer();
		
		if(users.containsKey(p.getName()))
		{
			int diff = ms.differenz(new Date(),users.get(p.getName()));
			if(FileHandler.readInt("signTime")>= diff)
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
			if(null == b) return;
			if(b.getType() == Material.SIGN_POST){
				BlockState state = b.getState();
				Sign s = null;
				if (state instanceof Sign){
					s = (Sign) state;
					if(mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("a") || mobSpawnerDebugCommand.getDebugLevel().equalsIgnoreCase("b")) p.sendMessage(p.getName() + "pressed sign");
					Date d1 = new Date();
					users.put(p.getName(), d1);
					signPress(p, s);
				}//if sign
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
				if(mobSpawner.isADigit(woerter[1])){
					ms.spawnMob(woerter[0],Integer.parseInt(woerter[1]),ms.addLineToLocation(s.getBlock().getLocation(),s.getLine(3)),p);
				}
				else{
					ms.spawnMob(woerter[0],1,ms.addLineToLocation(s.getBlock().getLocation(),s.getLine(3)),p);
				}//if word not a digit
			}//are 2 words on the line
		}//for every line
	}
}
