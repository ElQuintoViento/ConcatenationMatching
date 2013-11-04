
public class CreateTrieThread implements Runnable{
	
	
	// Attributes
	private WordNode head;
	private Trie current_trie;
	
	
	public CreateTrieThread(WordNode head, Trie current_trie){
		this.head = head;
		this.current_trie = current_trie;
	}
	
	
	@Override
	public void run() {
		WordNode temporary_node = this.head;
		
		if(temporary_node == null)
			return;
		
		temporary_node = temporary_node.getPrevious();
		
		do{
			temporary_node = temporary_node.getNext();

			this.current_trie.addWord(temporary_node.getWord());
		}while(temporary_node.getNext() != this.head);
		
	}

}
