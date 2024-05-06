package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class RegisterMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.REGISTER.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.registerUser(username, password);

		return Status.NORMAL;
	}

	@Serialize
	public String username, password;
}
