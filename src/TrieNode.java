import java.util.HashMap;
import java.util.Map;

public class TrieNode<T> {

	private Character key;
	private T value;
	private Map<Character, TrieNode<T>> children;
	private boolean isKeyNode;

	public TrieNode() {
		this.setKey(null);
		this.setValue(null);
		this.setChildren(new HashMap<Character, TrieNode<T>>());
	}

	public TrieNode(Character key) {
		this.setKey(key);
		this.setValue(null);
		this.setChildren(new HashMap<Character, TrieNode<T>>());
	}

	public TrieNode(Character key, T value) {
		this.setKey(key);
		this.setValue(value);
		this.setChildren(new HashMap<Character, TrieNode<T>>());
	}

	public TrieNode<T> addChild(Character ch) {
		TrieNode<T> trieNode = new TrieNode<T>(ch);
		this.getChildren().put(ch, trieNode);
		return trieNode;
	}

	public TrieNode<T> getChild(Character ch) {
		return this.getChildren().get(ch);
	}

	public boolean hasChild(Character ch) {
		if (this.getChildren().get(ch) != null) {
			return true;
		}
		return false;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean isKeyNode() {
		return isKeyNode;
	}

	public void setKeyNode(boolean isKeyNode) {
		this.isKeyNode = isKeyNode;
	}

	public Character getKey() {
		return key;
	}

	public void setKey(Character key) {
		this.key = key;
	}

	public Map<Character, TrieNode<T>> getChildren() {
		return children;
	}

	public void setChildren(Map<Character, TrieNode<T>> children) {
		this.children = children;
	}
}
