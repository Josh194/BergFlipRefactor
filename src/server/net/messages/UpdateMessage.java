package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class UpdateMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.UPDATE.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.updateBalance(username, balanceNew);

		return Status.NORMAL;
	}

	@Serialize
	public String username;
	
	@Serialize
	public double balanceNew;
}
