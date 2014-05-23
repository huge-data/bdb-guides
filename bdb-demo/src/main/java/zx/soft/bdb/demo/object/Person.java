package zx.soft.bdb.demo.object;

public class Person {

	int id;
	String name;
	boolean sex;

	public Person() {
		//		/
	}

	public Person(int id, String name, boolean sex) {
		this.id = id;
		this.name = name;
		this.sex = sex;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

}
