package com.kimit.enocraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
	public File constantfile = new File(getDataFolder(), "/constant.yml");
	public FileConfiguration constant = YamlConfiguration.loadConfiguration(constantfile);

	public static long STOCKINCREASE;
	public static long MERCHANTINCREASE;
	public static long MERCHANTDECREASE;
	public static double MERCHANTMINING;
	public static double MERCHANTEXPLORING;
	public static double MERCHANTETC;

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
			{
				int marketpos = Market.MARKET.firstEmpty();
				Market.MARKET.setItem(marketpos, market.getItemStack(Integer.toString(loop) + ".head"));
				ItemStack[] stacks = new ItemStack[market.getInt(Integer.toString(loop) + ".count")];
				for (int stackloop = 0; stackloop != market.getInt(Integer.toString(loop) + ".count"); stackloop++)
					stacks[stackloop] = market.getItemStack(Integer.toString(loop) + "." + Integer.toString(stackloop));
				Market.ITEMS.add(marketpos, stacks);
			}
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

		if (constantfile.exists())
		{
			STOCKINCREASE = constant.getLong("stock-increase");
			MERCHANTINCREASE = constant.getLong("merchant-increase");
			MERCHANTDECREASE = constant.getLong("merchant-decrease");
			MERCHANTMINING = constant.getDouble("merchant-mining");
			MERCHANTEXPLORING = constant.getDouble("merchant-exploring");
			MERCHANTETC = constant.getDouble("merchant-etc");
		}
		else
		{
			try
			{
				constantfile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			STOCKINCREASE = 324500;
			MERCHANTINCREASE = 1;
			MERCHANTDECREASE = 350000;
			MERCHANTMINING = 0.182;
			MERCHANTEXPLORING = 0.042;
			MERCHANTETC = 0.092;
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
		this.getCommand("constant").setExecutor(new Commands());
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

		int count = 0;
		for (var loop : Market.MARKET.getContents())
		{
			if (loop != null && loop.getType() != Material.AIR)
				count++;
		}
		for (int loop = 0; loop != count; loop++)
		{
			market.set(Integer.toString(loop) + ".head", Market.MARKET.getItem(loop));
			market.set(Integer.toString(loop) + ".count", Market.ITEMS.get(loop).length);
			for (int stackloop = 0; stackloop != Market.ITEMS.get(loop).length; stackloop++)
				market.set(Integer.toString(loop) + "." + Integer.toString(stackloop), Market.ITEMS.get(loop)[stackloop]);
		}
		try
		{
			market.save(marketfile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		constant.set("stock-increase", STOCKINCREASE);
		constant.set("merchant-increase", MERCHANTINCREASE);
		constant.set("merchant-decrease", MERCHANTDECREASE);
		constant.set("merchant-mining", MERCHANTMINING);
		constant.set("merchant-exploring", MERCHANTEXPLORING);
		constant.set("merchant-etc", MERCHANTETC);
		try
		{
			constant.save(constantfile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
