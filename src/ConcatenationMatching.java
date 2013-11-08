
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConcatenationMatching {

	
	
	public static void setupTriesAndLists(LinkedList[] lists, Trie[] tries, LinkedList[] formable_words){
		for(char c=97; c < 123; ++c){
			lists[c-97] = new LinkedList();
			tries[c-97] = new Trie(c);
			formable_words[c-97] = new LinkedList();
		}
	}
	
	
	
	public static void collectInput(BufferedReader buffer_reader, LinkedList[] lists) throws IOException{
		String temporary_string;
		int index = 0;
		WordNode temporary_node;
		
		while((temporary_string = buffer_reader.readLine()) != null){
			index = temporary_string.charAt(0) - 97;
			
			temporary_node = new WordNode(temporary_string);
			
			lists[index].add(temporary_node);
		}
	}
	
	
	
	public static void createTries(LinkedList[] lists, Trie[] tries){
		Thread[] trie_threads = new Thread[26];
		WordNode temporary_node;
		Trie temporary_trie;
		
		for (int i = 0; i < 26; ++i) {
			temporary_node = lists[i].getHead();
			temporary_trie = tries[i];
			
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
	}
	
	
	
	public static void findFormableWords(LinkedList[] lists, Trie[] tries, LinkedList[] formable_words){
		Thread[] find_threads = new Thread[26];
		
		for(int i=0; i < 26; ++i){
			find_threads[i] = new Thread(new FindWordsThread(lists[i], tries, formable_words[i], i));
			
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
	}
	
	
	
	public static int groupLargestWordOrWords(LinkedList[] formable_words, LinkedList largest_formable_words){
		int formable_word_amount = 0;
		int largest_length = 0;
		
		String temporary_word;
		
		WordNode next_node;
		WordNode temporary_node;
		
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
		
		return formable_word_amount;
	}
	
	
	
	public static void outputResults(LinkedList largest_formable_words, int formable_word_amount){
		WordNode next_node;
		
		System.out.print("\nLongest Word");
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
			System.out.println("\nNo words could be formed by permutations of other smaller words.\n");
		}
		
		System.out.println('\n');
	}
	
	
	
	public static void displayIOError(String io_message){
		System.err.println("\nError: Faulty Input Was Provided Or General IO Issues Have Occurred\n" +
				"Please Read Error Message To Have A Better Understanding Of The Error:\n"+
				"\n\tMessage:  " + io_message + '\n');
	}
	
	
	
	public static void main(String[] args) {
		
		try(BufferedReader buffer_reader = new BufferedReader(new FileReader("C:\\Users\\Thor\\Downloads\\words.txt"))){
			
			int index = -1;
			int largest_length = 0;
			int formable_word_amount = 0;
			
			LinkedList largest_formable_words = new LinkedList();
			LinkedList temporary_list;
			LinkedList[] alphabetical_lists = new LinkedList[26];
			LinkedList[] formable_words = new LinkedList[26];
			
			String first_large_word = "";
			String temporary_string;
			String temporary_word = "";
			
			
			Trie temporary_trie;
			Trie[] alphabetical_tries = new Trie[26];
			
			WordNode next_node;
			WordNode temporary_node;
			
			
			setupTriesAndLists(alphabetical_lists, alphabetical_tries, formable_words);
			
			collectInput(buffer_reader, alphabetical_lists);
			
			createTries(alphabetical_lists, alphabetical_tries);
			
			findFormableWords(alphabetical_lists, alphabetical_tries, formable_words);
			
			// Find the total amount of formable words from all the lists
			formable_word_amount = groupLargestWordOrWords(formable_words, largest_formable_words);
			
			outputResults(largest_formable_words, formable_word_amount);
			
		// IO Issues have occurred; possibly due to bad input.
		// Output exception message.
		}catch(IOException ioe){
			displayIOError(ioe.getMessage());
		}

	}
}