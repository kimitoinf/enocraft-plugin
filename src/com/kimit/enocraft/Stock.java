package com.kimit.enocraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Stock
{
	public static Inventory STOCK = Bukkit.createInventory(null, 27, "Stock");
	public static final int[] POSITION = {10, 12, 14, 16, 19, 21, 23, 25, 28, 30, 32, 34, 37, 39, 41, 43, 46, 48, 50, 52};
	public static long[] Price = new long[8];
	public static ArrayList<String> LORE = new ArrayList<String>();
	public static long DeathCount = 0;
	public static final long DEATHWEIGHT = 100000;
	public static final long DELISTING = 10000;
	public static long[] WEIGHT = Items.WEIGHT.clone();
	public static final Material[] COMPANY =
			{
					Material.BEEF,
					Material.DIAMOND,
					Material.NETHERITE_SWORD,
					Material.WHEAT,
					Material.EMERALD,
					Material.COD,
					Material.QUARTZ_BLOCK,
					Material.SPIDER_EYE
			};
	public static final String[] NAME =
			{
					"말딸축산",
					"(주)아오지광업",
					"화나생명",
					"(주)비닐하우스",
					"쌀먹물산",
					"소말리아수산",
					"야가다건설",
					"나사"
			};

	public static void init()
	{
		LORE.add("현재가 : ");
		LORE.add("좌클릭 시 1주 매도합니다.");
		LORE.add("우클릭 시 1주 매수합니다.");
		LORE.add("SHIFT + 좌클릭 시 모두 매도합니다.");
		LORE.add("SHIFT + 우클릭 시 10주 매수합니다.");
		LORE.add("");
		for (int loop = Items.Item.WHEAT.ordinal(); loop != Items.Item.EMERALD.ordinal(); loop++)
			WEIGHT[loop] = (long)Math.floor((double)Items.WEIGHT[loop] / 10.0);
		update();
	}

	public static double getProbability(long x)
	{
		double temp = (double)x;
		return (0.6 * temp / (2500000 + temp)) + 0.4;
	}

	public static void setPrice(int start, int end, double rate, int pos)
	{
		Random random = new Random();
		int range = random.nextInt((int)((double)Price[pos] * rate));
		long x = 0;
		for (int loop = start; loop != end; loop++)
			x += Items.Count[loop] * WEIGHT[loop];
		double number = random.nextDouble();
		if (number > getProbability(x)) Price[pos] -= range;
		else Price[pos] += range;
	}

	public static void update()
	{
		setPrice(Items.Item.BEEF.ordinal(), Items.Item.DIAMOND.ordinal(), 0.09, 0);
		setPrice(Items.Item.DIAMOND.ordinal(), Items.Item.WHEAT.ordinal(), 0.09, 1);
		setPrice(Items.Item.WHEAT.ordinal(), Items.Item.EMERALD.ordinal(), 0.09, 3);
		setPrice(Items.Item.EMERALD.ordinal(), Items.Item.COD.ordinal(), 0.13, 4);
		setPrice(Items.Item.COD.ordinal(), Items.Item.QUARTZ_BLOCK.ordinal(), 0.13, 5);
		setPrice(Items.Item.QUARTZ_BLOCK.ordinal(), Items.Item.SPIDER_EYE.ordinal(), 0.11, 6);
		setPrice(Items.Item.SPIDER_EYE.ordinal(), Items.Item.END.ordinal(), 0.13, 7);
		Random random = new Random();
		int range = random.nextInt((int)((double)Price[2] * 0.20));
		if (random.nextDouble() > getProbability(DEATHWEIGHT * DeathCount)) Price[2] -= range;
		else Price[2] += range;
		updateLore();
	}

	public static void updateLore()
	{
		for (int loop = 0; loop != 8; loop++)
		{
			LORE.set(0, "현재가 : " + Long.toString(Price[loop]));
			ItemStack item = new ItemStack(COMPANY[loop]);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(NAME[loop]);
			meta.setLore(LORE);
			item.setItemMeta(meta);
			STOCK.setItem(Stock.POSITION[loop], item);
		}
	}

	public static void delisting()
	{
		for (int loop = 0; loop != 8; loop++)
		{
			if (Price[loop] < DELISTING)
			{
				Bukkit.broadcastMessage(Main.PREFIX + NAME[loop] + " 주식이 상장폐지되었습니다.");
				for (int playerloop = 0; playerloop != Main.playerinfos.size(); playerloop++)
				{
					long[] stocks = Main.playerinfos.get(playerloop).getStocks();
					stocks[loop] = 0;
					Main.playerinfos.get(playerloop).totalstocks -= stocks[loop];
					Main.playerinfos.get(playerloop).setStocks(stocks);
					Main.playerinfos.get(playerloop).setInfo();
				}
				File playersfolder = new File(Bukkit.getPluginManager().getPlugin("Enocraft-plugin").getDataFolder().toString() + "/players");
				File[] playersfile = playersfolder.listFiles();
				for (var fileloop : playersfile)
				{
					FileConfiguration player = YamlConfiguration.loadConfiguration(fileloop);
					player.set("Company" + Integer.toString(loop), 0);
				}
				Price[loop] = 300000;
			}
		}
	}
}
