package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class ChangePasswordMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.CHANGE_PASSWORD.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.changeUserPassword(username, passwordOld, passwordNew);

		return Status.NORMAL;
	}

	@Serialize
	public String username, passwordOld, passwordNew;
}