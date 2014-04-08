import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Trie<T> {

    private TrieNode<T> root = null;
    private int size = 0;

    public Trie() {
        this.root = new TrieNode<T>();
    }

    public void put(String key, T value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value cannot be null");
        }
        TrieNode<T> curr = root;
        for (int i = 0; i < key.length(); i++) {
            Character ch = key.charAt(i);
            if (!curr.hasChild(ch)) {
                curr.addChild(ch);
            }
            curr = curr.getChild(ch);
        }

        if (curr.getValue() == null) {
            this.size++;
        }

        curr.setKeyNode(true);
        curr.setValue(value);
    }

    public T get(String key) {
        if (null == key) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        TrieNode<T> curr = root;
        for (int i = 0; i < key.length(); i++) {
            Character ch = key.charAt(i);
            if (curr.hasChild(ch)) {
                curr = curr.getChild(ch);
            } else {
                return null;
            }
        }

        if (!curr.isKeyNode()) {
            return null;
        }

        return curr.getValue();
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public Set<String> keySet() {
        Map<String, T> allValues = new TreeMap<String, T>();
        getAllValues("", root, allValues);
        return allValues.keySet();
    }

    public int size() {
        return this.size;
    }

    public void remove(String key) {
        throw new UnsupportedOperationException("Remove operation not supported");
    }

    public Map<String, T> search(String prefix) {
        Map<String, T> values = new TreeMap<String, T>();

        TrieNode<T> curr = root;

        for (int i = 0; i < prefix.length(); i++) {
            Character ch = prefix.charAt(i);
            if (curr.hasChild(ch)) {
                curr = curr.getChild(ch);
            } else {
                curr = null;
                break;
            }
        }

        if (curr != null) {
            getAllValues(prefix, curr, values);
        }

        return values;
    }

    private void getAllValues(String nodePrefix, TrieNode<T> node, Map<String, T> values) {
        if (node.isKeyNode()) {
            values.put(nodePrefix, node.getValue());
        }

        Set<Entry<Character, TrieNode<T>>> children = node.getChildren().entrySet();

        for (Entry<Character, TrieNode<T>> child : children) {
            String prefix = nodePrefix + child.getKey();
            getAllValues(prefix, child.getValue(), values);
        }
    }
}
