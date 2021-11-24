package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.entities.entities.VirtualEntity;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mcmiddleearth.mcmescripts.selector.McmeEntitySelector;
import com.mcmiddleearth.mcmescripts.selector.Selector;

import java.util.HashSet;
import java.util.List;

public class SetEnemyAction extends SelectingAction<VirtualEntity> {

    public SetEnemyAction(Selector<VirtualEntity> selector, McmeEntitySelector enemySelector) {
        super(selector, (entity, context)  -> {
            List<McmeEntity> enemies = enemySelector.select(context);
            DebugManager.verbose(Modules.Action.execute(SetEnemyAction.class),"Set enemies: "+enemySelector.getSelector()
                                                                                        +" -> "+enemies.size()+" selected.");
            enemies.forEach(enemy->DebugManager.verbose(Modules.Action.execute(SetEnemyAction.class),"\n"+enemy.getName()));
            entity.setEnemies(new HashSet<>(enemies));
        });
        DebugManager.info(Modules.Action.create(this.getClass()),"Set enemies: "+enemySelector.getSelector());
    }
}
