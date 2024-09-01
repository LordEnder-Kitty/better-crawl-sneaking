package net.enderkitty.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value = EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Inject(method = "isSpaceAroundPlayerEmpty", at = @At(value = "HEAD"), cancellable = true)
    private void isSpaceAroundPlayerEmpty(double offsetX, double offsetZ, float f, CallbackInfoReturnable<Boolean> cir) {
        Box box = this.getBoundingBox();
        if (this.isCrawling()) {
            cir.setReturnValue(this.getWorld().isSpaceEmpty(this, 
                    new Box(box.minX + offsetX, box.maxY, box.minZ + offsetZ, box.maxX + offsetX, box.maxY + 0.5, box.maxZ + offsetZ)));
        } else {
            cir.setReturnValue(this.getWorld().isSpaceEmpty(this, 
                    new Box(box.minX + offsetX, box.minY - (double) f - 1.0E-5F, box.minZ + offsetZ, box.maxX + offsetX, box.minY, box.maxZ + offsetZ)));
        }
    }
}
