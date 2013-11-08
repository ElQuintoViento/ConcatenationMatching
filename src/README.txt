
Name: Adam D. Thorson
Email: adamdanielthorson@gmail.com
Phone: 715 379 4407

Program:
	Concatenation Matching

Description:
	 Takes in a file that consists of lowercase letters that make up words and/or nonsense.
Each line has one word; in other words, there are no spaces.  The program must find the largest
word or words that can be formed from concatenating smaller words within the list.  It also
returns the whole amount of words that can be formed from smaller words.

Design:
	Initially, the input is brought in line by line; each of these lines/words is
associated with a specific node, WordNode, of a linked list.  These linked lists are based upon
the initial letter(a-z); any word that begins with 'a' will be associated with the linked list
of 'a', et cetera.
	In order that the program doesn't spend costly time looking up every single letter of
the alphabet for the next character in each word(while we are seeing if concatenating smaller
words work to form the given word), we must create tries for each initial letter.  In the long
run, this will pay off due to the fact that we won't be using the whole dictionary, but only
that which is in the list.
	To speed this up even more, the program utilizes multithreading; 26 threads are created
in order to create the 26 tries.  After these tries have been created, 26 more threads are
created to see whether words beginning with their associated letter can be formed.
	Figuring out if a word is formable or not is done in the following manner.  Each word is
processed by its associated thread.  The thread reads character by character whether this
character marks the end of a word.  If so, it branches off and checks to see if the remaining
portion of the word can be made.  If it can't, it goes back to the initial trie to see if the
next character and so on are the end of the word.  Upon reaching the end of the word without
finding another character that is the end of the word(besides the last character), it will
indicate that the word is not formable and hence will not be added to the formable words list.
	As these formable words are added to each associated character list, the head of the
list is either shift to the tail, this newly added word, or stays at its current head position.
The head is changed if the newly added word is of the same or greater length; this ensures
that all of the longest words of that associated beginning character are at the front of the
list, which will make finding the biggest words of each list easier.
	At the end, the largest formable words is doen by adding the largest words of the first
list then comparing with the second and so on.  If the largest words of one list aren't larger
in length than the ones of the largest list, they aren't added to the list; if equal, they are
added; if greater, they are added and the old words are cleared.