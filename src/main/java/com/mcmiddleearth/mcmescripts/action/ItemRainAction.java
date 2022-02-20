package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ItemRainAction extends SelectingAction<McmeEntity> {

    private static final Random random = new Random();

    public ItemRainAction(Selector<McmeEntity> selector, Set<ItemStack> items, int size, int drop_height, double probability, int duration) {
        super(selector, ((entity, context) -> {
            List<Item> spawnedItems = new ArrayList<>();
            List<ItemStack> itemList = new ArrayList<>(items);
            int stop = duration * 2;
            if(entity !=null) {
                new BukkitRunnable() {
                    int counter = 0;
                    @Override
                    public void run() {
                        if(counter<duration) {
                            Location location = entity.getLocation().add(0,drop_height,0);
                            for(int i = -size; i<size; i++) {
                                for(int j = -size; j<size; j++) {
                                    for(int k = 0; k<4; k+=2) {
                                        if (random.nextDouble() < probability) {
                                            ItemStack item = itemList.get(random.nextInt(itemList.size()));
                                            spawnedItems.add(location.getWorld().dropItemNaturally(location.clone().add(i, k, j), item));
                                        }
                                    }
                                }
                            }
                        } else if(counter < stop) {
                            if(spawnedItems.size()>0) {
                                for (int i = 0; i < spawnedItems.size() * 0.2; i++) {
                                    int index = random.nextInt(spawnedItems.size());
                                    Item item = spawnedItems.get(index);
                                    spawnedItems.remove(index);
                                    item.remove();
                                }
                            }
                        } else {
                            spawnedItems.forEach(Item::remove);
                            cancel();
                        }
                        counter++;
                    }
                }.runTaskTimer(MCMEScripts.getInstance(),0,2);
            }
        }));
        getDescriptor().indent()
                .addLine("Size: "+size)
                .addLine("Drop height: "+drop_height)
                .addLine("Probability: "+probability)
                .addLine("Duration: "+duration);
        if(!items.isEmpty()) {
            getDescriptor().addLine("Items: ").indent();
            items.forEach(item -> getDescriptor().addLine(item.getType().name()));
            getDescriptor().outdent();
        } else {
            getDescriptor().addLine("Items: --none--");
        }
        getDescriptor().outdent();
    }
}
