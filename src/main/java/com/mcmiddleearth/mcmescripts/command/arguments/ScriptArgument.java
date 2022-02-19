package com.mcmiddleearth.mcmescripts.command.arguments;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class ScriptArgument extends HelpfulScriptsArgumentType implements ArgumentType<String> {

    private final boolean requiresActiveScript;
    private final boolean acceptWildcards;

    public ScriptArgument(boolean requiresActiveScript, boolean acceptWildcards) {
        this.requiresActiveScript = requiresActiveScript;
        this.acceptWildcards = acceptWildcards;
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            MCMEScripts.getScriptManager().getScripts().values().stream().filter(script -> (!requiresActiveScript || script.isActive()))
                    .forEach(script -> builder.suggest(script.getName()));
            if(acceptWildcards) {
                builder.suggest("all").suggest("none");
            }
            return builder.buildFuture();

    }

}
