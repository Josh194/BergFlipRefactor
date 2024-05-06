package client.net.messages;

import client.net.MessageID;

import shared.net.message.Message;

public final class RegisterValidationMessage extends Message {
	@Override
	public int getID() { return MessageID.REGISTER_RESPONSE.ordinal(); }

	@Serialize
	public String response;
}
