package dev.kinau.betterpiechart.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Inject(at = @At("INVOKE"), method = "renderEntity")
	private void renderEntityStart(Entity entity, double d, double e, double f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
		try (Level level = entity.level()) {
			level.getProfiler().push(entity.getType().toShortString());
		} catch (IOException ignore) {}
	}

	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER), method = "renderEntity")
	private void renderEntityEnd(Entity entity, double d, double e, double f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
		try (Level level = entity.level()) {
			level.getProfiler().pop();
		} catch (IOException ignore) {}
	}
}