package client.net.messages;

import client.net.MessageID;

import shared.net.message.Message;

public final class PasswordChangeValidation extends Message {
	@Override
	public int getID() { return MessageID.PASSWORD_CHANGE_RESPONSE.ordinal(); }

	@Serialize
	public String response;
}
