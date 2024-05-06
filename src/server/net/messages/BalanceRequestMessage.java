package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class BalanceRequestMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.BALANCE.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.balance(username);

		return Status.NORMAL;
	}

	@Serialize
	public String username;
}
