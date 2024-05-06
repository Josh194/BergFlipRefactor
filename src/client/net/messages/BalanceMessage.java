package client.net.messages;

import client.net.MessageID;

import shared.net.message.Message;

public final class BalanceMessage extends Message {
	@Override
	public int getID() { return MessageID.BALANCE.ordinal(); }

	@Serialize
	public double balance;
}