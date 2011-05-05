package me.cr3dos.mobSpawner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.entity.Player;
import me.cr3dos.mobSpawner.file.*;
public class User
{
	Player p;
	Date lastTimeUsignSign;
	
	public boolean timeOver()
	{
		if( FileHandler.readInt("signWait") <= differenz(new Date(), lastTimeUsignSign))
		{
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	private int differenz(Date now, Date old) {
		
		Calendar cal_1 = new GregorianCalendar();
		Calendar cal_2 = new GregorianCalendar();
		cal_1.set(old.getYear(), old.getMonth(), old.getDate(), old.getHours(), old.getMinutes(), old.getSeconds());                     				
		cal_2.set(now.getYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds());            
		long time = cal_2.getTime().getTime() - cal_1.getTime().getTime();  
		int s = (int)time / 1000;     					
		
		return s;
	}
}
