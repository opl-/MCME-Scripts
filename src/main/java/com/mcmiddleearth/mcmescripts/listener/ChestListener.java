package com.mcmiddleearth.mcmescripts.listener;

import com.google.api.client.json.JsonString;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.compiler.LocationCompiler;
import com.mcmiddleearth.mcmescripts.compiler.LootTableCompiler;
import com.mcmiddleearth.mcmescripts.looting.ItemChoice;
import com.mcmiddleearth.mcmescripts.looting.LootTable;
import com.mcmiddleearth.mcmescripts.utils.JsonUtils;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ChestListener implements Listener {

    private static final Set<Location> openedChests = new HashSet<>();

    private final LootTable lootTable;


    private static final File openedChestFile = new File(MCMEScripts.getInstance().getDataFolder(),"openedChests.json");
    private static final File lootTableFile = new File(MCMEScripts.getInstance().getDataFolder(),"lootTable.json");

    public ChestListener() {
        loadOpenedChests();
        lootTable = loadLootTable();
    }

    @EventHandler
    public void onOpenChest(InventoryOpenEvent event) {
        Inventory chestInventory = event.getInventory();
        if(chestInventory.getHolder() instanceof Chest) {
            if(!openedChests.contains(((Chest)chestInventory.getHolder()).getLocation())) {
                chestInventory.clear();
                chestInventory.addItem(getLoot());
                addChest(((Chest)chestInventory.getHolder()).getLocation());
                openedChests.add(((Chest)chestInventory.getHolder()).getLocation());
                saveOpenedChests();
            }
        }
    }

    public static void addChest(Location location) {
        openedChests.add(location);
        saveOpenedChests();
    }

    private ItemStack[] getLoot() {
        return lootTable.selectItems().toArray(new ItemStack[0]);
    }

    private static void saveOpenedChests() {
        try(PrintWriter writer = new PrintWriter(new FileOutputStream(openedChestFile))) {
            for(Location location: openedChests) {
                writer.println(location.getWorld().getName()+","+location.getX()+","+location.getY()+","+location.getZ());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadOpenedChests() {
        if(openedChestFile.exists()) {
            openedChests.clear();
            try(Scanner reader = new Scanner(openedChestFile)) {
                while(reader.hasNext()) {
                    LocationCompiler.compile(new JsonPrimitive(reader.nextLine())).ifPresent(openedChests::add);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private LootTable loadLootTable() {
        Set<ItemChoice> choices = null;
        try {
            JsonObject jsonObject = JsonUtils.loadJsonData(lootTableFile);
            if(jsonObject!=null) {
                choices = LootTableCompiler.compileItemChoices(jsonObject).orElse(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LootTable(choices);
    }
}
