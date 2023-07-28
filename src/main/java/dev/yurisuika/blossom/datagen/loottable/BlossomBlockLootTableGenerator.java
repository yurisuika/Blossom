package dev.yurisuika.blossom.datagen.loottable;

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
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.CopyStateFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;

public class BlossomBlockLootTableGenerator extends FabricBlockLootTableProvider {

    public static final LootCondition.Builder WITH_SILK_TOUCH = MatchToolLootCondition.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1))));
    public static final LootCondition.Builder WITH_SHEARS = MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(Items.SHEARS));

    public BlossomBlockLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(Blossom.FLOWERING_OAK_LEAVES, (Block block) -> blossomingOakLeavesDrops(block, Blocks.OAK_SAPLING, BlockStatePropertyLootCondition.builder(Blossom.FLOWERING_OAK_LEAVES).properties(StatePredicate.Builder.create().exactMatch(FloweringLeavesBlock.AGE, 7)), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        addDrop(Blocks.OAK_LEAVES, (Block block) -> leavesDrops(block, Blocks.OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F));
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
                        .conditionally(WITH_SHEARS.invert().or(WITH_SILK_TOUCH.invert()))
                        .with(applyExplosionDecay(Blocks.OAK_LEAVES, ItemEntry.builder(Items.STICK)
                                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F)))
                                )
                                .conditionally(TableBonusLootCondition.builder(Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))
                        )
                )
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(WITH_SILK_TOUCH.invert())
                        .conditionally(condition)
                        .with(addSurvivesExplosionCondition(leaves, ItemEntry.builder(Items.APPLE)
                                .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286F, 3)))
                        )
                );
    }

}