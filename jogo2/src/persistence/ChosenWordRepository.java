package persistence;

import models.ChosenWord;

public interface ChosenWordRepository {
	
	public ChosenWord[] findBy(int id);

}