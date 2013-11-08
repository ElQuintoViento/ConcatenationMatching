
public class FindWordsThread implements Runnable{

	
	// Attributes
	private LinkedList the_words;
	private LinkedList formable_words;
	private Trie[] the_tries;
	private int char_index;
	
	
	// Constructors
	public FindWordsThread(LinkedList the_words, Trie[] the_tries, LinkedList formable_words, int char_index){
		this.the_words = the_words;
		this.the_tries = the_tries;
		this.formable_words = formable_words;
		this.char_index = char_index;
	}
	
	
	// Methods
	private boolean isFormable(WordNode current_node, int char_index, int word_index){
		Trie current_trie = this.the_tries[char_index];
		CharNode current_char = current_trie.getHead();
		String current_word = current_node.getWord();
		int word_size = current_word.length();
		boolean formable = false;
		
		for(int i=word_index; i < word_size; ++i){
			if(current_char.isEndOfWord()){
				if(i == word_size-1)
					return (formable = true);
				
				if(i < word_size - 1){
					if((formable = isFormable(current_node, (current_word.charAt(i+1)-97), i+1)))
						return formable;
				}
			}
			
			if(i < word_size - 1){
				if((current_char = current_char.getChild(current_word.charAt(i+1))) == null)
					break;
			}
		}
		
		return formable;
	}
	
	
	private boolean isFormable(WordNode current_node){
		Trie current_trie = this.the_tries[this.char_index];
		CharNode current_char = current_trie.getHead();
		String current_word = current_node.getWord();
		int word_size = current_word.length();
		boolean formable = false;
		
		for(int i=0; i < word_size; ++i){
			// LAST LETTER
			if(current_char.isEndOfWord()){
				if(i >= word_size-1)
					break;
				
				if(i < word_size-1){
					formable = isFormable(current_node, (current_word.charAt(i+1)-97), i+1);
				}else{
					break;
				}
				
				if(formable)
					break;
			}
			
			// LAST LETTER
			if(i < word_size-1){
				if((current_char = current_char.getChild(current_word.charAt(i+1))) == null)
					break;
			}
		}
		
		return formable;
	}
	
	
	
	// Overridden Methods
	@Override
	public void run() {
		WordNode current_node;
		
		if((current_node = this.the_words.getHead()) == null)
			return;
		
		for(int i=0; i < this.the_words.getSize(); ++i){
			if(isFormable(current_node)){
				this.formable_words.add(current_node.copy());
				
				// Keep largest words next to each other
				if(current_node.getWord().length() >= this.formable_words.getHead().getWord().length())
					this.formable_words.updateHead(this.formable_words.getHead().getPrevious());
			}
			
			current_node = current_node.getNext();
		}
	}

}
