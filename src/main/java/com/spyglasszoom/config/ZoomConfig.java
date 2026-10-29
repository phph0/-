package com.spyglasszoom.config;

public class ZoomConfig {

    public float zoomLevel = 4.0f;
    public float zoomSpeed = 0.4f;
    public boolean showOverlay = true;

    public ZoomConfig copy() {
        ZoomConfig c = new ZoomConfig();
        c.zoomLevel = this.zoomLevel;
        c.zoomSpeed = this.zoomSpeed;
        c.showOverlay = this.showOverlay;
        return c;
    }
}
