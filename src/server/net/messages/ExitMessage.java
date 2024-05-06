package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class ExitMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.EXIT.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.close();

		return Status.EXIT;
	}
}
