package me.cr3dos.mobSpawner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import me.cr3dos.mobSpawner.Commands.mobSpawnerDebugCommand;
import me.cr3dos.mobSpawner.Commands.mobSpawnerMSCommand;
import me.cr3dos.mobSpawner.Commands.mobSpawnerMSsetCommand;
import me.cr3dos.mobSpawner.Listeners.*;
import me.cr3dos.mobSpawner.file.FileHandler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;

/**
 * @author: cr3dos
 * @version: 1.0
 */
public class mobSpawner extends JavaPlugin{
	
	public static PermissionHandler permissionHandler;
	
	PluginDescriptionFile pdfFile;
	mobSpawnerPlayerListener mspl = null;
	mobSpawnerBlockListener msbl = null;
	mobSpawnerDebugCommand msDebug = null;
	private static final Logger log = Logger.getLogger("Minecraft");
		
	/*----------------------------------------------*/
	/* Dis/Enable                                   */
	/*----------------------------------------------*/	
	@Override
	public void onDisable() {
		log.info(pdfFile.getName() + " disabled");
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		mspl = new mobSpawnerPlayerListener(this);
		msbl = new mobSpawnerBlockListener(this);
		msDebug = new mobSpawnerDebugCommand(this);
		
		pm.registerEvent(Type.PLAYER_INTERACT, mspl, Priority.Normal, this);
		pm.registerEvent(Type.REDSTONE_CHANGE, this.msbl, Priority.Normal, this);
		
		mobSpawnerMSCommand msCommand = new mobSpawnerMSCommand(this);
		mobSpawnerMSsetCommand msSetCommand = new mobSpawnerMSsetCommand(this);
		getCommand("ms").setExecutor(msCommand);
		getCommand("MS").setExecutor(msCommand);
		getCommand("msset").setExecutor(msSetCommand);
		getCommand("msdebug").setExecutor(msDebug);

		setupPermissions();
		FileHandler.onStartUp(); 
		pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " " + pdfFile.getVersion() +  " enabled");
	}
	
	/*----------------------------------------------*/
	/* Helper                                       */
	/*----------------------------------------------*/
	/**
	 * add some blocks in a specified direction
	 * @param location: location position of the current Location
	 * @param line: String in the format: direction numberofblocks
	 * @return the new Locatio
	 */
	public Location addLineToLocation(Location location, String line) {
		if(line.trim().equals("")) ;
		String[] woerter = line.trim().split(" ");
		
		int numb = 0;
		if(woerter.length<2 || !isADigit(woerter[1])){
			return location;
		}
		else{
			numb = Integer.parseInt(woerter[1]);
		}
		
		if(woerter[0].equalsIgnoreCase("east"))
		{
			location.setZ(location.getZ() + numb);
		}
		else if(woerter[0].equalsIgnoreCase("north"))
		{
			location.setX(location.getX() + numb);
		}
		else if(woerter[0].equalsIgnoreCase("south"))
		{
			location.setX(location.getX() - numb);
		}
		else if(woerter[0].equalsIgnoreCase("west"))
		{
			location.setZ(location.getZ() - numb);
		}
		else if(woerter[0].equalsIgnoreCase("up"))
		{
			location.setY(location.getY() + numb);
		}
		else if(woerter[0].equalsIgnoreCase("down"))
		{
			location.setY(location.getY() - numb);
		}
		
		return location;
	}

	/**
	 * Spawn mobs specified by the parameters
	 * @param name: name of the mob(Zombie, zombie, zOmbie ... is possible)
	 * @param anz: how many mobs with this name
	 * @param world: which world
	 * @param location: whiche location
	 */
	public void spawnMob(String name, int anz, World world, Location location) {
		if(name.length() <2) return;
		name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		CreatureType ct = CreatureType.fromName(name);
		if(ct == null){
			return;
		}
		
		for(int i = 0; i<anz ; i++){
			world.spawnCreature(location, ct);
		}
	}
	
	/**
	 * Spawn mobs specified by the parameters
	 * @param name: name of the mob(Zombie, zombie, zOmbie ... is possible)
	 * @param anz: how many mobs with this name
	 * @param location: which location
	 * @param location: which player fired the command
	 */
	public void spawnMob(String name, int anz, Location location, Player p) {
		this.spawnMob(name, anz, p.getWorld(), location);
	}
	
	public int differenz(Date now, Date old) {
		Calendar cal_1 = new GregorianCalendar();
		cal_1.setTime(old);
		Calendar cal_2 = new GregorianCalendar();       
		cal_2.setTime(now);
		long time = cal_2.getTime().getTime() - cal_1.getTime().getTime();  
		int s = (int)time / 1000;     					
		
		return s;
	}
	/*----------------------------------------------*/
	/* Checker                                      */
	/*----------------------------------------------*/
	public static boolean isADigit(String string) {
		try
		{
			Integer.parseInt(string);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}	
	
	/*----------------------------------------------*/
	/* Permission                                   */
	/*----------------------------------------------*/
	private void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	
	    if (mobSpawner.permissionHandler == null) {
	    	if (permissionsPlugin != null) {
	    		mobSpawner.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	        } else {
	            log.info("Permission system not detected, defaulting to OP");
	        }
	    }
	}
	  
	public boolean hasPermission(Player p, String s)
	{
		if(null == permissionHandler)
		{
			return p.isOp();
		}
		if (mobSpawner.permissionHandler.has(p, s)) {
			return true;
		}
		return false;
	}
}
