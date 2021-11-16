package com.kimit.enocraft;

import org.bukkit.Material;

public class Items
{
	public static long[] Count = new long[Item.values().length];
	public static long[] PrevCount = new long[Item.values().length];
	public static long[] Price = new long[Item.values().length];

	public static final long[] WEIGHT =
			{
					64 * 80,
					64 * 80,
					64 * 80,
					64 * 80,
					64 * 80,

					8192,
					384,
					384,
					3072,
					32,
					32,
					64,
					60152,

					192,
					64,
					64,
					64,
					32,
					144,
					16,
					24,
					188,
					256,
					128,
					256,
					64,

					2043,

					64 * 50,
					64 * 50,
					64 * 50,
					64 * 50,

					128 * 20,
					//16 * 20
					16 * 20,
					32 * 20,
					64 * 20,
					34 * 20,
					//32 * 20,
					32 * 20,

					128,
					32,
					144,
					192,
					524,
					800,
					32,
					329264,
					2042,
					7025,
					16024,
					35489,
					320215,
					24508,
					14068,
					2092,
					4096,
					9206,
					86463,
					0
			};

	public static final Material[] MATERIAL =
			{
					Material.BEEF, // Drop by killing
					Material.RABBIT,
					Material.MUTTON,
					Material.PORKCHOP,
					Material.CHICKEN,

					Material.DIAMOND, // Drop by blocks
					Material.COPPER_INGOT,
					Material.IRON_INGOT,
					Material.GOLD_INGOT,
					Material.REDSTONE,
					Material.COAL,
					Material.LAPIS_LAZULI,
					Material.NETHERITE_INGOT, // On craft

					Material.WHEAT, // Drop by blocks
					Material.POTATO,
					Material.CARROT,
					Material.BEETROOT,
					Material.SUGAR_CANE, // Pick up
					Material.PUMPKIN,
					Material.MELON_SLICE,
					Material.NETHER_WART,
					Material.HONEY_BLOCK,
					Material.BEE_NEST, // On craft, drop by blocks
					Material.SWEET_BERRIES, // Pick up
					Material.APPLE, // Pick up
					Material.COCOA_BEANS, // Drop by blocks
					
					Material.EMERALD, // Drop by blocks

					Material.COD, // Drop by fishing
					Material.SALMON,
					Material.TROPICAL_FISH,
					Material.PUFFERFISH,

					Material.QUARTZ_BLOCK, // On craft
					// Concrete
					Material.CLAY_BALL, // Drop by blocks
					Material.GLASS, // On craft
					Material.BRICKS,
					Material.SCAFFOLDING, // On craft
					// Log
					Material.STONE_BRICKS, // Drop by blocks

					Material.SPIDER_EYE, // Drop by killing
					Material.ROTTEN_FLESH,
					Material.BONE,
					Material.GUNPOWDER,
					Material.ENDER_PEARL,
					Material.MAGMA_CREAM,
					Material.SLIME_BALL,
					Material.NETHER_STAR,
					Material.BLAZE_ROD,
					Material.SHULKER_SHELL,
					Material.NAUTILUS_SHELL, // Pick up, treasure
					Material.WITHER_SKELETON_SKULL, // Drop by killing
					Material.DRAGON_HEAD, // Drop by blocks
					Material.END_CRYSTAL, // On craft
					Material.TOTEM_OF_UNDYING, // Pick up, treasure
					// Banner of Villager
					Material.GHAST_TEAR, // Drop by killing
					Material.PHANTOM_MEMBRANE, // Drop by killing
					Material.TRIDENT, // Drop by killing
					Material.AIR
			};

	public static final Material[] BLOCKS =
			{
					Material.DIAMOND, // Drop by blocks
					Material.RAW_COPPER,
					Material.RAW_IRON,
					Material.RAW_GOLD,
					Material.REDSTONE,
					Material.COAL,
					Material.LAPIS_LAZULI,

					Material.WHEAT, // Drop by blocks
					Material.POTATO,
					Material.CARROT,
					Material.BEETROOT,
					Material.PUMPKIN,
					Material.MELON_SLICE,
					Material.NETHER_WART,
					Material.HONEY_BLOCK,
					Material.BEE_NEST, // On craft, drop by blocks
					Material.COCOA_BEANS, // Drop by blocks

					Material.EMERALD, // Drop by blocks

					Material.CLAY_BALL, // Drop by blocks
					Material.STONE_BRICKS, // Drop by blocks

					Material.DRAGON_HEAD // Drop by blocks
			};

	public static final Material[] PICKUP =
			{
					Material.BEEF, // Drop by killing
					Material.RABBIT,
					Material.MUTTON,
					Material.PORKCHOP,
					Material.CHICKEN,

					Material.NETHERITE_INGOT, // On craft

					Material.SUGAR_CANE, // Pick up
					Material.SWEET_BERRIES, // Pick up
					Material.APPLE, // Pick up
					Material.BEE_NEST, // On craft, drop by blocks

					Material.COD, // Drop by fishing
					Material.SALMON,
					Material.TROPICAL_FISH,
					Material.PUFFERFISH,

					Material.QUARTZ_BLOCK, // On craft
					Material.GLASS, // On craft
					Material.BRICKS,
					Material.SCAFFOLDING, // On craft

					Material.SPIDER_EYE, // Drop by killing
					Material.ROTTEN_FLESH,
					Material.BONE,
					Material.GUNPOWDER,
					Material.ENDER_PEARL,
					Material.MAGMA_CREAM,
					Material.SLIME_BALL,
					Material.NETHER_STAR,
					Material.BLAZE_ROD,
					Material.SHULKER_SHELL,
					Material.NAUTILUS_SHELL, // Pick up, treasure
					Material.WITHER_SKELETON_SKULL, // Drop by killing
					Material.TOTEM_OF_UNDYING, // Pick up, treasure
					Material.GHAST_TEAR, // Drop by killing
					Material.PHANTOM_MEMBRANE, // Drop by killing
					Material.TRIDENT // Drop by killing
			};

	public static final Material[] CRAFT =
			{
					Material.NETHERITE_INGOT, // On craft

					Material.BEE_NEST, // On craft, drop by blocks

					Material.QUARTZ_BLOCK, // On craft
					Material.GLASS, // On craft
					Material.BRICKS,
					Material.STONE_BRICKS,
					Material.SCAFFOLDING // On craft
			};

	public static enum Item
	{
		BEEF,
		RABBIT,
		MUTTON,
		PORKCHOP,
		CHICKEN,

		DIAMOND,
		COPPER_INGOT,
		IRON_INGOT,
		GOLD_INGOT,
		REDSTONE,
		COAL,
		LAPIS_LAZULI,
		NETHERITE_INGOT,

		WHEAT,
		POTATO,
		CARROT,
		BEETROOT,
		SUGAR_CANE,
		PUMPKIN,
		MELON_SLICE,
		NETHER_WART,
		HONEY_BLOCK,
		BEE_NEST,
		SWEET_BERRIES,
		APPLE,
		COCOA_BEANS,

		EMERALD,

		COD,
		SALMON,
		TROPICAL_FISH,
		PUFFERFISH,

		QUARTZ_BLOCK,
		// Concrete
		CLAY_BALL,
		GLASS,
		BRICKS,
		SCAFFOLDING,
		// Log
		STONE_BRICKS,

		SPIDER_EYE,
		ROTTEN_FLESH,
		BONE,
		GUNPOWDER,
		ENDER_PEARL,
		MAGMA_CREAM,
		SLIME_BALL,
		NETHER_STAR,
		BLAZE_ROD,
		SHULKER_SHELL,
		NAUTILUS_SHELL,
		WITHER_SKELETON_SKULL,
		DRAGON_HEAD,
		END_CRYSTAL,
		TOTEM_OF_UNDYING,
		// Banner of Villager
		GHAST_TEAR,
		PHANTOM_MEMBRANE,
		TRIDENT,
		END
	}

	public static int getItempos(Material material)
	{
		Material temp = material;
		switch (material)
		{
			case RAW_GOLD:
				temp = Material.GOLD_INGOT;
				break;
			case RAW_IRON:
				temp = Material.IRON_INGOT;
				break;
			case RAW_COPPER:
				temp = Material.COPPER_INGOT;
				break;
		}
		for (Items.Item loop : Items.Item.values())
		{
			if (temp == Items.MATERIAL[loop.ordinal()])
				return loop.ordinal();
		}
		return -1;
	}

	public static boolean isInThere(Material[] items, Material material)
	{
		for (var loop : items)
		{
			if (material == loop)
				return true;
		}
		return false;
	}
}
