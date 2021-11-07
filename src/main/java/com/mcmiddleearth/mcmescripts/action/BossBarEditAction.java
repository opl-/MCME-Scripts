package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.mcmescripts.trigger.TriggerContext;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class BossBarEditAction extends Action {

    private final NamespacedKey barKey;
    private final String title;
    private final BarColor color;
    private final BarStyle style;
    private final Boolean fog;
    private final Boolean dark;
    private final Boolean music;
    private final Double progress;
    private final Boolean visible;

    public BossBarEditAction(NamespacedKey barKey, String title, BarColor color, BarStyle style, Boolean fog, Boolean dark, Boolean music,
                             Double progress, Boolean visible) {
        this.barKey = barKey;
        this.title = title;
        this.color = color;
        this.style = style;
        this.fog = fog;
        this.dark = dark;
        this.music = music;
        this.progress = progress;
        this.visible = visible;
    }

    @Override
    protected void handler(TriggerContext context) {
        BossBar bar = Bukkit.getBossBar(barKey);
        if (bar == null) {
            bar = Bukkit.createBossBar(barKey, "", BarColor.RED, BarStyle.SOLID);
        }
        editBar(bar, title, style, color, fog, dark, music, progress, visible);
    }

    public static void editBar(BossBar bar, String title, BarStyle style, BarColor color, Boolean fog, Boolean dark, Boolean music,
                               Double progress, Boolean visible) {
        if(title!=null) bar.setTitle(title);
        if(style!=null) bar.setStyle(style);
        if(color!=null) bar.setColor(color);
        if(visible!=null) bar.setVisible(visible);
        if(fog!=null) {
            if (fog) bar.addFlag(BarFlag.CREATE_FOG);
            else bar.removeFlag(BarFlag.CREATE_FOG);
        }
        if(dark!=null) {
            if (dark) bar.addFlag(BarFlag.DARKEN_SKY);
            else bar.removeFlag(BarFlag.DARKEN_SKY);
        }
        if(music!=null) {
            if (music) bar.addFlag(BarFlag.PLAY_BOSS_MUSIC);
            else bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
        }
        if(progress!=null) bar.setProgress(progress);
    }
}
