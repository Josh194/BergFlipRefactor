package client.net.messages;

import client.net.MessageID;

import shared.net.message.Message;

public final class PayoutMessage extends Message {
	@Override
	public int getID() { return MessageID.PAYOUT.ordinal(); }

	@Serialize
	public double payout;
}
