package com.mcmiddleearth.mcmescripts.action;

import com.craftmend.openaudiomc.api.interfaces.AudioApi;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.entity.Player;

public class SoundStopAction  extends SelectingAction<Player> {

    public SoundStopAction(Selector<Player> selector, String musicId) {
        super(selector, (player, context) -> {
            DebugManager.verbose(Modules.Action.execute(SoundStopAction.class),"musicId: "+musicId);
            if(SoundStartAction.hasOpenAudio()) {
                AudioApi audioApi = AudioApi.getInstance();
                if (musicId == null || musicId.equalsIgnoreCase("")) {
                    audioApi.getMediaApi().stopMedia(audioApi.getClient(player.getUniqueId()));
                } else {
                    audioApi.getMediaApi().stopMedia(audioApi.getClient(player.getUniqueId()), musicId);
                }
            }
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"MusicId: "+musicId);
    }
}
