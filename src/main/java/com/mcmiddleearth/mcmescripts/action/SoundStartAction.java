package com.mcmiddleearth.mcmescripts.action;

import com.craftmend.openaudiomc.api.interfaces.AudioApi;
import com.craftmend.openaudiomc.generic.media.objects.MediaOptions;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SoundStartAction extends SelectingAction<Player> {

    private static String url = "http://media.mcmiddleearth.com/media/sounds/";

    public SoundStartAction(Selector<Player> selector, String musicFile, String musicId) {
        super(selector, (player, context) -> {
            //DebugManager.verbose(Modules.Action.execute(SoundStartAction.class),"MusicFile: "+musicFile+" musicId: "+musicId);
            if(SoundStartAction.hasOpenAudio()) {
                AudioApi audioApi = AudioApi.getInstance();
                MediaOptions options = new MediaOptions();
                options.setId(musicId);
                audioApi.getMediaApi().playMedia(audioApi.getClient(player.getUniqueId()), url + musicFile, options);
            } else {
                DebugManager.warn(Modules.Action.execute(SoundStartAction.class),"OpenAudioMc plugin not found!");
            }
        });
        //DebugManager.info(Modules.Action.create(this.getClass()),"MusicFile: "+musicFile+" musicId: "+musicId);
        getDescriptor().indent()
                .addLine("Music file: "+musicFile)
                .addLine("Music id: "+musicId).outdent();
    }

    public static boolean hasOpenAudio() {
        return Bukkit.getPluginManager().getPlugin("OpenAudioMc") != null;
    }
}
