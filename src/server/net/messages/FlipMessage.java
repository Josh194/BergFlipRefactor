package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class FlipMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.FLIP.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.flipCoin(predictedResult, bet);

		return Status.NORMAL;
	}

	@Serialize
	public String predictedResult, bet;
}
