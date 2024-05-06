package client.net;

public enum MessageID {
	BALANCE,
	PAYOUT,
	LOGIN_RESPONSE,
	REGISTER_RESPONSE,
	PASSWORD_CHANGE_RESPONSE,
	LEADERBOARD;

	public static MessageID ordinals[] = MessageID.values();
}

