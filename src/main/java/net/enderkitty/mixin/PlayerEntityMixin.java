package net.enderkitty.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    public PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }
    
    @Inject(method = "canFallAtLeast", at = @At(value = "RETURN"), cancellable = true)
    private void isSpaceAroundPlayerEmpty(double deltaX, double deltaZ, double minHeight, CallbackInfoReturnable<Boolean> cir) {
        AABB box = this.getBoundingBox();
        
        cir.setReturnValue(this.isVisuallyCrawling() ? cir.getReturnValue() || this.level().noCollision(
                this, new AABB(box.minX + deltaX, box.maxY, box.minZ + deltaZ, box.maxX + deltaX, box.maxY + 0.9, box.maxZ + deltaZ)
        ) : cir.getReturnValue());
    }
}
