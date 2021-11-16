package com.kimit.enocraft;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main extends JavaPlugin
{
	public Logger logger = getServer().getLogger();
	public static final String PREFIX = "[Enocraft] ";
	public static ArrayList<PlayerInfo> playerinfos = new ArrayList<PlayerInfo>();
	public File pricefile = new File(getDataFolder(), "/price.yml");
	public FileConfiguration price = YamlConfiguration.loadConfiguration(pricefile);
	public File locationfile = new File(getDataFolder(), "/location.yml");
	public FileConfiguration location = YamlConfiguration.loadConfiguration(locationfile);
	public File stockfile = new File(getDataFolder(), "/stock.yml");
	public FileConfiguration stock = YamlConfiguration.loadConfiguration(stockfile);
	public File marketfile = new File(getDataFolder(), "/market.yml");
	public FileConfiguration market = YamlConfiguration.loadConfiguration(marketfile);

	@Override
	public void onEnable()
	{
		super.onEnable();
		logger.info(PREFIX + "Enocraft plugin is enabled.");
		if (pricefile.exists())
		{
			for (Items.Item loop : Items.Item.values())
				Items.Price[loop.ordinal()] = price.getInt(loop.toString());
		}
		else
		{
			File folder = new File(getDataFolder().toString());
			folder.mkdir();
			try
			{
				pricefile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			Items.Price = Items.WEIGHT.clone();
		}

		if (!locationfile.exists())
		{
			try
			{
				locationfile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		if (stockfile.exists())
		{
			for (int loop = 0; loop != 8; loop++)
				Stock.Price[loop] = stock.getLong("Company" + Integer.toString(loop));
		}
		else
		{
			try
			{
				stockfile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			Arrays.fill(Stock.Price, 300000);
		}

		if (marketfile.exists())
		{
			for (int loop = 0; loop != Market.MARKET.getSize(); loop++)
				Market.MARKET.setItem(Market.MARKET.firstEmpty(), market.getItemStack(Integer.toString(loop)));
		}
		else
		{
			try
			{
				marketfile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
			playerinfos.add(playerInfo);
		}

		Arrays.fill(Items.Count, 0);
		Arrays.fill(Items.PrevCount, 0);
		Merchant.init();
		Stock.init();
		Market.init();
		this.getCommand("setshop").setExecutor(new Commands());
		this.getCommand("shop").setExecutor(new Commands());
		this.getCommand("stock").setExecutor(new Commands());
		this.getCommand("dragonegg").setExecutor(new Commands());
		this.getCommand("playerinfo").setExecutor(new Commands());
		this.getCommand("send").setExecutor(new Commands());
		this.getCommand("market").setExecutor(new Commands());
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, Schedule.UPDATE, 20 * 60 * 40, 20 * 60 * 40);
		scheduler.scheduleSyncRepeatingTask(this, Schedule.STOCK, 20 * 60, 20 * 60);
		Bukkit.getPluginManager().registerEvents(new Event(), this);
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		logger.info(PREFIX + "Enocraft plugin is disabled.");
		for (Items.Item loop : Items.Item.values())
			price.set(loop.toString(), Items.Price[loop.ordinal()]);
		try
		{
			price.save(pricefile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		for (int loop = 0; loop != 8; loop++)
			stock.set("Company" + Integer.toString(loop), Stock.Price[loop]);
		try
		{
			stock.save(stockfile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		for (int loop = 0; loop != Market.MARKET.getSize(); loop++)
			market.set(Integer.toString(loop), Market.MARKET.getItem(loop));
		try
		{
			market.save(marketfile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
