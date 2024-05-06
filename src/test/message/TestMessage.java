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

	@Serialize
	public short testInt2;
}