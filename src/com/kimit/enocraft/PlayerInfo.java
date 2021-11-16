package com.kimit.enocraft;

import fr.minuskube.netherboard.Netherboard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerInfo
{
	private UUID uuid;
	private File playerfile;
	private FileConfiguration player;
	private long money = 0;
	private long[] stocks = new long[8];
	private int market = 0;
	public long totalstocks = 0;
	public long totalinvest = 0;
	public Inventory STOCK = Bukkit.createInventory(null, 27, "Stock");
	public Inventory MARKETSEND = Bukkit.createInventory(null, 27, "Send");
	public Inventory MARKETRECEIVE = Bukkit.createInventory(null, 27, "Receive");
	public boolean marketsendopen = false;
	public int taskid;

	PlayerInfo(UUID uuid)
	{
		this.uuid = uuid;
		Arrays.fill(stocks, 0);
		playerfile = new File(Bukkit.getPluginManager().getPlugin("Enocraft-plugin").getDataFolder(), "/players/" + uuid.toString() + ".yml");
		player = YamlConfiguration.loadConfiguration(playerfile);
		if (!playerfile.exists())
		{
			File folder = new File(Bukkit.getPluginManager().getPlugin("Enocraft-plugin").getDataFolder().toString() + "/players");
			folder.mkdir();
			try
			{
				playerfile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			setInfo();
		}
		else
		{
			money = player.getLong("Money");
			for (int loop = 0; loop != 8; loop++)
				stocks[loop] = player.getLong("Company" + Integer.toString(loop));
			totalstocks = player.getLong("Stocks");
			totalinvest = player.getLong("Invest");
			market = player.getInt("Market");
			ItemStack[] contents = new ItemStack[MARKETRECEIVE.getContents().length];
			for (int loop = 0; loop != MARKETRECEIVE.getContents().length; loop++)
				contents[loop] = player.getItemStack("Receives." + Integer.toString(loop));
		}
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public long getMoney()
	{
		return money;
	}

	public void setMoney(long money)
	{
		this.money = money;
	}

	public long[] getStocks()
	{
		return stocks;
	}

	public void setStocks(long[] stocks)
	{
		this.stocks = stocks;
	}

	public int getMarket()
	{
		return market;
	}

	public void upMarket()
	{
		market++;
	}

	public void downMarket()
	{
		market--;
	}

	public void setInfo()
	{
		player.set("Money", money);
		for (int loop = 0; loop != 8; loop++)
			player.set("Company" + Integer.toString(loop), stocks[loop]);
		player.set("Stocks", totalstocks);
		player.set("Invest", totalinvest);
		player.set("Market", market);
		for (int loop = 0; loop != MARKETRECEIVE.getContents().length; loop++)
			player.set("Receives." + Integer.toString(loop), MARKETRECEIVE.getContents()[loop]);
		try
		{
			player.save(playerfile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static int getPlayer(Player player)
	{
		int playerpos = 0;
		for (int loop = 0; loop != Main.playerinfos.size(); loop++)
		{
			if (player.getUniqueId().equals(Main.playerinfos.get(loop).getUuid()))
			{
				playerpos = loop;
				break;
			}
		}
		return playerpos;
	}

	public static long sellItem(Player player, Material product, boolean shift)
	{
		ItemStack[] items = player.getInventory().getContents();
		int amount = 0;
		int itempos = 0;
		long total = 0;
		for (int loop = 0; loop != items.length; loop++)
		{
			if ((items[loop] != null) && (items[loop].getType() == product) && (items[loop].getAmount() > 0))
			{
				amount += items[loop].getAmount();
				itempos = loop;
			}
		}
		if (amount == 0) return -1;
		int pos = 0;
		for (int loop = 0; loop != Items.MATERIAL.length; loop++)
		{
			if (Items.MATERIAL[loop].equals(product))
			{
				pos = loop;
				break;
			}
		}
		if (shift)
		{
			total = Items.Price[pos] * amount;
			player.getInventory().remove(product);
		}
		else
		{
			total = Items.Price[pos];
			items[itempos].setAmount(items[itempos].getAmount() - 1);
			player.getInventory().setContents(items);
		}
		player.updateInventory();
		Main.playerinfos.get(getPlayer(player)).setMoney(Main.playerinfos.get(getPlayer(player)).getMoney() + total);
		return total;
	}

	public static long purchaseItem(Player player, Material product, boolean shift)
	{
		int playerpos = getPlayer(player);
		int pos = 0;
		for (int loop = 0; loop != Items.MATERIAL.length; loop++)
		{
			if (Items.MATERIAL[loop].equals(product))
			{
				pos = loop;
				break;
			}
		}
		final long money = Main.playerinfos.get(playerpos).getMoney();
		long total = 0;

		if (shift)
		{
			total = 2 * Items.Price[pos] * 64;
			if (money < total) return -1;
			Main.playerinfos.get(playerpos).setMoney(money - total);
			player.getInventory().addItem(new ItemStack(product, 64));
		}
		else
		{
			total = 2 * Items.Price[pos];
			if (money < total) return -1;
			Main.playerinfos.get(playerpos).setMoney(money - total);
			player.getInventory().addItem(new ItemStack(product, 1));
		}
		return total;
	}

	public static void updateScoreboard(Player player)
	{
		Netherboard.instance().getBoard(player).setAll("닉네임 : " + player.getName(), "돈 : " + Long.toString(Main.playerinfos.get(getPlayer(player)).getMoney()) + " 원");
	}

	public static long sellStock(Player player, Material company, boolean shift)
	{
		int pos = 0;
		for (int loop = 0; loop < 8; loop++)
		{
			if (Stock.COMPANY[loop] == company)
			{
				pos = loop;
				break;
			}
		}
		int playerpos = getPlayer(player);
		long stock = Main.playerinfos.get(playerpos).getStocks()[pos];
		long[] stocks = Main.playerinfos.get(playerpos).getStocks();
		long total = 0;
		if (stock == 0) return -1;
		if (shift)
		{
			total = stock * Stock.Price[pos];
			Main.playerinfos.get(playerpos).totalstocks -= stocks[pos];
			stocks[pos] = 0;
		}
		else
		{
			total = Stock.Price[pos];
			Main.playerinfos.get(playerpos).totalstocks -= 1;
			stocks[pos] -= 1;
		}
		Main.playerinfos.get(playerpos).setStocks(stocks);
		Main.playerinfos.get(playerpos).setMoney(Main.playerinfos.get(playerpos).getMoney() + total);
		Main.playerinfos.get(playerpos).totalinvest -= total;
		if (Main.playerinfos.get(playerpos).totalstocks <= 0)
		{
			Main.playerinfos.get(playerpos).totalinvest = 0;
			Main.playerinfos.get(playerpos).totalstocks = 0;
		}
		return total;
	}

	public static long purchaseStock(Player player, Material company, boolean shift)
	{
		int pos = 0;
		for (int loop = 0; loop < 8; loop++)
		{
			if (Stock.COMPANY[loop] == company)
			{
				pos = loop;
				break;
			}
		}
		int playerpos = getPlayer(player);
		long total = 0;
		final long money = Main.playerinfos.get(playerpos).getMoney();
		long[] stocks = Main.playerinfos.get(playerpos).getStocks();
		if (shift)
		{
			total = 10 * Stock.Price[pos];
			if (money < total) return -1;
			stocks[pos] += 10;
			Main.playerinfos.get(playerpos).totalstocks += 10;
		}
		else
		{
			total = Stock.Price[pos];
			if (money < total) return -1;
			stocks[pos] += 1;
			Main.playerinfos.get(playerpos).totalstocks += 1;
		}
		Main.playerinfos.get(playerpos).setMoney(money - total);
		Main.playerinfos.get(playerpos).setStocks(stocks);
		Main.playerinfos.get(playerpos).totalinvest += total;
		return total;
	}

	public static void updateStock(Player player)
	{
		int playerpos = PlayerInfo.getPlayer(player);
		ItemStack[] contents = Stock.STOCK.getContents();
		for (int loop = 0; loop != 8; loop++)
		{
			ItemMeta meta = contents[Stock.POSITION[loop]].getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(5, Long.toString(Main.playerinfos.get(playerpos).stocks[loop]) + " 주 보유");
			meta.setLore(lore);
			contents[Stock.POSITION[loop]].setItemMeta(meta);
		}
		Main.playerinfos.get(playerpos).STOCK.setContents(contents);
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		long[] stocks = Main.playerinfos.get(playerpos).getStocks();
		long total = 0;
		long totalinvest = Main.playerinfos.get(playerpos).totalinvest;
		for (int loop = 0; loop != 8; loop++)
			total += stocks[loop] * Stock.Price[loop];
		lore.add("총 투자 금액 : " + Long.toString(totalinvest) + " 원");
		lore.add("현재 가치 : " + Long.toString(total) + " 원");
		lore.add("손익률 : " + Double.toString(Math.round(((double)100 * total / totalinvest - (double)100) * 100) / 100.0) + " %");
		meta.setLore(lore);
		meta.setDisplayName("통계");
		item.setItemMeta(meta);
		Main.playerinfos.get(playerpos).STOCK.setItem(4, item);
		player.updateInventory();
	}
}
