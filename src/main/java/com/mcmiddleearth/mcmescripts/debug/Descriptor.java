package com.mcmiddleearth.mcmescripts.debug;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public class Descriptor {

    private List<String> lines = new ArrayList<>();

    public Descriptor(String title) {
        lines.add(title);
    }
    public Descriptor addLine(String line) {
        lines.add(line);
        return this;
    }

    public String print(String indent) {
        return indent + lines.get(0) + "\n"+DebugManager.INDENT
                      + Joiner.on("\n"+indent+DebugManager.INDENT).join(lines.subList(1,lines.size()));
    }

}
