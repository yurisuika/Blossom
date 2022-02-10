package com.yurisuika.blossom;

import com.yurisuika.blossom.registry.BlossomRegistry;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlossomClient implements ClientModInitializer {

    public static final String MOD_ID = "blossom";

    public static final Logger LOGGER = LogManager.getLogger("Blossom");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Loading Blossom Client!");

        BlossomRegistry.registerRenderLayers();
        BlossomRegistry.registerColorProviders();
    }

}
