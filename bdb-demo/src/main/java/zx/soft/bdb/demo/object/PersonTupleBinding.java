package zx.soft.bdb.demo.object;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class PersonTupleBinding extends TupleBinding<Person> {

	@Override
	public Person entryToObject(TupleInput input) {
		Person p = new Person();
		p.setId(input.readInt());
		p.setName(input.readString());
		p.setSex(input.readBoolean());
		return p;
	}

	@Override
	public void objectToEntry(Person p, TupleOutput output) {
		output.writeInt(p.getId());
		output.writeString(p.getName());
		output.writeBoolean(p.isSex());
	}

}
