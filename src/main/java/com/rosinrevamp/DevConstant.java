package com.rosinrevamp;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.SharedConstants;
public class DevConstant implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        // brigadier runs before mod initializer so we need to set this here
        SharedConstants.isDevelopment = true;
    }
}
