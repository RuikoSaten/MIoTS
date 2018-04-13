package cn.edu.dgut.dao;

public abstract class Dao {
	public abstract int insert(Object obj);
	public abstract int update(Object obj);
	public abstract int delete(Object obj);
	public abstract int select(Object obj);
}
