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

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
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
public class mobSpawner extends JavaPlugin
{

	public static PermissionHandler permissionHandler;

	PluginDescriptionFile pdfFile;
	mobSpawnerPlayerListener mspl = null;
	mobSpawnerBlockListener msbl = null;
	mobSpawnerDebugCommand msDebug = null;
	private static final Logger log = Logger.getLogger("Minecraft");

	/*----------------------------------------------*/
	/* Dis/Enable */
	/*----------------------------------------------*/
	@Override
	public void onDisable()
	{
		log.info(pdfFile.getName() + " disabled");
	}

	@Override
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		mspl = new mobSpawnerPlayerListener(this);
		msbl = new mobSpawnerBlockListener(this);
		msDebug = new mobSpawnerDebugCommand(this);

		pm.registerEvent(Type.PLAYER_INTERACT, mspl, Priority.Normal, this);
		pm.registerEvent(Type.REDSTONE_CHANGE, this.msbl, Priority.Normal, this);

		mobSpawnerMSCommand msCommand = new mobSpawnerMSCommand(this);
		mobSpawnerMSsetCommand msSetCommand = new mobSpawnerMSsetCommand(this);
		getCommand("ms").setExecutor(msCommand);
		getCommand("msset").setExecutor(msSetCommand);
		getCommand("msdebug").setExecutor(msDebug);

		setupPermissions();
		FileHandler.onStartUp();
		pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " " + pdfFile.getVersion() + " enabled");
	}

	/*----------------------------------------------*/
	/* Helper */
	/*----------------------------------------------*/
	/**
	 * add some blocks in a specified direction
	 * 
	 * @param location: location position of the current Location
	 * @param line: String in the format: direction numberofblocks
	 * @return the new Location
	 */
	public Location addLineToLocation(Location location, String line)
	{
		if (line.trim().equals(""))
		;
		String[] woerter = line.trim().split(" ");

		int numb = 0;
		if (woerter.length < 2 || !isADigit(woerter[1]))
		{
			return location;
		}
		else
		{
			numb = Integer.parseInt(woerter[1]);
		}

		if (woerter[0].equalsIgnoreCase("east"))
		{
			location.setZ(location.getZ() + numb);
		}
		else if (woerter[0].equalsIgnoreCase("north"))
		{
			location.setX(location.getX() + numb);
		}
		else if (woerter[0].equalsIgnoreCase("south"))
		{
			location.setX(location.getX() - numb);
		}
		else if (woerter[0].equalsIgnoreCase("west"))
		{
			location.setZ(location.getZ() - numb);
		}
		else if (woerter[0].equalsIgnoreCase("up"))
		{
			location.setY(location.getY() + numb);
		}
		else if (woerter[0].equalsIgnoreCase("down"))
		{
			location.setY(location.getY() - numb);
		}

		return location;
	}

	/**
	 * Spawn mobs specified by the parameters
	 * 
	 * @param name: name of the mob(Zombie, zombie, zOmbie ... is possible)
	 * @param anz: how many mobs with this name
	 * @param world: which world
	 * @param location: whiche location
	 */
	public void spawnMob(String name, int anz, World world, Location location)
	{
		if (name.length() < 2 || null == name) return;
		if (name.equalsIgnoreCase("PigZombie")) name = "PigZombie";
		boolean angry = false;
		DyeColor color = DyeColor.WHITE;
		boolean colorSheep = false;

		if (name.equalsIgnoreCase("Wolf:Angry"))
		{
			name = "Wolf";
			angry = true;
		}

		else if (name.length() > 10)
		{
			String begin = name.substring(0, 6);
			if (begin.equalsIgnoreCase("sheep:"))
			{
				String x = name.substring(6);
				color = getDyeColorFromName(x);
				colorSheep = true;
				name = "Sheep";
			}
		}
		else name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		if (FileHandler.read("mob." + name) == null || FileHandler.read("mob." + name).equalsIgnoreCase("false")) return;
		CreatureType ct = CreatureType.fromName(name);
		if (ct == null)
		{
			return;
		}

		for (int i = 0; i < anz; i++)
		{
			if (angry == true)
			{
				LivingEntity e = world.spawnCreature(location, ct);

				if (e instanceof Wolf)
				{
					Wolf w = (Wolf) e;
					w.setAngry(true);
				}
				continue;
			}
			if (colorSheep)
			{
				LivingEntity e = world.spawnCreature(location, ct);

				if (e instanceof Sheep)
				{
					Sheep s = (Sheep) e;
					s.setColor(color);
				}
				continue;
			}
			world.spawnCreature(location, ct);
		}
	}

	/**
	 * Spawn mobs specified by the parameters
	 * 
	 * @param name: name of the mob(Zombie, zombie, zOmbie ... is possible)
	 * @param anz: how many mobs with this name
	 * @param location: which location
	 * @param location: which player fired the command
	 */
	public void spawnMob(String name, int anz, Location location, Player p)
	{
		this.spawnMob(name, anz, p.getWorld(), location);
	}

	public int differenz(Date now, Date old)
	{
		Calendar cal_1 = new GregorianCalendar();
		cal_1.setTime(old);
		Calendar cal_2 = new GregorianCalendar();
		cal_2.setTime(now);
		long time = cal_2.getTime().getTime() - cal_1.getTime().getTime();
		int s = (int) time / 1000;

		return s;
	}

	/*----------------------------------------------*/
	/* Checker */
	/*----------------------------------------------*/
	public static boolean isADigit(String string)
	{
		try
		{
			Integer.parseInt(string);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	public static boolean isASupportedMob(String mobname)
	{
		if (mobname.equalsIgnoreCase("Pig")) return true;
		if (mobname.equalsIgnoreCase("Wolf")) return true;
		if (mobname.equalsIgnoreCase("Wolf:Angry")) return true;
		if (mobname.equalsIgnoreCase("Sheep")) return true;
		if (mobname.equalsIgnoreCase("Cow")) return true;
		if (mobname.equalsIgnoreCase("Spider")) return true;
		if (mobname.equalsIgnoreCase("Zombie")) return true;
		if (mobname.equalsIgnoreCase("Skeleton")) return true;
		if (mobname.equalsIgnoreCase("Chicken")) return true;
		if (mobname.equalsIgnoreCase("Squid")) return true;
		if (mobname.equalsIgnoreCase("PigZombie")) return true;
		if (mobname.equalsIgnoreCase("Creeper")) return true;
		if (mobname.equalsIgnoreCase("Ghast")) return true;
		if (mobname.equalsIgnoreCase("slime")) return true;
		return false;
	}

	private DyeColor getDyeColorFromName(String x)
	{
		DyeColor c;

		if (x.equalsIgnoreCase("orange")) c = DyeColor.ORANGE;
		else if (x.equalsIgnoreCase("magenta")) c = DyeColor.MAGENTA;
		else if (x.equalsIgnoreCase("LIGHT_BLUE")) c = DyeColor.LIGHT_BLUE;
		else if (x.equalsIgnoreCase("Lime")) c = DyeColor.LIME;
		else if (x.equalsIgnoreCase("PINK")) c = DyeColor.PINK;
		else if (x.equalsIgnoreCase("Silver")) c = DyeColor.SILVER;
		else if (x.equalsIgnoreCase("CYAN")) c = DyeColor.CYAN;
		else if (x.equalsIgnoreCase("PURPLE")) c = DyeColor.PURPLE;
		else if (x.equalsIgnoreCase("black")) c = DyeColor.BLACK;
		else if (x.equalsIgnoreCase("brown")) c = DyeColor.BROWN;
		else if (x.equalsIgnoreCase("blue")) c = DyeColor.BLUE;
		else if (x.equalsIgnoreCase("GRAY")) c = DyeColor.GRAY;
		else if (x.equalsIgnoreCase("GREEN")) c = DyeColor.GREEN;
		else if (x.equalsIgnoreCase("RED")) c = DyeColor.RED;
		else if (x.equalsIgnoreCase("YELLOW")) c = DyeColor.YELLOW;
		else c = DyeColor.WHITE;

		return c;
	}

	/*----------------------------------------------*/
	/* Permission */
	/*----------------------------------------------*/
	private void setupPermissions()
	{
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

		if (mobSpawner.permissionHandler == null)
		{
			if (permissionsPlugin != null)
			{
				mobSpawner.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
			}
			else
			{
				log.info("Permission system not detected, defaulting to OP");
			}
		}
	}

	public boolean hasPermission(Player p, String s)
	{
		if (null == permissionHandler)
		{
			return p.isOp();
		}
		if (mobSpawner.permissionHandler.has(p, s))
		{
			return true;
		}
		return false;
	}

	public static Block[] getBlocks(Location l, World world)
	{
		return null;
	}
}
