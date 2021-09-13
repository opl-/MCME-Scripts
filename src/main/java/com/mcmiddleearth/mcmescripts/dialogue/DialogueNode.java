package com.mcmiddleearth.mcmescripts.dialogue;

import com.mcmiddleearth.mcmescripts.action.Action;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DialogueNode {

    private Component prompt;

    private final Map<String,DialogueNode> answers = new HashMap<>();

    private final Set<Action> actions = new HashSet<>();
}
