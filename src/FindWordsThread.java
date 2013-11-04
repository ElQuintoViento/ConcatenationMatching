
public class FindWordsThread implements Runnable{

	
	// Attributes
	private Trie current_trie;
	private Trie[] all_tries;
	private LinkedList list;
	private LinkedList formable_words;
	
	
	// Constructors
	public FindWordsThread(Trie current_trie, Trie[] all_tries, LinkedList list, LinkedList formable_words){
		this.current_trie = current_trie;
		this.all_tries = all_tries;
		this.list = list;
		this.formable_words = formable_words;
	}
	
	
	// Methods
	private boolean isFormable(String word, int index){
		int char_index = word.charAt(index) - 97;
		Trie temporary_trie = all_tries[char_index];
		
		return false;
	}
	
	private boolean isFormable(String word){
		CharNode temporary_char;
		boolean formable = false;
		
		if(word.length() <= 1)
			return false;
		
		temporary_char = current_trie.getHead();
		
		for(int i=0; i < word.length()-1; ++i){
			if(temporary_char.isEndOfWord())
				formable = isFormable(word, i+1);
			
			if(formable)
				return true;
			
			if((temporary_char = temporary_char.getChild(word.charAt(i+1))) == null)
					break;
		}
		
		return false;
	}
	
	
	
	// Overridden Methods
	@Override
	public void run() {
		WordNode temporary_node = this.list.getHead();
		
		if(temporary_node == null)
			return;
		
		temporary_node = temporary_node.getPrevious();
		
		do{
			temporary_node = temporary_node.getNext();

			
		}while(temporary_node.getNext() != this.list.getHead());	
		
	}

}
