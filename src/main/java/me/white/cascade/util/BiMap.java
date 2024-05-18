package me.white.cascade.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BiMap<T, U> {
    private Map<T, U> tuMap = new HashMap<>();
    private Map<U, T> utMap = new HashMap<>();

    public Map<T, U> getTUMap() {
        return tuMap;
    }

    public Map<U, T> getUTMap() {
        return utMap;
    }

    public void putTU(T t, U u) {
        tuMap.put(t, u);
        utMap.put(u, t);
    }

    public void putUT(U u, T t) {
        tuMap.put(t, u);
        utMap.put(u, t);
    }

    public Set<T> getTSet() {
        return tuMap.keySet();
    }

    public Set<U> getUSet() {
        return utMap.keySet();
    }

    public Set<Entry<T, U>> getEntrySet() {
        Set<Entry<T, U>> set = new HashSet<>();
        for (Map.Entry<T, U> entry : tuMap.entrySet()) {
            set.add(new Entry<>(entry.getKey(), entry.getValue()));
        }
        return set;
    }

    public U getU(T t) {
        return tuMap.get(t);
    }

    public T getT(U u) {
        return utMap.get(u);
    }

    public boolean containsT(T t) {
        return tuMap.containsKey(t);
    }

    public boolean containsU(U u) {
        return utMap.containsKey(u);
    }

    public U removeT(T t) {
        U u = tuMap.remove(t);
        utMap.remove(u);
        return u;
    }

    public T removeU(U u) {
        T t = utMap.remove(u);
        tuMap.remove(t);
        return t;
    }

    public void clear() {
        tuMap.clear();
        utMap.clear();
    }

    public static class Entry<T, U> {
        private T t;
        private U u;

        private Entry(T t, U u) {
            this.t = t;
            this.u = u;
        }

        public T getT() {
            return t;
        }

        public U getU() {
            return u;
        }
    }
}
