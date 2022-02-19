package com.mcmiddleearth.mcmescripts.command.arguments;

import com.mcmiddleearth.mcmescripts.MCMEScripts;
import com.mcmiddleearth.mcmescripts.script.Script;
import com.mcmiddleearth.mcmescripts.trigger.ExternalTrigger;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class TriggerArgument extends HelpfulScriptsArgumentType implements ArgumentType<String> {

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Script script = MCMEScripts.getScriptManager().getScript(context.getArgument("script",String.class));
        if(script!=null) {
            script.getTriggers().stream().filter(trigger->trigger instanceof ExternalTrigger)
                    .forEach(trigger -> builder.suggest(trigger.getName()));
        }
        return builder.buildFuture();

    }

}
