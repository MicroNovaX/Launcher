package ru.gravit.utils.helper;

import ru.gravit.launcher.LauncherAPI;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonHelper {
    @LauncherAPI
    public static final ScriptEngineManager scriptManager = new ScriptEngineManager();
    @LauncherAPI
    public static final ScriptEngineFactory nashornFactory = getEngineFactories(scriptManager);

    private static ScriptEngineFactory getEngineFactories(ScriptEngineManager manager) {
        // Метод похож на костыль но таковым не является, ибо единоразовое получение фактории быстрее, чем её переполучение на ходу.
        for (ScriptEngineFactory fact : manager.getEngineFactories())
            if (fact.getNames().contains("nashorn") || fact.getNames().contains("Nashorn")) return fact;
        return null;
    }

    @LauncherAPI
    public static String low(String s) {
        return s.toLowerCase(Locale.US);
    }

    @LauncherAPI
    public static boolean multiMatches(Pattern[] pattern, String from) {
        for (Pattern p : pattern)
            if (p.matcher(from).matches()) return true;
        return false;
    }

    @LauncherAPI
    public static String multiReplace(Pattern[] pattern, String from, String replace) {
        Matcher m;
        String tmp = null;
        for (Pattern p : pattern) {
            m = p.matcher(from);
            if (m.matches()) tmp = m.replaceAll(replace);
        }
        return tmp != null ? tmp : from;
    }

    @LauncherAPI
    public static ScriptEngine newScriptEngine() {
        return nashornFactory.getScriptEngine();
    }

    @LauncherAPI
    public static Thread newThread(String name, boolean daemon, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(daemon);
        if (name != null)
            thread.setName(name);
        return thread;
    }

    @LauncherAPI
    public static String replace(String source, String... params) {
        for (int i = 0; i < params.length; i += 2)
            source = source.replace('%' + params[i] + '%', params[i + 1]);
        return source;
    }

    private CommonHelper() {
    }
}
