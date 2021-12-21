package com.kimit.enocraft;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.logging.Logger;

public class Schedule
{
	public static final Runnable UPDATE = new Runnable()
	{
		@Override
		public void run()
		{
			final long[] PrevPrice = Items.Price.clone();
			final int players = Bukkit.getOnlinePlayers().size();
			for (Items.Item loop : Items.Item.values())
			{
				if (Items.PrevCount[loop.ordinal()] > Items.Count[loop.ordinal()])
					Items.Price[loop.ordinal()] = (long)Math.ceil((double)Items.WEIGHT[loop.ordinal()] * ((double)Main.MERCHANTINCREASE * Main.MERCHANTETC * Math.log10(players + 1) + 1.2));
				else if (Items.PrevCount[loop.ordinal()] < Items.Count[loop.ordinal()])
					Items.Price[loop.ordinal()] = (long)Math.ceil((double)Items.WEIGHT[loop.ordinal()] * (double)Main.MERCHANTDECREASE / (Main.MERCHANTDECREASE + (double)(Items.Count[loop.ordinal()] * Items.WEIGHT[loop.ordinal()])));
			}
			for (int loop = Items.Item.SPIDER_EYE.ordinal(); loop != Items.Item.END.ordinal(); loop++)
			{
				if (Items.PrevCount[loop] > Items.Count[loop])
					Items.Price[loop] = (long)Math.ceil((double)Items.WEIGHT[loop] * ((double)Main.MERCHANTINCREASE * Main.MERCHANTEXPLORING * Math.log10(players + 1) + 1.2));
			}
			for (int loop = Items.Item.DIAMOND.ordinal(); loop != Items.Item.WHEAT.ordinal(); loop++)
			{
				if (Items.PrevCount[loop] > Items.Count[loop])
					Items.Price[loop] = (long)Math.ceil((double)Items.WEIGHT[loop] * ((double)Main.MERCHANTINCREASE * Main.MERCHANTMINING * Math.log10(players + 1) + 1.2));
			}
			Items.PrevCount = Items.Count.clone();
			Arrays.fill(Items.Count, 0);

			Merchant.update(Merchant.LIVESTOCK, Items.Item.BEEF.ordinal(), Items.Item.DIAMOND.ordinal(), false);
			Merchant.update(Merchant.MINING, Items.Item.DIAMOND.ordinal(), Items.Item.WHEAT.ordinal(), false);
			Merchant.update(Merchant.FARMING, Items.Item.WHEAT.ordinal(), Items.Item.EMERALD.ordinal(), false);
			Merchant.update(Merchant.FISHING, Items.Item.COD.ordinal(), Items.Item.QUARTZ_BLOCK.ordinal(), false);
			Merchant.update(Merchant.ARCHITECTURE, Items.Item.QUARTZ_BLOCK.ordinal(), Items.Item.SPIDER_EYE.ordinal(), true);
			Merchant.update(Merchant.EXPLORING, Items.Item.SPIDER_EYE.ordinal(), Items.Item.TRIDENT.ordinal(), false);

			Arrays.fill(Items.Count, 0);
			Stock.DeathCount = 0;
			Logger logger = Bukkit.getLogger();
			logger.info(Main.PREFIX + "아이템 카운트가 초기화되었습니다.");
		}
	};

	public static final Runnable STOCK = new Runnable()
	{
		@Override
		public void run()
		{
			Stock.delisting();
			Stock.update();
		}
	};
}
