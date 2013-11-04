
public class CreateTrieThread implements Runnable{
	
	//
	private WordNode head;
	private Trie current_trie;
	
	
	public CreateTrieThread(WordNode head, Trie current_trie){
		this.head = head;
		this.current_trie = current_trie;
	}
	
	
	@Override
	public void run() {
		WordNode temporary_node = this.head.getPrevious();
		
		if(temporary_node == null)
			return;
		
		do{
			temporary_node = temporary_node.getNext();

			this.current_trie.addWord(temporary_node.getWord());
		}while(temporary_node.getNext() != this.head);
		
	}

}
