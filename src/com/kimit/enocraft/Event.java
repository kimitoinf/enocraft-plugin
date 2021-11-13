package com.kimit.enocraft;

import de.tr7zw.nbtapi.NBTItem;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Event implements Listener
{
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerInfo player = new PlayerInfo(e.getPlayer().getUniqueId());
		Main.playerinfos.add(player);
		BPlayerBoard board = Netherboard.instance().createBoard(e.getPlayer(), "플레이어 정보");
		board.setAll("닉네임 : " + e.getPlayer().getName(), "돈 : " + Long.toString(player.getMoney()) + " 원");
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		int player = PlayerInfo.getPlayer(e.getPlayer());
		Main.playerinfos.get(player).setInfo();
		Main.playerinfos.remove(player);
	}

	@EventHandler
	public void blockDropItem(BlockDropItemEvent e)
	{
		for (Item loop : e.getItems())
		{
			NBTItem nbt = new NBTItem(loop.getItemStack());
			if (!nbt.hasKey("Counted"))
			{
				nbt.setBoolean("Counted", true);
				loop.setItemStack(nbt.getItem());
				int amount = loop.getItemStack().getAmount();
				Material material = loop.getItemStack().getType();
				for (Items.Item itemloop : Items.Item.values())
				{
					if (material == Items.MATERIAL[itemloop.ordinal()])
						Items.Count[itemloop.ordinal()] += amount;
				}
			}
		}
		return;
	}

	@EventHandler
	public void clickItem(InventoryClickEvent e)
	{
		if (e.getCurrentItem() == null)
		{
			return;
		}
		Player player = (Player)e.getWhoClicked();
		Material material = e.getCurrentItem().getType();
		int playerpos = PlayerInfo.getPlayer(player);
		switch (e.getView().getTitle())
		{
			case "Architecture":
				if (e.getClick().isRightClick() && e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getTopInventory()))
				{
					long result;
					if (e.getClick().isShiftClick()) result = PlayerInfo.purchaseItem(player, material, true);
					else result = PlayerInfo.purchaseItem(player, material, false);
					if (result == -1) player.sendMessage("돈이 부족합니다.");
					else player.sendMessage(Long.toString(result) + " 원에 구매 완료.");
					Main.playerinfos.get(playerpos).setInfo();
				}
			case "Livestock":
			case "Mining":
			case "Farming":
			case "Fishing":
			case "Exploring":
				if (e.getClick().isLeftClick() && e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getTopInventory()))
				{
					long result;
					if (e.getClick().isShiftClick()) result = PlayerInfo.sellItem(player, material, true);
					else result = PlayerInfo.sellItem(player, material, false);
					if (result == -1) player.sendMessage("아이템이 부족합니다.");
					else player.sendMessage(Long.toString(result) + " 원에 판매 완료.");
					Main.playerinfos.get(playerpos).setInfo();
				}
				PlayerInfo.updateScoreboard(player);
				e.setCancelled(true);
				break;
			case "Stock":
				if (e.getSlot() == 4)
				{
					e.setCancelled(true);
					break;
				}
				long result;
				if (e.getClick().isLeftClick() && e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getTopInventory()))
				{
					if (e.getClick().isShiftClick()) result = PlayerInfo.sellStock(player, material, true);
					else result = PlayerInfo.sellStock(player, material, false);
					if (result == -1) player.sendMessage("주식이 부족합니다.");
					else player.sendMessage(Long.toString(result) + " 원에 매도 완료.");
					Main.playerinfos.get(playerpos).setInfo();
				}
				if (e.getClick().isRightClick() && e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getTopInventory()))
				{
					if (e.getClick().isShiftClick()) result = PlayerInfo.purchaseStock(player, material, true);
					else result = PlayerInfo.purchaseStock(player, material, false);
					if (result == -1) player.sendMessage("돈이 부족합니다.");
					else player.sendMessage(Long.toString(result) + " 원에 매수 완료.");
					Main.playerinfos.get(playerpos).setInfo();
				}
				PlayerInfo.updateScoreboard(player);
				PlayerInfo.updateStock(player);
				e.setCancelled(true);
				break;
		}
		return;
	}

	@EventHandler
	public void entityInteract(PlayerInteractEntityEvent e)
	{
		if (e.getHand() == EquipmentSlot.HAND) return;
		switch (e.getRightClicked().getName())
		{
			case "Livestock":
				e.getPlayer().openInventory(Merchant.LIVESTOCK);
				break;
			case "Mining":
				e.getPlayer().openInventory(Merchant.MINING);
				break;
			case "Farming":
				e.getPlayer().openInventory(Merchant.FARMING);
				break;
			case "Fishing":
				e.getPlayer().openInventory(Merchant.FISHING);
				break;
			case "Architecture":
				e.getPlayer().openInventory(Merchant.ARCHITECTURE);
				break;
			case "Exploring":
				e.getPlayer().openInventory(Merchant.EXPLORING);
				break;
			case "Information":
				e.getPlayer().sendMessage("Enocraft에 오신 것을 환영합니다.");
				e.getPlayer().sendMessage("/shop 명령어를 통해 상점으로 빠르게 이동하세요.");
				e.getPlayer().sendMessage("/stock 명령어를 통해 주식을 이용해 보세요.");
				break;
		}
		return;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Stock.DeathCount++;
	}

	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e)
	{
		if (e.getView().getTitle().equals("Stock"))
			Bukkit.getScheduler().cancelTask(Main.playerinfos.get(PlayerInfo.getPlayer((Player)e.getPlayer())).taskid);
	}
}
