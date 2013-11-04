
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConcatenationMatching {

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
			Trie[] alphabetical_tries = new Trie[26];
			Thread[] trie_threads = new Thread[26];
			
			
			for(char c = 97; c < 123; ++c){
				alphabetical_lists[c-97] = new LinkedList();
				alphabetical_tries[c-97] = new Trie(c);
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
			/*for(int i = 0; i < 26; ++i){
				System.out.println("\"" + current_letter + "\" list:");
				
				temporary_list = alphabetical_lists[i];
				
				if(temporary_list.getSize() == 0)
					continue;
				
				System.out.println("Size: " + temporary_list.getSize());
				for(int j = 0; j < temporary_list.getSize(); ++j){
					System.out.println('\t' + temporary_list.getHead().getWord());
					temporary_list.updateHead(temporary_list.getHead().getNext());
				}
				
				System.out.println();
				++current_letter;
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
			
			// DEBUGGING PURPOSES ONLY
			//alphabetical_tries[0].listTrie();
			
			end_time = System.currentTimeMillis();
			
			end_time = end_time - start_time;
			System.out.println("Time: " + end_time);
			
		}catch(IOException ioe){
			System.err.println("Error: Continue to try!");
		}

	}

}
