package com.kimit.enocraft;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
			switch (s)
			{
				case "setshop":
					Location setshop = player.getLocation();
					location.set("shop", setshop);
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
					Location getshop = location.getLocation("shop");
					if (getshop != null)
					{
						player.teleport(getshop);
						commandSender.sendMessage("Teleporting...");
					}
					break;
				case "stock":
					commandSender.sendMessage("Open stock.");
					int playerpos = PlayerInfo.getPlayer(player);
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
			}
		}
		return true;
	}
}
