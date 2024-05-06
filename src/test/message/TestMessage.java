package test.message;

import shared.net.message.Message;

public final class TestMessage extends Message {
	public static final int ID = 17;

	@Override
	public int getID() { return ID; }

	@Serialize
	public boolean testBool;

	@Serialize
	public String testString;

	@Serialize
	public int testInt1;

	@Serialize(length = 4)
	public String testStringArray[];

	@Serialize(length = 3)
	public int testIntArray[];

	@Serialize
	public short testInt2;
}