import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class LFUCache {
    private final int capacity;
    private int minFreq;
    private final Map<Integer, Integer> keyValueMap;
    private final Map<Integer, Integer> keyFreqMap; 
    private final Map<Integer, LinkedHashMap<Integer, Integer>> freqMap;
    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyValueMap = new HashMap<>();
        this.keyFreqMap = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    public int get(int key) {
        if (!keyValueMap.containsKey(key)) {
            return -1; 
        }
        int value = keyValueMap.get(key);
        int freq = keyFreqMap.get(key);
        keyFreqMap.put(key, freq + 1);
        freqMap.get(freq).remove(key);
        if (freqMap.get(freq).isEmpty()) {
            freqMap.remove(freq);
            if (minFreq == freq) {
                minFreq++;
            }
        }
        freqMap.computeIfAbsent(freq + 1, k -> new LinkedHashMap<>()).put(key, value);
        
        return value; 
    }

    public void put(int key, int value) {
        if (capacity == 0) return;        
        if (keyValueMap.containsKey(key)) {
            keyValueMap.put(key, value);
            get(key); 
            return;
        }
        if (keyValueMap.size() >= capacity) {
            Map.Entry<Integer, Integer> lfuEntry = freqMap.get(minFreq).entrySet().iterator().next();
            int lfuKey = lfuEntry.getKey();
            keyValueMap.remove(lfuKey);
            keyFreqMap.remove(lfuKey);
            freqMap.get(minFreq).remove(lfuKey);
            if (freqMap.get(minFreq).isEmpty()) {
                freqMap.remove(minFreq);
            }
        }
        keyValueMap.put(key, value);
        keyFreqMap.put(key, 1);
        minFreq = 1;         
        freqMap.computeIfAbsent(1, k -> new LinkedHashMap<>()).put(key, value);
    }
}
public class Main {
    public static void main(String[] args) {
        LFUCache lfuCache = new LFUCache(2); 
        lfuCache.put(1, 1);      
        lfuCache.put(2, 2);      
        System.out.println(lfuCache.get(1)); 
        lfuCache.put(3, 3);      
        System.out.println(lfuCache.get(2)); 
        System.out.println(lfuCache.get(3)); 
        lfuCache.put(4, 4);      
        System.out.println(lfuCache.get(1)); 
        
        System.out.println(lfuCache.get(3)); 
        
        System.out.println(lfuCache.get(4)); 
    }
}
