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
			for (Items.Item loop : Items.Item.values())
			{
				if (Items.PrevCount[loop.ordinal()] > Items.Count[loop.ordinal()])
					Items.Price[loop.ordinal()] = (long)Math.ceil(1.06 * (double)Items.Price[loop.ordinal()]);
				else if (Items.PrevCount[loop.ordinal()] < Items.Count[loop.ordinal()])
					Items.Price[loop.ordinal()] = (long)Math.floor(Items.Price[loop.ordinal()] * (double)240000 / (double)(240000 + (Items.Count[loop.ordinal()] * Items.WEIGHT[loop.ordinal()])));
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
