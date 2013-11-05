
import java.util.ArrayList;


public class CharNode {
	
	
	// Attributes
	private char character;
	private boolean word_end;
	private CharNode parent;
	private ArrayList<CharNode> children;
	
	
	// Constructors
	public CharNode(){
		this.character = '-';
		this.children = new ArrayList<CharNode>();
		this.word_end = false;
	}
	
	public CharNode(char character){
		this.character = character;
		this.children = new ArrayList<CharNode>();
		this.word_end = false;
	}
	
	public CharNode(char character, CharNode parent){
		this.character = character;
		this.parent = parent;
		this.children = new ArrayList<CharNode>();
		this.word_end = false;
	}
	
	
	// Accessors
	public char getChar(){return this.character;}
	
	public CharNode getParent(){return this.parent;}
	
	public ArrayList<CharNode> getChildren(){return this.children;}
	
	public boolean isEndOfWord(){return this.word_end;}
	
	
	// Mutators
	public void updateParent(CharNode parent){
		this.parent = parent;
	}
	
	public void updateEnd(){
		this.word_end = true;
	}
	
	
	// Methods
	public void addChild(CharNode child){
		this.children.add(child);
	}
	
	public boolean hasChildren(){
		return (children.size() != 0);
	}
	
	public CharNode getChild(char child){
		for(CharNode each:children){
			if(child == each.getChar())
				return each;
		}
		
		return null;
	}
	
}
