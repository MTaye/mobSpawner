package me.cr3dos.mobSpawner.file;

import java.io.File;

import me.cr3dos.mobSpawner.MobSpawner;

import org.bukkit.util.config.Configuration;

public class FileHandler
{
	/**
	 * Settings signTime how long waiting for next pressing on a sign DebugLevel
	 * the Level of debugging send message of this person cmdTime how long
	 * waiting for next spawncommand
	 */
	private static String mainDirectory = "plugins/mobSpawner";
	private static File file = new File(mainDirectory + File.separator + "config.yml");

	private static Configuration config;

	/*----------------------------------------------*/
	/* staring/load */
	/*----------------------------------------------*/
	private static Configuration load()
	{
		deletSecondFolder();
		try
		{
			Configuration config = new Configuration(file);
			config.load();
			return config;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void onStartUp()
	{
		new File(mainDirectory).mkdir();

		deletSecondFolder();

		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		config = load();
		defaultSetting();
	}

	private static void defaultSetting()
	{
		if (null == config.getProperty("cmdTime")) write("cmdTime", "0");
		if (null == config.getProperty("signTime")) write("signTime", "0");
		if (null == config.getProperty("mob.Wolf")) write("mob.Wolf", "true");
		if (null == config.getProperty("mob.Pig")) write("mob.Pig", "true");
		if (null == config.getProperty("mob.Sheep")) write("mob.Sheep", "true");
		if (null == config.getProperty("mob.Cow")) write("mob.Cow", "true");
		if (null == config.getProperty("mob.Spider")) write("mob.Spider", "true");
		if (null == config.getProperty("mob.Zombie")) write("mob.Zombie", "true");
		if (null == config.getProperty("mob.Skeleton")) write("mob.Skeleton", "true");
		if (null == config.getProperty("mob.Chicken")) write("mob.Chicken", "true");
		if (null == config.getProperty("mob.Squid")) write("mob.Squid", "true");
		if (null == config.getProperty("mob.PigZombie")) write("mob.PigZombie", "true");
		if (null == config.getProperty("mob.Creeper")) write("mob.Creeper", "true");
		if (null == config.getProperty("mob.Ghast")) write("mob.Ghast", "true");
		if (null == config.getProperty("mob.Slime")) write("mob.Slime", "true");
		if (null == config.getProperty("spawnItem")) write("spawnItem", "270");
	}

	private static void deletSecondFolder()
	{
		if (new File("MobSpawner").exists())
		{
			File f = new File("MobSpawner");
			FileHandler.deleteFolders(f);
			f.delete();
		}
	}

	private static void deleteFolders(File file)
	{
		for (File f : file.listFiles())
		{
			if (f.isDirectory()) deleteFolders(f);
			else f.delete();
		}
		deletSecondFolder();
	}

	/*----------------------------------------------*/
	/* writing */
	/*----------------------------------------------*/
	public static void write(String key, String input)
	{
		if (null == config)
		{
			config = load();
		}
		config.setProperty(key, input);
		config.save();
		deletSecondFolder();
	}

	/**
	 * 
	 * @param key
	 * @param input
	 * @return
	 */
	public static boolean writeInt(String key, String input)
	{
		if (MobSpawner.isADigit(input))
		{
			if (null == config)
			{
				config = load();
			}
			config.setProperty(key, input);
			config.save();
			deletSecondFolder();
			return true;
		}
		deletSecondFolder();
		return false;

	}

	/*----------------------------------------------*/
	/* reading */
	/*----------------------------------------------*/
	public static String read(String root)
	{
		if (null == config)
		{
			config = load();
		}
		return config.getString(root);
	}

	public static int readInt(String root)
	{
		if (null == config)
		{
			config = load();
		}
		String s = config.getString(root);
		if (null == s)
		{
			return 0;
		}
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

}
