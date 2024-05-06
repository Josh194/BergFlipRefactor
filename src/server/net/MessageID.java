package server.net;

public enum MessageID {
	EXIT,
	LOGIN,
	REGISTER,
	CHANGE_PASSWORD,
	FLIP,
	BALANCE,
	UPDATE,
	LEADERBOARD,
	ROLL;

	// ? this is guaranteed to only run after MessageID has already been initialized... right?
	public static MessageID ordinals[] = MessageID.values();
}
