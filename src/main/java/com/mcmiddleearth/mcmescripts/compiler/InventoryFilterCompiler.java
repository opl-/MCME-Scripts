package com.mcmiddleearth.mcmescripts.compiler;

import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcmiddleearth.mcmescripts.component.InventoryFilter;
import com.mcmiddleearth.mcmescripts.component.ItemFilter;

public class InventoryFilterCompiler {
    private static final String
            KEY_ITEM_FILTER         = "item_filter",
            KEY_ITEM_FILTERS        = "item_filters",
            KEY_REQUIRE_ALL_FILTERS = "require_all_filters";

    public static InventoryFilter compile(JsonObject jsonObject) {
        Set<ItemFilter> itemFilters = ItemFilterCompiler.compile(jsonObject.get(KEY_ITEM_FILTER));
        itemFilters.addAll(ItemFilterCompiler.compile(jsonObject.get(KEY_ITEM_FILTERS)));

        JsonElement requireAllFiltersElement = jsonObject.get(KEY_REQUIRE_ALL_FILTERS);
        boolean requireAllFilters = requireAllFiltersElement != null && requireAllFiltersElement.getAsBoolean();

        return new InventoryFilter(itemFilters, requireAllFilters);
    }
}
