package net.niekie3202.farmersfeet.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmBlockMixin {
    @Unique
    private static final ResourceKey<Enchantment> FARMERS_FEET = ResourceKey.create(
            Registries.ENCHANTMENT,
            Identifier.fromNamespaceAndPath("farmers-feet", "farmersfeet"));

    @Inject(method = "turnToDirt", at = @At("HEAD"), cancellable = true)
    private static void cropguard$protect(@Nullable Entity sourceEntity, BlockState state,
                                          Level level, BlockPos pos, CallbackInfo ci) {
        if (sourceEntity instanceof LivingEntity living) {
            ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);
            var holder = level.registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .getOrThrow(FARMERS_FEET);
            if (EnchantmentHelper.getItemEnchantmentLevel(holder, boots) > 0) {
                if (living instanceof ServerPlayer player && level.getServer() != null) {
                    var advancement = level.getServer().getAdvancements()
                            .get(Identifier.fromNamespaceAndPath("farmers-feet", "saved_farmland"));
                    if (advancement != null) {
                        player.getAdvancements().award(advancement, "saved_farmland");
                    }
                }
                ci.cancel();
            }
        }
    }
}
