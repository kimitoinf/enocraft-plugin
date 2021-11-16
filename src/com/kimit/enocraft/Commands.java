package com.kimit.enocraft;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class Commands implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		if (commandSender instanceof Player)
		{
			File locationfile = new File(Bukkit.getPluginManager().getPlugin("Enocraft-plugin").getDataFolder(), "/location.yml");
			FileConfiguration location = YamlConfiguration.loadConfiguration(locationfile);
			Player player = (Player)commandSender;
			int playerpos = PlayerInfo.getPlayer(player);
			switch (s)
			{
				case "setshop":
					Location setshop = player.getLocation();
					location.set("shop.world", setshop.getWorld().getName());
					location.set("shop.x", setshop.getX());
					location.set("shop.y", setshop.getY());
					location.set("shop.z", setshop.getZ());
					try
					{
						location.save(locationfile);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					commandSender.sendMessage("Setting shop location was completed.");
					commandSender.sendMessage(Double.toString(setshop.getX()) + ", " + Double.toString(setshop.getY()) + ", " + Double.toString(setshop.getZ()));
					break;
				case "shop":
					Location getshop = new Location(Bukkit.getWorld(location.getString("shop.world")), location.getDouble("shop.x"), location.getDouble("shop.y"), location.getDouble("shop.z"));
					if (getshop != null)
					{
						commandSender.sendMessage("Teleport in 5 seconds...");
						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								player.teleport(getshop);
							}
						}.runTaskLater(Bukkit.getPluginManager().getPlugin("Enocraft-plugin"), 20 * 5);
					}
					break;
				case "stock":
					commandSender.sendMessage("Open stock.");
					BukkitTask task = new BukkitRunnable()
					{
						@Override
						public void run()
						{
							PlayerInfo.updateStock(player);
						}
					}.runTaskTimer(Bukkit.getPluginManager().getPlugin("Enocraft-plugin"), 0, 20);
					player.openInventory(Main.playerinfos.get(PlayerInfo.getPlayer(player)).STOCK);
					Main.playerinfos.get(playerpos).taskid = task.getTaskId();
					break;
				case "dragonegg":
					if (!StringUtils.isNumeric(strings[0]))
						player.sendMessage("Invalid argument.");
					else
					{
						int pos = Integer.parseInt(strings[0]);
						if (pos >= 0 && pos <= 7)
						{
							Stock.Price[pos] *= 2;
							Stock.updateLore();
							player.sendMessage("Regulating stock price was completed.");
						}
						else
							player.sendMessage("Invalid argument.");
					}
					break;
				case "playerinfo":
					if (Bukkit.getPlayer(strings[0]) == null)
						break;
					int destplayerpos = PlayerInfo.getPlayer(Bukkit.getPlayer(strings[0]));
					commandSender.sendMessage("Money : " + Long.toString(Main.playerinfos.get(destplayerpos).getMoney()));
					for (int loop = 0; loop != Main.playerinfos.get(destplayerpos).getStocks().length; loop++)
						commandSender.sendMessage("Company " + Integer.toString(loop) + " : " + Long.toString(Main.playerinfos.get(destplayerpos).getStocks()[loop]));
					commandSender.sendMessage("Total invest : " + Long.toString(Main.playerinfos.get(destplayerpos).totalinvest));
					break;
				case "send":
					sendMoney(player, strings[0], strings[1]);
					break;
				case "market":
					if (strings.length < 1)
					{
						player.sendMessage("Invalid argument.");
						break;
					}
					switch (strings[0])
					{
						case "send":
							int count = 0;
							for (var loop : Market.MARKET.getContents())
							{
								if (loop != null && loop.getType() != Material.AIR)
									count++;
							}
							if (count >= Market.MARKET.getContents().length)
							{
								player.sendMessage("시장에 빈 공간이 없습니다. 나중에 다시 시도하세요.");
								break;
							}
							if (Main.playerinfos.get(playerpos).getMarket() >= 3)
							{
								player.sendMessage("시장에는 3회 이상 등록할 수 없습니다.");
								break;
							}
							player.sendMessage("판매할 아이템을 넣으세요.");
							player.openInventory(Main.playerinfos.get(playerpos).MARKETSEND);
							break;
						case "open":
							player.openInventory(Market.MARKET);
							break;
						case "receive":
							player.openInventory(Main.playerinfos.get(playerpos).MARKETRECEIVE);
							break;
						default:
							player.sendMessage("Invalid argument.");
							break;
					}
			}
		}
		return true;
	}

	private void sendMoney(Player sender, String receiver, String money)
	{
		if (Bukkit.getPlayer(receiver) == null)
		{
			sender.sendMessage("Invalid player.");
			return;
		}
		if (!StringUtils.isNumeric(money) || Long.parseLong(money) <= 0)
		{
			sender.sendMessage("Invalid argument.");
			return;
		}
		long temp = Long.parseLong(money);
		int senderpos = PlayerInfo.getPlayer(sender);
		int receiverpos = PlayerInfo.getPlayer(Bukkit.getPlayer(receiver));
		long sendermoney = Main.playerinfos.get(senderpos).getMoney();
		if (sendermoney < temp)
		{
			sender.sendMessage("잔액이 부족합니다.");
			return;
		}
		Main.playerinfos.get(senderpos).setMoney(sendermoney - temp);
		Main.playerinfos.get(receiverpos).setMoney(Main.playerinfos.get(receiverpos).getMoney() + temp);
		sender.sendMessage("플레이어 " + receiver + " 에게 " + money + " 원을 보냈습니다.");
		PlayerInfo.updateScoreboard(sender);
		PlayerInfo.updateScoreboard(Bukkit.getPlayer(receiver));
	}
}
