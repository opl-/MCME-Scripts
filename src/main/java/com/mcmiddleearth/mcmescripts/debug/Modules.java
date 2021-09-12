package com.mcmiddleearth.mcmescripts.debug;

@SuppressWarnings("rawtypes")
public class Modules {

    public static class Action {
        public static String execute(Class clazz) {
            return "action.execute."+clazz.getSimpleName();
        }

        public static String create(Class clazz) {
            return "action.create."+clazz.getSimpleName();
        }
    }

    public static class Condition {
        public static String test(Class clazz) {
            return "condition.test."+clazz.getSimpleName();
        }

        public static String create(Class clazz) {
            return "condition.create."+clazz.getSimpleName();
        }
    }

    public static class Trigger {
        public static String call(Class clazz) {
            return "trigger.call."+clazz.getSimpleName();
        }

        public static String create(Class clazz) {
            return "trigger.create."+clazz.getSimpleName();
        }

        public static String register(Class clazz) {
            return "trigger.register."+clazz.getSimpleName();
        }

        public static String unregister(Class clazz) {
            return "trigger.unregister."+clazz.getSimpleName();
        }
    }

    public static class Script {
        public static String create(Class clazz) {
            return "script.read."+clazz.getSimpleName();
        }

        public static String load(Class clazz) {
            return "script.load."+clazz.getSimpleName();
        }

        public static String unload(Class clazz) {
            return "script.unload."+clazz.getSimpleName();
        }
    }

    public static class Selector {
        public static String select(Class clazz) {
            return "selector.select."+clazz.getSimpleName();
        }

        public static String create(Class clazz) {
            return "selector.create."+clazz.getSimpleName();
        }

    }

}
