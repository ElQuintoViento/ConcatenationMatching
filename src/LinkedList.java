
public class LinkedList {

	// Attributes
	private WordNode head;
	private int counter;
	
	
	// Constructors
	public LinkedList(){
		this.counter = 0;
	}
	
	public LinkedList(WordNode head){
		this.head = head;
		this.counter = 0;
		++this.counter;
	}
	
	
	// Accessors
	public WordNode getHead(){return this.head;}
	
	public int getSize(){return this.counter;}
	
	
	// Mutators
	public boolean updateHead(WordNode head){
		return (this.head = head) == head;
	}
	
	
	// Methods
	public void add(WordNode node){
		//WordNode node = new WordNode(word);
		//boolean success = false;
		WordNode temporary_node;
		
		if (this.counter <= 0){
			updateHead(node);
			this.head.updatePrevious(node);
			this.head.updateNext(node);
			
			++this.counter;
			
			// DEBUGGING PURPOSES ONLY
			//System.out.println("Added HEAD");
			
		}else{
			temporary_node = this.head.getPrevious();
			
			this.head.updatePrevious(node);
			node.updateNext(this.head);
			
			temporary_node.updateNext(node);
			node.updatePrevious(temporary_node);
			
			++this.counter;
			
			// DEBUGGING PURPOSES ONLY
			//System.out.println("Added Another");
		}
		
	}
}
