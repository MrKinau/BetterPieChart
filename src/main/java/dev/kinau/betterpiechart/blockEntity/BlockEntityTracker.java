package dev.kinau.betterpiechart.blockEntity;

import dev.kinau.betterpiechart.utils.BlockEntityUtils;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BlockEntityTracker {

    private static final BlockEntityTracker INSTANCE = new BlockEntityTracker();

    private final Map<String, Long> counts = new HashMap<>();

    public static BlockEntityTracker getInstance() {
        return INSTANCE;
    }

    public void add(BlockEntity blockEntity) {
        synchronized (counts) {
            addCount(blockEntity);
        }
    }

    private void addCount(BlockEntity blockEntity) {
        counts.merge(BlockEntityUtils.getName(blockEntity), 1L, Long::sum);
        Optional<String> optTag = BlockEntityUtils.getTag(blockEntity);
        optTag.ifPresent(tag -> counts.merge(tag, 1L, Long::sum));
    }

    public void clear() {
        synchronized (counts) {
            counts.clear();
        }
    }

    public Map<String, Long> counts() {
        synchronized (counts) {
            return counts;
        }
    }
}
