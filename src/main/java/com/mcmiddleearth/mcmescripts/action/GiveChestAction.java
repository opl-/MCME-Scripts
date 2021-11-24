package com.mcmiddleearth.mcmescripts.action;

import com.mcmiddleearth.entities.entities.McmeEntity;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.listener.ChestListener;
import com.mcmiddleearth.mcmescripts.looting.ItemChoice;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.selector.Selector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Set;

public class GiveChestAction extends SelectingAction<McmeEntity> {

    public GiveChestAction(Selector<McmeEntity> selector, Set<ItemStack> items, Set<ItemChoice> choices, int duration) {
        super(selector, (entity, context) -> {
            Location loc = entity.getLocation().clone();
            BlockFace face = null;
            if(loc.getYaw()<-135 || loc.getYaw()>135) face = BlockFace.NORTH;
            else if(loc.getYaw()<-45) face = BlockFace.EAST;
            else if(loc.getYaw()<45) face = BlockFace.SOUTH;
            else face = BlockFace.WEST;
            Block block = loc.getBlock().getRelative(face,2);
            if(!block.isPassable()) {
                block = block.getRelative(face.getOppositeFace());
                if(!block.isPassable()) {
                    block = block.getRelative(BlockFace.UP);
                    if(!block.isPassable()) {
                        block = loc.getBlock();
                    }
                }
            }
            BlockData data = Bukkit.createBlockData(Material.CHEST);
            ((Chest)data).setFacing(face.getOppositeFace());
            BlockState restore = block.getState();
            block.setBlockData(data,false);
            ChestListener.addChest(block.getLocation());
            Block finalBlock = block;
            new BukkitRunnable() {
                @Override
                public void run() {
                    org.bukkit.block.Chest chest = ((org.bukkit.block.Chest) finalBlock.getState());
                    int size = chest.getInventory().getContents().length;
                    chest.getInventory().setContents(Arrays.copyOfRange(items.toArray(new ItemStack[0]),0,size));
                    LootTable lootTable = new LootTable(choices);
                    lootTable.selectItems().forEach(item->chest.getInventory().addItem(item));
                }
            }.runTaskLater(MCMEScripts.getInstance(),1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    org.bukkit.block.Chest chest = ((org.bukkit.block.Chest) finalBlock.getState());
                    chest.getInventory().clear();
                    restore.update(true, false);
                }
            }.runTaskLater(MCMEScripts.getInstance(),duration+1);
        });
    }
}
