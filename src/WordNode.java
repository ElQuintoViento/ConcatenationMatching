
public class WordNode {
	
	
	// Attributes
	private String word;
	private WordNode next;
	private WordNode previous;
	private WordNode[] parents;
	private WordNode[] children;
	
	
	// Constructors
	public WordNode(){}
	
	public WordNode(String word){
		this.word = word;
	}
	
	
	// Accessors
	public String getWord(){return this.word;}
	
	public WordNode getNext(){return this.next;}
	
	public WordNode getPrevious(){return this.previous;}
	
	
	// Mutators
	public boolean updateNext(WordNode next){
		return (this.next = next) == next;
	}
	
	public boolean updatePrevious(WordNode previous){
		return (this.previous = previous) == previous;
	}
	
	
	// Methods
	public WordNode copy(){
		WordNode new_node = new WordNode(this.word);
		
		return new_node;
	}
	
}
