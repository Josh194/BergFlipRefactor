package client.net.messages;

import client.net.MessageID;

import shared.net.message.Message;

public final class RegisterValidationMessage extends Message {
	@Override
	public int getID() { return MessageID.REGISTER_RESPONSE.ordinal(); }

	public enum ResponseType {
		SUCCESS,
		ERROR_TAKEN,		// username already taken
		ERROR_CREDENTIAL;	// username/password too short

		public static ResponseType ordinals[] = ResponseType.values();
	}

	@Serialize
	public int code;
}
