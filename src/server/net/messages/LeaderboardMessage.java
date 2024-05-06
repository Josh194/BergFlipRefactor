package server.net.messages;

import server.net.ClientContext;
import server.net.MessageID;
import server.net.ServerMessage;

public final class LeaderboardMessage extends ServerMessage {
	@Override
	public int getID() { return MessageID.LEADERBOARD.ordinal(); }

	@Override
	public Status handle(ClientContext context) {
		context.getLeaderboardScores();

		return Status.NORMAL;
	}
}
