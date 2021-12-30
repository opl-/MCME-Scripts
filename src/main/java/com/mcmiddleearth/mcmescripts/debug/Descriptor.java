package com.mcmiddleearth.mcmescripts.debug;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public class Descriptor {

    private List<String> lines = new ArrayList<>();

    private int indentLevel = 1;

    public Descriptor(String title) {
        lines.add(title);
    }
    public Descriptor addLine(String line) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<indentLevel; i++) { builder.append(DebugManager.INDENT);}
        builder.append(line);
        lines.add(builder.toString());
        return this;
    }

    public Descriptor add(Descriptor other) {
        other.lines.forEach(this::addLine);
        return this;
    }

    public Descriptor indent() {
        indentLevel++;
        return this;
    }

    public Descriptor outdent() {
        if(indentLevel>0) indentLevel--;
        return this;
    }

    public String print(String indent) {
        return indent + Joiner.on("\n"+indent).join(lines);
    }

}
