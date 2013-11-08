
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConcatenationMatching {

	public static void setupTriesAndLists(LinkedList[] lists, Trie[] tries, LinkedList[] formable_words){
		for(char c=97; c < 123; ++c){
			lists[c-97] = new LinkedList();
			tries[c-97] = new Trie(c);
			formable_words[c-97] = new LinkedList();
		}
	}
	
	// BECOMES FINDWORDSTHREAD
	public static boolean isFormable(WordNode current_node, Trie[] the_tries, int char_index, int word_index){
		Trie current_trie = the_tries[char_index];
		CharNode current_char = current_trie.getHead();
		String current_word = current_node.getWord();
		int word_size = current_word.length();
		boolean formable = false;
		
		for(int i=word_index; i < word_size; ++i){
			if(current_char.isEndOfWord()){
				if(i == word_size-1)
					return (formable = true);
				
				if(i < word_size - 1){
					if((formable = isFormable(current_node, the_tries, (current_word.charAt(i+1)-97), i+1)))
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
	
	// BECOMES FINDWORDSTHREAD
	public static boolean isFormable(WordNode current_node, Trie[] the_tries, int char_index){
		Trie current_trie = the_tries[char_index];
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
					formable = isFormable(current_node, the_tries, (current_word.charAt(i+1)-97), i+1);
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
	
	// BECOMES FINDWORDSTHREAD
	public static void findFormableWords(LinkedList the_words, Trie[] the_tries, LinkedList formable_words, int char_index){
		WordNode current_node;
		
		if((current_node = the_words.getHead()) == null)
			return;
		
		for(int i=0; i < the_words.getSize(); ++i){
			if(isFormable(current_node, the_tries, char_index)){
				formable_words.add(current_node.copy());
				
				// Keep largest words next to each other
				if(current_node.getWord().length() >= formable_words.getHead().getWord().length())
					formable_words.updateHead(formable_words.getHead().getPrevious());
			}
			
			current_node = current_node.getNext();
		}
	}
	
	
	public static void main(String[] args) {
		
		try(BufferedReader buffer_reader = new BufferedReader(new FileReader("C:\\Users\\Thor\\Downloads\\words.txt"))){
			
			//
			int index = -1;
			char current_letter = 97;
			long start_time = System.currentTimeMillis();
			long end_time;
			
			//
			String temporary_string;
			WordNode temporary_node;
			Trie temporary_trie;
			LinkedList temporary_list;
			LinkedList[] alphabetical_lists = new LinkedList[26];
			LinkedList[] formable_words = new LinkedList[26];
			Trie[] alphabetical_tries = new Trie[26];
			Thread[] trie_threads = new Thread[26];
			Thread[] find_threads = new Thread[26];
			
			setupTriesAndLists(alphabetical_lists, alphabetical_tries, formable_words);
			
			while((temporary_string = buffer_reader.readLine()) != null){
				index = temporary_string.charAt(0) - 97;
				
				// Is value from 0 to 25?
				//if(Math.abs(index - 12.5) > 12.5)
				
				// DEBUGGING PURPOSES ONLY
				//System.out.println("Index " + index +": " + temporary_string);
				
				temporary_node = new WordNode(temporary_string);
				
				alphabetical_lists[index].add(temporary_node);
			}
			
			// DEBUGGING PURPOSES ONLY
			/*for(int i = 0; i < 26; ++i){
				System.out.println("\"" + current_letter + "\" list:");
				++current_letter;
				
				temporary_list = alphabetical_lists[i];
				
				if(temporary_list.getSize() == 0)
					continue;
				
				System.out.println("Size: " + temporary_list.getSize());
				for(int j = 0; j < temporary_list.getSize(); ++j){
					System.out.println('\t' + temporary_list.getHead().getWord());
					temporary_list.updateHead(temporary_list.getHead().getNext());
				}
				
				System.out.println();
			}*/
			
			//
			
			for (int i = 0; i < 26; ++i) {
				temporary_node = alphabetical_lists[i].getHead();
				temporary_trie = alphabetical_tries[i];
				
				trie_threads[i] = new Thread(new CreateTrieThread(temporary_node, temporary_trie));

				trie_threads[i].start();
			}
			
			for(int i=0; i < 26; ++i){
				try {
					trie_threads[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			// FIND FORMABLE WORDS
			//-------------------------------------------------------------------------------------------------------------------------------------------------------
			for(int i=0; i < 26; ++i){
				//findFormableWords(alphabetical_lists[i], alphabetical_tries, formable_words[i], i);
				find_threads[i] = new Thread(new FindWordsThread(alphabetical_lists[i], alphabetical_tries, formable_words[i], i));
				
				find_threads[i].start();
			}
			
			for(int i=0; i < 26; ++i){
				try {
					find_threads[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//-------------------------------------------------------------------------------------------------------------------------------------------------------

			
			// DEBUGGING PURPOSES ONLY
			/*for(int i=0; i < 26; ++i){
				WordNode the_head = formable_words[i].getHead();
				WordNode the_next = the_head;
				
				if(the_head != null){
					do{
						System.out.println(the_next.getWord());
						
						the_next = the_next.getNext();
					}while(the_next != the_head);
					System.out.println();
				}
			}*/
			
			LinkedList largest_formable_words = new LinkedList();
			WordNode next_node;
			String temporary_word = "";
			String first_large_word = "";
			int largest_length = 0;
			int formable_word_amount = 0;
			
			for(int i=0; i < 26; ++i){
				if((temporary_node = formable_words[i].getHead()) != null){
					formable_word_amount += formable_words[i].getSize();
					
					next_node = temporary_node.getNext();
					temporary_word = temporary_node.getWord();
				
					// Next word is greater in size than the currently listed longest word
					// Must become the head of the largest words
					// Forget old largest words
					if(temporary_word.length() >  largest_length){
						largest_formable_words.clear();
						largest_formable_words.add(temporary_node.copy());
						largest_length = temporary_word.length();
						
						// Add other ties
						while((next_node != temporary_node) && (next_node.getWord().length() == largest_length)){
							largest_formable_words.add(next_node.copy());
							next_node = next_node.getNext();
						}
						
					}else if(temporary_word.length() == largest_length){
						largest_formable_words.add(temporary_node.copy());
						
						// Add other ties
						while((next_node != temporary_node) && (next_node.getWord().length() == largest_length)){
							largest_formable_words.add(next_node.copy());
							next_node = next_node.getNext();
						}
					}
				}
			}
			
			System.out.print("Longest Word");
			if(largest_formable_words.getSize() > 1)
				System.out.print("s");
			System.out.print(":");
			
			if(largest_formable_words.getSize() > 0){
				next_node = largest_formable_words.getHead();
				
				for(int i = 0; i < largest_formable_words.getSize(); ++i){
					if((i % 5) == 0)
						System.out.print("\n\t");
					System.out.print(next_node.getWord());
					
					// Adding Commas
					if(((i+1) % (largest_formable_words.getSize())) != 0)
						System.out.print(", ");
					
					next_node = next_node.getNext();
				}
				
				System.out.print("\n\nBesides the longest word, " + (formable_word_amount-largest_formable_words.getSize()) + " words can be formed from other words within the list.");
				
			}else{
				System.out.println("\nTHERE ARE NO FORMABLE WORDS\n");
			}
			
			System.out.println('\n');
			
			end_time = System.currentTimeMillis();
			
			end_time = end_time - start_time;
			System.out.println("Time: " + end_time);
			
		}catch(IOException ioe){
			System.err.println("Error: Continue to try!");
		}

	}
	
	
	
}