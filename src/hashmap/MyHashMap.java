package hashmap;

import java.util.HashMap;

public class MyHashMap<K,V> {
    private Entry[] table;
    private static Integer DEFAULT_CAPACITY = 8;
    private int size = 0;

    public MyHashMap(){
        this.table = new Entry[DEFAULT_CAPACITY];
    }

    public MyHashMap(Integer size){
        this.table = new Entry[size];
    }

    public int size(){
        return size;
    }

    public V get(Object key){
        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        for(Entry<K,V> e = table[i];e!= null; e = e.next){
            if(e.k.equals(key)){

                return e.v;
            }
        }

        return null;
    }

    public V put(K key,V value){

        int hash = key.hashCode();
        int i = indexFor(hash, table.length);
        for(Entry<K,V> e = table[i];e!= null; e = e.next){
            if(e.k.equals(key)){
                V oldValue = e.v;
                e.v = value;
                return oldValue;
            }
        }

        addEntry(key, value, i);
        return value;
    }

    private void addEntry(K key, V value, int i) {
        Entry entry = new Entry(key,value,table[i]);
        table[i] = entry;
        size++;
    }

    private int indexFor(int hash, int length){
        return hash % length;
    }

    class Entry<K, V>{
        private K k;
        private V v;
        private Entry<K,V> next;

        public Entry(K k, V v, Entry<K,V> next){
            this.k = k;
            this.v = v;
            this.next = next;

        }
    }


    public static void main(String[] args) {
        MyHashMap<String, String> myHashMapExample = new MyHashMap<>();
        myHashMapExample.put("周瑜","老师");

        for (int i = 0; i < 10; i++) {
            myHashMapExample.put("蔡文姬"+i,"辅助"+i);
        }
        System.out.println(myHashMapExample.get("周瑜"));
        System.out.println(myHashMapExample.get("蔡文姬2"));
    }

}
