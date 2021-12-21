package com.kimit.enocraft;

import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;

public class Market
{
	public static Inventory MARKET = Bukkit.createInventory(null, 54, "Market");
	public static ArrayList<ItemStack[]> ITEMS = new ArrayList<ItemStack[]>();
	public static ArrayList<String> LORE = new ArrayList<String>();

	public static void init()
	{
		LORE.add("판매자 : ");
		LORE.add("가격 : ");
	}

	public static void send(Player player, long price)
	{
		int playerpos = PlayerInfo.getPlayer(player);
		ItemStack[] contents = Main.playerinfos.get(playerpos).MARKETSEND.getContents();
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (var loop : contents)
		{
			if (loop != null && loop.getType() != Material.AIR)
				items.add(loop);
		}
		ItemStack item = new ItemStack(items.get(0).getType(), 1);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) LORE.clone();
		lore.set(0, "판매자 : " + player.getName());
		lore.set(1, "가격 : " + Long.toString(price));
		int marketpos = MARKET.firstEmpty();
		ItemStack[] stacks = new ItemStack[items.size()];
		for (int loop = 0; loop != items.size(); loop++)
		{
			lore.add(items.get(loop).getType().toString() + " " + Integer.toString(items.get(loop).getAmount()) + " 개");
			NBTItem nbt = new NBTItem(items.get(loop));
			player.sendMessage(nbt.toString());
			stacks[loop] = items.get(loop);
		}
		ITEMS.add(marketpos, stacks);
		meta.setLore(lore);
		meta.setDisplayName("판매 물품");
		item.setItemMeta(meta);
		MARKET.setItem(marketpos, item);
		Main.playerinfos.get(playerpos).marketsendopen = false;
		Main.playerinfos.get(playerpos).upMarket();
		long fee = (long)Math.ceil((double)price / 10.0);
		Main.playerinfos.get(playerpos).setMoney(Main.playerinfos.get(playerpos).getMoney() - fee);
		player.sendMessage("수수료 " + Long.toString(fee) + " 원이 부과되었으며 정상적으로 등록되었습니다.");
		Main.playerinfos.get(playerpos).MARKETSEND.clear();
		PlayerInfo.updateScoreboard(player);
	}

	public static long purchase(Player player, int slot)
	{
		int playerpos = PlayerInfo.getPlayer(player);
		ItemStack[] contents = MARKET.getContents();
		ItemMeta meta = contents[slot].getItemMeta();
		List<String> lore = meta.getLore();
		long price = Long.parseLong(lore.get(1).replaceAll("[^0-9]", ""));
		final long money = Main.playerinfos.get(playerpos).getMoney();
		if (price > money)
			return -1;
		Main.playerinfos.get(playerpos).setMoney(money - price);
		PlayerInfo.updateScoreboard(player);
		for (int loop = 0; loop != ITEMS.get(slot).length; loop++)
			Main.playerinfos.get(playerpos).MARKETRECEIVE.setItem(Main.playerinfos.get(playerpos).MARKETRECEIVE.firstEmpty(), ITEMS.get(slot)[loop]);
		MARKET.remove(MARKET.getItem(slot));
		ITEMS.remove(slot);
		String[] split = lore.get(0).split(" ");
		if (Bukkit.getPlayer(split[2]) != null)
		{
			int sellerpos = PlayerInfo.getPlayer(Bukkit.getPlayer(split[2]));
			Main.playerinfos.get(sellerpos).setMoney(Main.playerinfos.get(sellerpos).getMoney() + price);
			Bukkit.getPlayer(split[2]).sendMessage("당신의 물품이 판매되었습니다.");
			Main.playerinfos.get(sellerpos).downMarket();
			PlayerInfo.updateScoreboard(Bukkit.getPlayer(split[2]));
		}
		else
		{
			File playerfile = new File(Bukkit.getPluginManager().getPlugin("Enocraft-plugin").getDataFolder(), "/players/" + Bukkit.getOfflinePlayer(split[2]).getUniqueId().toString() + ".yml");
			FileConfiguration playerdata = YamlConfiguration.loadConfiguration(playerfile);
			playerdata.set("Money", playerdata.getLong("Money") + price);
			try
			{
				playerdata.save(playerfile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return price;
	}
}
