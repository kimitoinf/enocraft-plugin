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
					0,
					4096,
					9206,
					86463,
					0
			};

	public static final Material[] MATERIAL =
			{
					Material.BEEF,
					Material.RABBIT,
					Material.MUTTON,
					Material.PORKCHOP,
					Material.CHICKEN,

					Material.DIAMOND,
					Material.IRON_INGOT,
					Material.GOLD_INGOT,
					Material.REDSTONE,
					Material.COAL,
					Material.LAPIS_LAZULI,
					Material.NETHERITE_INGOT,

					Material.WHEAT,
					Material.POTATO,
					Material.CARROT,
					Material.BEETROOT,
					Material.SUGAR_CANE,
					Material.PUMPKIN,
					Material.MELON_SLICE,
					Material.NETHER_WART,
					Material.HONEY_BLOCK,
					Material.BEE_NEST,
					Material.SWEET_BERRIES,
					Material.APPLE,
					Material.COCOA_BEANS,
					
					Material.EMERALD,

					Material.COD,
					Material.SALMON,
					Material.TROPICAL_FISH,
					Material.PUFFERFISH,

					Material.QUARTZ_BLOCK,
					// Concrete
					Material.CLAY_BALL,
					Material.GLASS,
					Material.BRICKS,
					Material.SCAFFOLDING,
					// Log
					Material.STONE_BRICKS,

					Material.SPIDER_EYE,
					Material.ROTTEN_FLESH,
					Material.BONE,
					Material.GUNPOWDER,
					Material.ENDER_PEARL,
					Material.MAGMA_CREAM,
					Material.SLIME_BALL,
					Material.NETHER_STAR,
					Material.BLAZE_ROD,
					Material.SHULKER_SHELL,
					Material.NAUTILUS_SHELL,
					Material.WITHER_SKELETON_SKULL,
					Material.DRAGON_HEAD,
					Material.END_CRYSTAL,
					Material.TOTEM_OF_UNDYING,
					// Banner of Villager
					Material.GHAST_TEAR,
					Material.PHANTOM_MEMBRANE,
					Material.TRIDENT,
					Material.AIR
			};

	public static enum Item
	{
		BEEF,
		RABBIT,
		MUTTON,
		PORKCHOP,
		CHICKEN,

		DIAMOND,
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
}
