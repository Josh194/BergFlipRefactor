package test.util;

public class AssertionBuilder {
	public AssertionBuilder(String msg) {
		this.msg = msg;
	}

	public void testEq(Object x, Object y) {
		if (!x.equals(y)) {
			numFailures++;
			
			System.out.println("[ERROR] test assertion failed! (" + msg + ") -> {\n" + x.toString().indent(4) + "} != {\n" + y.toString().indent(4) + "}");
		}
	}

	public int getNumFailures() {
		return numFailures;
	}

	private String msg;
	private int numFailures = 0;
}
