package test.message;

import shared.net.message.Message;

public final class TestMessage extends Message {
	@Override
	public int getID() { return 0; }

	@Serialize
	public boolean testBool;

	@Serialize
	public String testString;

	@Serialize
	public int testInt1;

	@Serialize
	public short testInt2;
}