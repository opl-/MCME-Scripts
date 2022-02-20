package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.condition.Condition;
import com.mcmiddleearth.mcmescripts.debug.DebugManager;
import com.mcmiddleearth.mcmescripts.debug.Descriptor;
import com.mcmiddleearth.mcmescripts.debug.Modules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Logger;

public abstract class DecisionTreeTrigger extends Trigger {

    private DecisionNode decisionNode;

    public DecisionTreeTrigger() {
        decisionNode = new DecisionNode();
    }

    public DecisionTreeTrigger(Collection<Action> actions) {
        decisionNode = new DecisionNode(actions);
    }

    public DecisionTreeTrigger(Action action) {
        decisionNode = new DecisionNode(action);
    }

    public void addCondition(Condition condition) {
        decisionNode.addCondition(condition);
    }

    public void addConditions(Collection<Condition> conditions) {
        decisionNode.addConditions(conditions);
    }

    public DecisionNode getDecisionNode() {
        return decisionNode;
    }

    public void setDecisionNode(DecisionNode decisionNode) {
        this.decisionNode = decisionNode;
    }

    @Override
    public void call(TriggerContext context) {
        //context.getDescriptor().add(getDescriptor()).indent();
        context.getDescriptor().add(super.getDescriptor()).indent();
        decisionNode.call(context);
        context.getDescriptor().outdent();
        super.call(context);
    }

    @Override
    public Descriptor getDescriptor() {
        return super.getDescriptor().indent()
                    .add(decisionNode.getDescriptor()).outdent();
    }

    public static class DecisionNode {

        Collection<Action> actions;

        Collection<Condition> conditions;

        boolean metAllConditions = false;

        DecisionNode conditionSuccessTrigger, conditionFailTrigger;

        public DecisionNode() {
            actions = new HashSet<>();
        }

        public DecisionNode(Action action) {
            this();
            if(action != null)
                actions.add(action);
        }

        public DecisionNode(Collection<Action> actions) {
            this.actions = new HashSet<>(actions);
        }

        public void call(TriggerContext context) {
            //DebugManager.info(Modules.Trigger.call(this.getClass()),
            //        "Conditions: "+conditions.size()+" actions: "+actions.size()+" met all: "+metAllConditions);
            context.getDescriptor().addLine("Checking conditions ...").indent();
            if(checkConditions(context)) {
                context.getDescriptor().outdent().addLine("Condition check success! Accessing actions: ").indent();
                if(actions!=null) {
                    actions.forEach(action -> action.execute(context));
                }
                context.getDescriptor().outdent();
                if(conditionSuccessTrigger!=null) {
                    context.getDescriptor().addLine("Then: ").indent();
                    conditionSuccessTrigger.call(context);
                    context.getDescriptor().outdent();
                }
            } else {
                context.getDescriptor().outdent().addLine("Condition check failed!");
                if(conditionFailTrigger!=null) {
                    context.getDescriptor().addLine("Else: ").indent();
                    conditionFailTrigger.call(context);
                    context.getDescriptor().outdent();
                }
            }
        }

        private boolean checkConditions(TriggerContext context) {
            if(conditions == null || conditions.isEmpty()) {
                context.getDescriptor().addLine("Conditions: --none--");
                return true;
            }
            for(Condition condition: conditions) {
                context.getDescriptor().addLine("Met all: "+metAllConditions);
                if(metAllConditions && !condition.test(context)) {
                    return false;
                } else if(!metAllConditions && condition.test(context)) {
                    return true;
                }
            }
            return metAllConditions;
        }

        public void addCondition(Condition condition) {
            if(conditions==null) {
                conditions = new HashSet<>(Collections.singleton(condition));
            } else {
                conditions.add(condition);
            }
        }

        public void addConditions(Collection<Condition> conditions) {
            if(this.conditions==null) {
                this.conditions = new HashSet<>(conditions);
            } else {
                this.conditions.addAll(conditions);
            }
        }

        public void addAction(Action action) {
            actions.add(action);
        }

        public void addActions(Collection<Action> actions) {
            this.actions.addAll(actions);
        }

        public void setMetAllConditions(boolean metAllConditions) {
            this.metAllConditions = metAllConditions;
        }

        public void setConditionSuccessTrigger(DecisionNode conditionSuccessTrigger) {
            this.conditionSuccessTrigger = conditionSuccessTrigger;
        }

        public void setConditionFailTrigger(DecisionNode conditionFailTrigger) {
            this.conditionFailTrigger = conditionFailTrigger;
        }

        public String toString() {
            return "Actions: "+(actions==null?"null":actions.size()+"\n"+collectionToString(actions))
                 +" Conditions: "+(conditions==null?"null":conditions.size()+"\n"+collectionToString(conditions))+" metAll: "+metAllConditions;
        }

        public Descriptor getDescriptor() {
            Descriptor descriptor = new Descriptor();
            if(conditions.isEmpty()) {
                descriptor.addLine("Conditions: --none--");
            } else {
                descriptor.addLine("Conditions:").indent();
                conditions.forEach(condition -> descriptor.add(condition.getDescriptor()));
                descriptor.outdent();
            }
            if(actions.isEmpty()) {
                descriptor.addLine("Actions: --none--");
            } else {
                descriptor.addLine("Actions:").indent();
                actions.forEach(action -> descriptor.add(action.getDescriptor()));
                descriptor.outdent();
            }
            if(conditionSuccessTrigger != null) {
                descriptor.addLine("Then:").indent()
                        .add(conditionSuccessTrigger.getDescriptor()).outdent();
            }
            if(conditionFailTrigger != null) {
                descriptor.addLine("Else:").indent()
                        .add(conditionSuccessTrigger.getDescriptor()).outdent();
            }
            return descriptor;
        }

        @SuppressWarnings({"rawtypes","unchecked"})
        private String collectionToString(Collection collection) {
            StringBuilder builder = new StringBuilder();
            collection.forEach(element -> builder.append(element.toString()).append("\n"));
            return builder.toString();
        }
    }

    @Override
    public String toString() {
        return " CallOnce: "+isCallOnce()+"\nDecision node: \n"+decisionNode.toString();
    }

    /*@Override
    public String print(String indent) {
        return super.print(indent);
    }*/
}
