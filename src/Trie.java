
public class Trie {
	
	
	// Attributes
	private CharNode head;
	
	
	// Constructors
	public Trie(){
		this.head = new CharNode();
	}
	
	public Trie(char head){
		this.head = new CharNode(head);
	}
	
	
	// Accessors
	public CharNode getHead(){return this.head;}
	
	
	// Methods
	public boolean addWord(String word){
		CharNode temporary_node;
		CharNode temporary_child;
		int i=1;
		
		if(word.length() <= 0)
			return false;
			
		if(this.head.getChar() != word.charAt(0))
			return false;
		
		temporary_node = this.head;
		
		/*if(word.length() == 1)
			temporary_node.updateEnd();
			return true;*/
		
		for(i=1; i < word.length(); ++i){
			if((temporary_child = temporary_node.getChild(word.charAt(i))) == null)
				break;
			temporary_node = temporary_child;
		}
		
		for(int j=i; j < word.length(); ++j){
			temporary_child = new CharNode(word.charAt(j), temporary_node);
			temporary_node.addChild(temporary_child);
			
			temporary_node = temporary_child;
		}
		
		temporary_node.updateEnd();
		
		return true;
	}
	
	
	// DEBUGGING PURPOSES ONLY
	public void listTrie(){
		System.out.println(this.head.getChar());
		for(CharNode each:this.head.getChildren()){
			listTrie(1, each);
		}
	}
	
	public void listTrie(int level, CharNode next){
		for(int i=0; i < level; ++i){
			System.out.print(' ');
		}
		System.out.println(next.getChar());
		
		for(CharNode each:next.getChildren()){
			listTrie(level + 1, each);
		}
	}
}
