package com.mcmiddleearth.mcmescripts.dialogue;

import com.mcmiddleearth.mcmescripts.action.Action;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DialogueNode {

    private Component prompt;

    private Map<String,DialogueNode> answers = new HashMap<>();

    private Set<Action> actions = new HashSet<>();
}
