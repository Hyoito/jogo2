package models;

public class ChosenWord {

	private int id;
	private String word;

	public ChosenWord(int cId, String cWord) {
		this.id = cId;
		this.word = cWord;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return word;
	}
}