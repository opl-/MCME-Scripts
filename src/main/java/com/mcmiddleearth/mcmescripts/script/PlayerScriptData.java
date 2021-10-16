package com.mcmiddleearth.mcmescripts.script;

import com.mcmiddleearth.mcmescripts.dialogue.DialogueNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerScriptData {

    private Map<String, DialogueNode> currentDialogueNodes = new HashMap<>();

    private Map<String, Set<String>> currentQuestStages = new HashMap<>();
}
