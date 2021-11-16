package com.kimit.enocraft;

import de.tr7zw.nbtapi.NBTItem;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Event implements Listener
{
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerInfo player = new PlayerInfo(e.getPlayer().getUniqueId());
		Main.playerinfos.add(player);
		BPlayerBoard board = Netherboard.instance().createBoard(e.getPlayer(), "플레이어 정보");
		board.setAll("닉네임 : " + e.getPlayer().getName(), "돈 : " + Long.toString(player.getMoney()) + " 원");
		if (!e.getPlayer().hasPlayedBefore())
			e.getPlayer().getInventory().addItem(new ItemStack(Material.SWEET_BERRIES, 64));
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
			Material material = loop.getItemStack().getType();
			if (!Items.isInThere(Items.BLOCKS, material))
				break;
			NBTItem nbt = new NBTItem(loop.getItemStack());
			if (!nbt.hasKey("Counted"))
			{
				nbt.setBoolean("Counted", true);
				loop.setItemStack(nbt.getItem());
				Items.Count[Items.getItempos(material)] += loop.getItemStack().getAmount();
			}
		}
		return;
	}

	@EventHandler
	public void pickUpItem(EntityPickupItemEvent e)
	{
		if (e.getEntity() instanceof Player)
		{
			Material material = e.getItem().getItemStack().getType();
			if (!Items.isInThere(Items.PICKUP, material))
				return;
			NBTItem nbt = new NBTItem(e.getItem().getItemStack());
			if (!nbt.hasKey("Counted"))
			{
				nbt.setBoolean("Counted", true);
				e.getItem().setItemStack(nbt.getItem());
				Items.Count[Items.getItempos(material)] += e.getItem().getItemStack().getAmount();
			}
		}
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
		long result = 0;
		switch (e.getView().getTitle())
		{
			case "Architecture":
				if (e.getClick().isRightClick() && e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getTopInventory()))
				{
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
			case "Market":
				if (e.getClick().isLeftClick() && e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getTopInventory()))
				{
					result = Market.purchase(player, e.getSlot());
					if (result == -1) player.sendMessage("돈이 부족합니다.");
					else
					{
						player.sendMessage(Long.toString(result) + " 원에 구매 완료.");
						player.sendMessage("/market receive 명령어를 통해 구매한 물품을 가져가세요.");
					}
				}
				e.setCancelled(true);
				break;
			case "Receive":
				if (e.getClickedInventory().equals(e.getView().getBottomInventory()))
					e.setCancelled(true);
				break;
			default:
				switch (e.getClickedInventory().getType())
				{
					case FURNACE:
					case BLAST_FURNACE:
					case CRAFTING:
					case WORKBENCH:
						if (!Items.isInThere(Items.CRAFT, material))
							return;
						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								ItemStack[] contents = e.getView().getBottomInventory().getContents();
								for (int loop = 0; loop != contents.length; loop++)
								{
									if (contents[loop] == null)
										continue;
									if (contents[loop].getType() == material)
									{
										NBTItem nbt = new NBTItem(contents[loop]);
										if (!nbt.hasKey("Counted"))
										{
											nbt.setBoolean("Counted", true);
											contents[loop] = nbt.getItem();
											Items.Count[Items.getItempos(material)] += contents[loop].getAmount();
										}
									}
								}
								e.getView().getBottomInventory().setContents(contents);
							}
						}.runTaskLater(Bukkit.getPluginManager().getPlugin("Enocraft-plugin"), 5);
				}
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
		switch (e.getView().getTitle())
		{
			case "Stock":
				Bukkit.getScheduler().cancelTask(Main.playerinfos.get(PlayerInfo.getPlayer((Player)e.getPlayer())).taskid);
				break;
			case "Send":
				int count = 0;
				for (var loop : e.getView().getTopInventory().getContents())
				{
					if (loop != null && loop.getType() != Material.AIR)
						count++;
				}
				if (count == 0)
				{
					e.getPlayer().sendMessage("판매할 아이템이 없습니다.");
					break;
				}
				Main.playerinfos.get(PlayerInfo.getPlayer((Player)e.getPlayer())).marketsendopen = true;
				e.getPlayer().sendMessage("판매 가격을 입력해 주세요.");
				break;
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		int playerpos = PlayerInfo.getPlayer(e.getPlayer());
		if (Main.playerinfos.get(playerpos).marketsendopen)
		{
			if (!StringUtils.isNumeric(e.getMessage()) || Long.parseLong(e.getMessage()) < 0)
			{
				e.getPlayer().sendMessage("0 이상의 숫자로 입력해 주세요.");
				e.setCancelled(true);
				return;
			}
			long price = Long.parseLong(e.getMessage());
			long fee = (long)Math.ceil((double)price / 10.0);
			if (fee > Main.playerinfos.get(playerpos).getMoney())
			{
				e.getPlayer().sendMessage("수수료를 낼 돈이 없습니다.");
				e.setCancelled(true);
				return;
			}
			Market.send(e.getPlayer(), price);
			e.setCancelled(true);
		}
	}
}
