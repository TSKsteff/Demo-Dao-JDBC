package model.dao;

import java.util.List;

import model.entities.Deparment;

public interface DepartementDao {

	void insert(Deparment obj);
	
	void update(Deparment obj);
	
	void deleteById(Integer id);
	
	Deparment findById(Integer id);
	
	List<Deparment> finall();
}
