package spell;

public class Trie implements ITrie{
	
	Node root;
	int nodeCount;
	int wordCount;
	
	public Trie(){
		root = new Node();
		nodeCount = 1;
		wordCount = 0;
	}

	@Override
	public void add(String word) {
		root.addWord(word, -1);
	}

	@Override
	public INode find(String word) {
		Node n = new Node();
		n = root.findWord(word, -1);
		if (n != null && n.getNodeFreq() > 0) {
			return n;
		}
		else{
			return null;
		}
	}

	@Override
	public int getWordCount() {
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		return nodeCount;
	}
	
	@Override
	public int hashCode() {
		return wordCount * nodeCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trie other = (Trie) obj;
		if (nodeCount != other.nodeCount)
			return false;
		if (this.hashCode() != other.hashCode()) {
			return false;
		}
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		if (wordCount != other.wordCount)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		StringBuilder word = new StringBuilder();
		StringBuilder list =  new StringBuilder();
		root.toString(word, list);
		return list.toString();
	}
	
	public int getNodeFreq(String word){
		Node n = root.findWord(word, -1);
		return n.nodeFreq;
	}
	
	
	
	
	//-----------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------
	
	protected class Node implements INode{
		int nodeFreq;
		Node[] nodes;
		String name;
		
		public Node(){
			nodes = new Node[26];
			nodeFreq = 0;
		}
		
		public int getNodeFreq(){
			return nodeFreq;
		}
		
		public String getName(){
			return name;
		}
		
		public void addWord(String word, int spot){
			spot++;
			if (spot < word.length()) {
				char c = word.charAt(spot);
				int arrayspot = c - 'a';
				if (nodes[arrayspot] == null) {
					nodes[arrayspot] = new Node();
					nodeCount++;
					nodes[arrayspot].addWord(word, spot);
				}
				else{
					nodes[arrayspot].addWord(word, spot);
				}
			}
			else{
				if (nodeFreq == 0) {
					wordCount++;
				}
				nodeFreq++;
				name = word;
			}
		}
		
		public Node findWord(String word, int spot){
			spot++;
			if (spot < word.length()) {
				char c = word.charAt(spot);
				int arrayspot = c - 'a';
				if (nodes[arrayspot] != null) {
					return nodes[arrayspot].findWord(word, spot);
				}
				else{
					return null;
				}
			}
			else{
				return this;
			}
		}

		@Override
		public int getValue() {
			return nodeFreq;
		}

		@Override
		public int hashCode() {
			return wordCount * nodeCount;
		}

		@Override
		public boolean equals(Object obj) {
			boolean result = true;
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (nodeFreq != other.nodeFreq)
				return false;
			for (int i = 0; i < 26 && result; i++) {
				if (this.nodes[i]==null && other.nodes[i] == null) {
					
				}
				else if (this.nodes[i] == null || other.nodes[i] == null) {
					result = false;
				}
				else{
					result = this.nodes[i].equals(other.nodes[i]);
				}
			}
			return true;
		}
		
		public String toString(StringBuilder word, StringBuilder list){
			if (this.nodeFreq > 0) {
				list.append(word.toString());
				list.append("\n");
			}
			for (int i = 0; i < 25; i++) {
				if (nodes[i] != null) {}
				else{
					char c = (char) (i+97);
					word.append(c);
					nodes[i].toString(word, list);
					word.deleteCharAt(word.length()-1);
				}
			}
			return list.toString();
		}
	}
}
