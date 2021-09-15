package com.mcmiddleearth.mcmescripts.trigger;

import com.mcmiddleearth.mcmescripts.action.Action;
import com.mcmiddleearth.mcmescripts.condition.Condition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

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

    public void call(TriggerContext context) {
        decisionNode.call(context);
        super.call(context);
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
            if(checkConditions(context)) {
                if(actions!=null) {
                    actions.forEach(action -> action.execute(context));
                }
                if(conditionSuccessTrigger!=null) {
                    conditionSuccessTrigger.call(context);
                }
            } else {
                if(conditionFailTrigger!=null) {
                    conditionFailTrigger.call(context);
                }
            }
        }

        private boolean checkConditions(TriggerContext context) {
            if(conditions == null || conditions.isEmpty()) return true;
            for(Condition condition: conditions) {
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
            return "Actions: "+(actions==null?"null":actions.size())
                 +" Conditions: "+(conditions==null?"null":conditions.size())+" metAll: "+metAllConditions;
        }
    }

    public String toString() {
        return " CallOnce: "+isCallOnce()+"\nDecision node: \n"+decisionNode.toString();
    }
}
