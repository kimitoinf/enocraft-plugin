package com.kimit.enocraft;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Market
{
	public static Inventory MARKET = Bukkit.createInventory(null, 54, "Market");
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
		for (var loop : items)
			lore.add(loop.getType().name() + " " + Integer.toString(loop.getAmount()) + " 개");
		meta.setLore(lore);
		item.setItemMeta(meta);
		MARKET.setItem(MARKET.firstEmpty(), item);
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
		for (int loop = 2; loop != lore.size(); loop++)
		{
			String origin = lore.get(loop);
			String[] split = origin.split(" ");
			ItemStack item = new ItemStack(Material.getMaterial(split[0]), Integer.parseInt(split[1]));
			Main.playerinfos.get(playerpos).MARKETRECEIVE.setItem(Main.playerinfos.get(playerpos).MARKETRECEIVE.firstEmpty(), item);
		}
		MARKET.remove(MARKET.getItem(slot));
		String[] split = lore.get(0).split(" ");
		int sellerpos = PlayerInfo.getPlayer(Bukkit.getPlayer(split[2]));
		Main.playerinfos.get(sellerpos).setMoney(Main.playerinfos.get(sellerpos).getMoney() + price);
		Bukkit.getPlayer(split[2]).sendMessage("당신의 물품이 판매되었습니다.");
		Main.playerinfos.get(sellerpos).downMarket();
		PlayerInfo.updateScoreboard(player);
		PlayerInfo.updateScoreboard(Bukkit.getPlayer(split[2]));
		return price;
	}
}
