package net.niekie3202.farmersfeet;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FarmersFeet implements ModInitializer {
	public static final String MOD_ID = "FarmersFeet";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final ResourceKey<LootTable> BOOK_TABLE = ResourceKey.create(
			Registries.LOOT_TABLE,
			Identifier.fromNamespaceAndPath("farmers-feet", "chests/farmers_feet_book"));

	@Override
	public void onInitialize() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (key.identifier().getNamespace().equals("minecraft")
					&& key.identifier().getPath().startsWith("chests/village/")) {

				tableBuilder.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.05f))
						.add(NestedLootTable.lootTableReference(BOOK_TABLE)));
			}
		});
	}
}
