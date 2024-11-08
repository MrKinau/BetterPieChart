package dev.kinau.betterpiechart.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.kinau.betterpiechart.blockEntity.BlockEntityTracker;
import dev.kinau.betterpiechart.expander.LevelRendererExpander;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements LevelRendererExpander {

	@Shadow @Final private List<Entity> visibleEntities;

	private Map<String, Long> lastVisibleEntities;

	@Inject(at = @At("HEAD"), method = "renderEntity")
	private void renderEntityStart(Entity entity, double d, double e, double f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
		Profiler.get().push(entity.getType().toShortString());
		Profiler.get().incrementCounter(entity.getType().toShortString());
	}

	@Inject(at = @At(value = "TAIL"), method = "renderEntity")
	private void renderEntityEnd(Entity entity, double d, double e, double f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
		Profiler.get().pop();
	}

	@Inject(method = "collectVisibleEntities", at = @At("RETURN"))
	private void collectVisibleEntities(Camera camera, Frustum frustum, List<Entity> list, CallbackInfoReturnable<Boolean> cir) {
		BlockEntityTracker.getInstance().clear();
		this.lastVisibleEntities = visibleEntities.stream().collect(Collectors.groupingBy(entity -> entity.getType().toShortString(), Collectors.counting()))
				.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}

	@Override
	public Map<String, Long> betterPieChart$getLastVisibleEntityCounts() {
		return lastVisibleEntities;
	}
}