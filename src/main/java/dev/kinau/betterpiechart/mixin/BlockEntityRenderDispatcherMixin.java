package dev.kinau.betterpiechart.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {

    @Inject(at = @At("INVOKE"), method = "render")
    private <E extends BlockEntity> void renderBlockEntityStart(E blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
        Level level = ((BlockEntityRenderDispatcher)(Object)this).level;
        level.getProfiler().push(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType()).getPath());
    }

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER), method = "render")
    private <E extends BlockEntity> void renderBlockEntityEnd(E blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
        Level level = ((BlockEntityRenderDispatcher)(Object)this).level;
        level.getProfiler().pop();
    }
}
