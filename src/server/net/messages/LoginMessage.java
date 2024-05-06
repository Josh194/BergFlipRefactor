package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class LoginMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.LOGIN.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.loginUser(username, password);

		return Status.NORMAL;
	}

	@Serialize
	public String username, password;
}