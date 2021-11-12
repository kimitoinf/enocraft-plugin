package com.kimit.enocraft;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Merchant
{
	public static final int[] POSITION = {1, 3, 5, 7, 10, 12, 14, 16, 19, 21, 23, 25, 28, 30, 32, 34, 37, 39, 41, 43, 46, 48, 50, 52};
	public static ArrayList<String> PURCHASELORE = new ArrayList<String>();
	public static ArrayList<String> NOTPURCHASELORE = new ArrayList<String>();
	public static Inventory LIVESTOCK = Bukkit.createInventory(null, 18, "Livestock");
	public static Inventory MINING = Bukkit.createInventory(null, 18, "Mining");
	public static Inventory FARMING = Bukkit.createInventory(null, 36, "Farming");
	public static Inventory FISHING = Bukkit.createInventory(null, 9, "Fishing");
	public static Inventory ARCHITECTURE = Bukkit.createInventory(null, 18, "Architecture");
	public static Inventory EXPLORING = Bukkit.createInventory(null, 45, "Exploring");

	public static void init()
	{
		PURCHASELORE.add("판매 가격 : ");
		PURCHASELORE.add("구매 가격 : ");
		PURCHASELORE.add("좌클릭 시 1개 판매합니다.");
		PURCHASELORE.add("우클릭 시 1개 구매합니다.");
		PURCHASELORE.add("SHIFT + 좌클릭 시 모두 판매합니다.");
		PURCHASELORE.add("SHIFT + 우클릭 시 64개 구매합니다.");
		NOTPURCHASELORE.add("판매 가격 : ");
		NOTPURCHASELORE.add("좌클릭 시 1개 판매합니다.");
		NOTPURCHASELORE.add("SHIFT + 좌클릭 시 모두 판매합니다.");
		update(LIVESTOCK, Items.Item.BEEF.ordinal(), Items.Item.DIAMOND.ordinal(), false);
		update(MINING, Items.Item.DIAMOND.ordinal(), Items.Item.WHEAT.ordinal(), false);
		update(FARMING, Items.Item.WHEAT.ordinal(), Items.Item.EMERALD.ordinal(), false);
		update(FISHING, Items.Item.COD.ordinal(), Items.Item.QUARTZ_BLOCK.ordinal(), false);
		update(ARCHITECTURE, Items.Item.QUARTZ_BLOCK.ordinal(), Items.Item.SPIDER_EYE.ordinal(), true);
		update(EXPLORING, Items.Item.SPIDER_EYE.ordinal(), Items.Item.END.ordinal(), false);
	}

	public static void update(Inventory inventory, int start, int end, boolean purchase)
	{
		int posloop = 0;
		for (int loop = start; loop != end; loop++)
		{
			ItemStack item = new ItemStack(Items.MATERIAL[loop]);
			ItemMeta meta = item.getItemMeta();
			if (purchase)
			{
				PURCHASELORE.set(0, "판매 가격 : " + Long.toString(Items.Price[loop]) + " 원");
				PURCHASELORE.set(1, "구매 가격 : " + Long.toString(2 * Items.Price[loop]) + " 원");
				meta.setLore(PURCHASELORE);
			}
			else
			{
				NOTPURCHASELORE.set(0, "판매 가격 : " + Long.toString(Items.Price[loop]) + " 원");
				meta.setLore(NOTPURCHASELORE);
			}
			item.setItemMeta(meta);
			inventory.setItem(POSITION[posloop], item);
			posloop++;
		}
	}
}
