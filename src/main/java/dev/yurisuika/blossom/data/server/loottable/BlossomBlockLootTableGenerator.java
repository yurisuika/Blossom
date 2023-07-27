package dev.yurisuika.blossom.data.server.loottable;

import dev.yurisuika.blossom.Blossom;
import dev.yurisuika.blossom.block.FloweringLeavesBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.CopyStateFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;

public class BlossomBlockLootTableGenerator extends FabricBlockLootTableProvider {

    public BlossomBlockLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(Blossom.FLOWERING_OAK_LEAVES, (Block block) -> blossomingOakLeavesDrops(block, Blocks.OAK_SAPLING, BlockStatePropertyLootCondition.builder(Blossom.FLOWERING_OAK_LEAVES).properties(StatePredicate.Builder.create().exactMatch(FloweringLeavesBlock.AGE, 7)), SAPLING_DROP_CHANCE));
        addDrop(Blocks.OAK_LEAVES, (Block block) -> leavesDrops(block, Blocks.OAK_SAPLING, SAPLING_DROP_CHANCE));
    }

    public LootTable.Builder blossomingOakLeavesDrops(Block leaves, Block drop, LootCondition.Builder condition, float ... chance) {
        return LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(leaves)
                                .conditionally(WITH_SILK_TOUCH)
                                .apply(CopyStateFunction.builder(leaves)
                                        .addProperty(FloweringLeavesBlock.AGE)
                                )
                                .alternatively(ItemEntry.builder(Blocks.OAK_LEAVES)
                                        .conditionally(WITH_SHEARS.and(WITH_SILK_TOUCH.invert()))
                                )
                                .alternatively(ItemEntry.builder(drop)
                                        .conditionally(SurvivesExplosionLootCondition.builder())
                                        .conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, chance))
                                )
                        )
                )
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(WITHOUT_SILK_TOUCH_NOR_SHEARS)
                        .with(applyExplosionDecay(Blocks.OAK_LEAVES, ItemEntry.builder(Items.STICK)
                                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                                )
                                .conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, LEAVES_STICK_DROP_CHANCE))
                        )
                )
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(WITHOUT_SILK_TOUCH)
                        .conditionally(condition)
                        .with(addSurvivesExplosionCondition(leaves, ItemEntry.builder(Items.APPLE)
                                .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))
                        )
                );
    }

}
