
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConcatenationMatching {

	public static void main(String[] args) {
		
		try(BufferedReader buffer_reader = new BufferedReader(new FileReader("C:\\Users\\Thor\\Downloads\\words2.txt"))){
			
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
			
			
			for(char c = 97; c < 123; ++c){
				alphabetical_lists[c-97] = new LinkedList();
				alphabetical_tries[c-97] = new Trie(c);
				formable_words[c-97] = new LinkedList();
			}
			
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
			for(int i = 0; i < 26; ++i){
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
			}
			
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
			
			//System.out.println("\"abashment\": " + temporary_node);
			//System.out.println("Next Node: " + temporary_node.getNext());
			
			// DEBUGGING PURPOSES ONLY
			//alphabetical_tries[0].listTrie();
			//temporary_node = alphabetical_lists[0].getHead();
			/*if(temporary_node != null){
				do{
					System.out.println(temporary_node.getWord());
					temporary_node = temporary_node.getNext();
				}while(temporary_node != alphabetical_lists[0].getHead());
			}*/
			
			//-----------------------------------------------------------------------------------------------------------------------------------
			for(int i=0; i < 26; ++i){
				//System.out.println("Find Thread #" + i);
				temporary_node = alphabetical_lists[i].getHead();
				temporary_trie = alphabetical_tries[i];
				
				debuggingThread(temporary_trie, alphabetical_tries, alphabetical_lists[i], formable_words[i]);
				//find_threads[i] = new Thread(new FindWordsThread(temporary_trie, alphabetical_tries, alphabetical_lists[i], formable_words[i]));
				
				//find_threads[i].start();
			}
			
			/*for(int i=0; i < 26; ++i){
				try {
					find_threads[i].join();
					//System.out.println("Joined Find Thread #" + i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			//-------------------------------------------------------------------------------------------------------------------------------------
			
			// DEBUGGING PURPOSES ONLY
			for(int i=0; i < 26; ++i){
				WordNode the_head = formable_words[i].getHead();
				WordNode the_next = the_head;
				
				if(the_head != null){
					do{
						System.out.println(the_next.getWord());
						
						the_next = the_next.getNext();
					}while(the_next != the_head);
					System.out.println();
				}
			}
			
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
						System.out.println('\t');
					System.out.print(next_node.getWord());
					
					// Adding Commas
					if(((i+1) % (largest_formable_words.getSize() + 1)) != 0)
						System.out.print(", ");
					
					next_node = next_node.getNext();
				}
			}else{
				System.out.println("\nTHERE ARE NO FORMABLE WORDS\n");
			}
			
			end_time = System.currentTimeMillis();
			
			end_time = end_time - start_time;
			System.out.println("Time: " + end_time);
			
		}catch(IOException ioe){
			System.err.println("Error: Continue to try!");
		}

	}
	
	
	
	
	// SHOULD NOT BE HERE
	// ONLY FOR DEBUGGING
	public static void debuggingThread(Trie trie, Trie[] tries, LinkedList list, LinkedList formable_words){
		WordNode temporary_node = list.getHead();
		WordNode new_node;
		WordNode head = temporary_node;
		
		if (temporary_node != null) {

			do {
				//System.out.println("Current: " + temporary_node);
				//System.out.println("   Next: " + temporary_node.getNext());
				//System.out.println("   Head: " + this.head);
				//System.out.println(" L Size: " + this.list.getSize());
				//System.out.println(" W Size: " + this.formable_words.getSize());

				if (isFormable(temporary_node.getWord(), trie, tries)) {
					formable_words.add(temporary_node.copy());
					if (temporary_node.getWord().length() >= formable_words.getHead().getWord().length())
						formable_words.updateHead(formable_words.getHead().getPrevious());
				}
				//System.out.println("Temporary Node: " + temporary_node.getWord());

				temporary_node = temporary_node.getNext();
				
			} while (temporary_node != head);
		}
	}
	
	// ONLY FOR DEBUGGING
	private static boolean isFormable(String word, Trie trie, Trie[] all_tries){
		CharNode temporary_char;
		boolean formable = false;
		
		if(word.length() <= 1)
			return false;
		
		temporary_char = trie.getHead();
		
		for(int i=0; i < word.length()-1; ++i){
			if(temporary_char.isEndOfWord())
				formable = isFormable(word, i+1, all_tries);
			
			if(formable)
				return true;
			
			if((temporary_char = temporary_char.getChild(word.charAt(i+1))) == null)
					break;
		}
		
		return formable;
	}
	
	// ONLY FOR DEBUGGING
	private static boolean isFormable(String word, int index, Trie[] all_tries){
		int char_index = word.charAt(index) - 97;
		boolean formable = false;
		Trie temporary_trie = all_tries[char_index];
		CharNode temporary_char = temporary_trie.getHead();
		
		for(int i=index; i < word.length()-1; ++i){
			if(temporary_char.isEndOfWord()){
				if(!temporary_char.hasChildren())
					return true;
				
				formable = isFormable(word, i+1, all_tries);
			}
			
			if(formable)
				return true;
			
			if((temporary_char = temporary_char.getChild(word.charAt(i+1))) == null)
				break;
		}
		
		return formable;
	}

}
