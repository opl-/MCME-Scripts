package com.mcmiddleearth.mcmescripts.command.arguments;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.debug.Modules;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ModuleArgument extends HelpfulScriptsArgumentType implements ArgumentType<String> {

    private final boolean acceptAll;
    private final boolean acceptNone;

    public ModuleArgument(boolean acceptAll, boolean acceptNone) {
        this.acceptAll = acceptAll;
        this.acceptNone = acceptNone;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Arrays.stream(Modules.values()).forEach(module -> builder.suggest(module.getModule()));
        if(acceptAll) {
            builder.suggest("all");
        }
        if(acceptNone) {
            builder.suggest("none");
        }
        return builder.buildFuture();
    }

}