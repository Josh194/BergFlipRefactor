package client.net.messages;

import client.net.MessageID;

import shared.net.message.Message;

public final class LoginValidationMessage extends Message {
	@Override
	public int getID() { return MessageID.LOGIN_RESPONSE.ordinal(); }

	@Serialize
	public boolean isValid;
}
