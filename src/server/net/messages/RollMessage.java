package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class RollMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.ROLL.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.rollDie(predictedRoll, bet);

		return Status.NORMAL;
	}

	@Serialize
	public int predictedRoll;
	
	@Serialize
	public int bet;
}
