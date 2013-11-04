
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
	
	/*public WordNode(String word, WordNode next){
		this.word = word;
		this.next = next;
	}
	
	public WordNode(String word, WordNode next, WordNode previous){
		this.word = word;
		this.next = next;
		this.previous = previous;
	}*/
	
	
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
	
}
