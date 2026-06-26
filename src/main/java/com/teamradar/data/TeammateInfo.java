package com.teamradar.data;

public class TeammateInfo {

    public String name;
    public float health;
    public float maxHealth;
    public double x;
    public double y;
    public double z;
    public String dimension;
    public boolean online;
    public boolean recentlyDied;

    public TeammateInfo(String name) {
        this.name = name;
        this.health = 20f;
        this.maxHealth = 20f;
        this.dimension = "minecraft:overworld";
        this.online = false;
        this.recentlyDied = false;
    }

    public float getHealthPercent() {
        return maxHealth > 0 ? (health / maxHealth) * 100f : 0f;
    }

    public int getHealthColor() {
        float pct = getHealthPercent();
        if (pct > 60f) return 0x55FF55;
        if (pct > 30f) return 0xFFAA00;
        return 0xFF4444;
    }

    public String getDimensionShort() {
        if (dimension.contains("nether")) return "NETHER";
        if (dimension.contains("end")) return "END";
        return "OW";
    }

    public double distanceTo(double px, double py, double pz) {
        double dx = x - px;
        double dz = z - pz;
        return Math.sqrt(dx * dx + dz * dz);
    }
}
