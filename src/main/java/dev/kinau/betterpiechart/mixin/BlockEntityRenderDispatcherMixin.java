package dev.kinau.betterpiechart.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.kinau.betterpiechart.blockEntity.BlockEntityTracker;
import dev.kinau.betterpiechart.utils.BlockEntityUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public <E extends BlockEntity> void renderBlockEntityStart(E blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo ci) {
        BlockEntityTracker.getInstance().add(blockEntity);
        ProfilerFiller profiler = Profiler.get();
        BlockEntityUtils.getTag(blockEntity).ifPresent(profiler::push);
        profiler.push(BlockEntityUtils.getName(blockEntity));
    }

    @Inject(method = "render", at = @At(value = "RETURN"))
    private <E extends BlockEntity> void renderBlockEntityEnd(E blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
        ProfilerFiller profiler = Profiler.get();
        BlockEntityUtils.getTag(blockEntity).ifPresent(s -> profiler.pop());
        profiler.pop();
    }
}
