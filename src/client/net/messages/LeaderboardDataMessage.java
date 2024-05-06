package client.net.messages;

import client.net.MessageID;

import shared.net.message.Message;

public final class LeaderboardDataMessage extends Message {
	@Override
	public int getID() { return MessageID.LEADERBOARD.ordinal(); }

	@Serialize(length = 6)
	public String data[];
}