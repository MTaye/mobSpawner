package me.cr3dos.mobSpawner.file;

import java.io.File;

import me.cr3dos.mobSpawner.mobSpawner;

import org.bukkit.util.config.Configuration;

public class FileHandler
{
	/**
	 * Settings signTime how long waiting for next pressing on a sign DebugLevel
	 * the Level of debugging send message of this person cmdTime how long
	 * waiting for next spawncommand
	 */
	private static String mainDirectory = "plugins/mobSpawner";
	private static File file = new File(mainDirectory + File.separator
			+ "config.yml");

	private static Configuration config;

	/*----------------------------------------------*/
	/* staring/load */
	/*----------------------------------------------*/
	private static Configuration load()
	{
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
	}

	/*----------------------------------------------*/
	/* writing */
	/*----------------------------------------------*/
	public static void write(String key, String input)
	{
		config.setProperty(key, input);
		config.save();
	}

	/**
	 * 
	 * @param key
	 * @param input
	 * @return
	 */
	public static boolean writeInt(String key, String input)
	{
		if (mobSpawner.isADigit(input))
		{
			config.setProperty(key, input);
			config.save();
			return true;
		}
		return false;

	}

	/*----------------------------------------------*/
	/* reading */
	/*----------------------------------------------*/
	public static String read(String root)
	{
		return config.getString(root);
	}

	public static int readInt(String root)
	{
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
