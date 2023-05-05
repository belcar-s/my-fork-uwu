package me.flashyreese.mods.sodiumextra.client;

import net.minecraft.util.MetricsData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientTickHandler {
    private int averageFps, lowestFps, highestFps;

    public void onClientInitialize() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraftClient -> {
            MetricsData metricsData = minecraftClient.getMetricsData();
            long[] ls = metricsData.getSamples();

            int width = minecraftClient.getWindow().getScaledWidth() / 2;

            int startIndex = metricsData.getStartIndex();
            int considered = Math.min(ls.length, width);

            int skipped = ls.length - considered;

            int sum = 0;
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (int i = 0; i < considered; ++i) {
                int sample = (int)(ls[metricsData.wrapIndex(startIndex + skipped + i)] / 1_000_000L);
                min = Math.min(min, sample);
                max = Math.max(max, sample);
                sum += sample;
            }

            this.lowestFps = (int) (max == 0 ? 0 : 1000 / (float)(max));
            this.highestFps = (int) (min == 0 ? 0 : 1000 / (float)(min));
            this.averageFps = (int) (sum == 0 ? 0 : 1000 / (sum / (float)(considered)));
        });
    }

    public int getAverageFps() {
        return this.averageFps;
    }

    public int getLowestFps() {
        return this.lowestFps;
    }

    public int getHighestFps() {
        return this.highestFps;
    }
}
