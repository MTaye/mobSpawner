package me.cr3dos.mobSpawner.file;

import java.io.File;

import me.cr3dos.mobSpawner.mobSpawner;

import org.bukkit.util.config.Configuration;

public class FileHandler
{
	private static String mainDirectory = "plugins/mobSpawner";
	private static File file = new File(mainDirectory + File.separator + "config.yml");
	
	private static Configuration config;
	
	/*----------------------------------------------*/
	/* staring/load                                 */
	/*----------------------------------------------*/	
	public static Configuration load(){
        try {
            Configuration config = new Configuration(file);
            config.load();
            return config;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
    public static void onStartUp()
    {
    	new File(mainDirectory).mkdir();


        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            } 
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else
        {
        	config = load();
        }
    }
	
	
	/*----------------------------------------------*/
	/* writing                                      */
	/*----------------------------------------------*/	
    public static void write(String key, String input){ 
        config.setProperty(key, input);
        config.save();
    }

	public static boolean writeInt(String key, String input) {
		try
		{
			Integer.parseInt(input);
	        config.setProperty(key, input);
	        config.save();
	        return true;
		}
    	catch(NumberFormatException e)
    	{
    		return false;
    	}
	}
	/*----------------------------------------------*/
	/* reading                                      */
	/*----------------------------------------------*/	
    public static String read(String root){
        return config.getString(root);
    }
    
    public static  int readInt(String root){
    	String s = config.getString(root);
    	if(null == s)
    	{
    		return 0;
    	}
    	try{
    		return Integer.parseInt(s);
    	}
    	catch(NumberFormatException e)
    	{
    		return 0;
    	}
    }


	
}
