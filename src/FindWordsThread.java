
public class FindWordsThread implements Runnable{

	
	// Attributes
	private Trie current_trie;
	private Trie[] all_tries;
	//private LinkedList list;
	private WordNode head;
	private LinkedList formable_words;
	
	
	// Constructors
	public FindWordsThread(Trie current_trie, Trie[] all_tries, WordNode head/*LinkedList list*/, LinkedList formable_words){
		this.current_trie = current_trie;
		this.all_tries = all_tries;
		//this.list = list;
		this.head = head;
		this.formable_words = formable_words;
	}
	
	
	// Methods
	private boolean isFormable(String word, int index){
		int char_index = word.charAt(index) - 97;
		boolean formable = false;
		Trie temporary_trie = all_tries[char_index];
		CharNode temporary_char = temporary_trie.getHead();
		
		for(int i=index; i < word.length()-1; ++i){
			if(temporary_char.isEndOfWord()){
				if(!temporary_char.hasChildren())
					return true;
				
				formable = isFormable(word, i+1);
			}
			
			if(formable)
				return true;
			
			if((temporary_char = temporary_char.getChild(word.charAt(i+1))) == null)
				break;
		}
		
		return formable;
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
		
		return formable;
	}
	
	
	
	// Overridden Methods
	@Override
	public void run() {
		WordNode temporary_node = this.head;
		
		if (temporary_node != null) {

			do {
				System.out.println("Current: " + temporary_node);
				System.out.println("   Next: " + temporary_node.getNext());
				System.out.println("   Head: " + this.head);

				if (isFormable(temporary_node.getWord())) {
					formable_words.add(temporary_node);
					if (temporary_node.getWord().length() >= formable_words.getHead().getWord().length())
						formable_words.updateHead(formable_words.getHead().getPrevious());
				}
				System.out.println("Temporary Node: " + temporary_node.getWord());

				temporary_node = temporary_node.getNext();
				
			} while (temporary_node != this.head);
		}
	}

}
