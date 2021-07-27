package com.sakura.websocket;


import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class BiDirectionHashMap<K,V> {

    private ConcurrentHashMap<K,V> k2v;
    private ConcurrentHashMap<V,K> v2k;


    BiDirectionHashMap(){
        this.k2v = new ConcurrentHashMap<>();
        this.v2k = new ConcurrentHashMap<>();
    }


    public void put(K k, V v){
        k2v.put(k, v);
        v2k.put(v, k);
    }

    public int size(){
        return k2v.size();
    }

    public boolean containsKey(K k){
        return k2v.containsKey(k);
    }

    public boolean containsValue(V v){
        return v2k.containsValue(v);
    }

    public boolean removeBykey(K k){
        if(!k2v.containsKey(k)){
            return false;
        }

        V value = k2v.get(k);
        k2v.remove(k);
        v2k.remove(value);
        return true;
    }


    public boolean removeByValue(V v) {
        if (!v2k.containsKey(v)) {
            return false;
        }

        K key = v2k.get(v);
        v2k.remove(v);
        k2v.remove(key);
        return true;
    }

    public V getByKey(K k) {
        return k2v.getOrDefault(k, null);
    }

    public K getByValue(V v) {
        return v2k.getOrDefault(v, null);
    }

    public Collection<K> keys() {
        return v2k.values();
    }

    public Collection<V> values() {
        return k2v.values();
    }

}
