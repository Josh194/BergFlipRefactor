package server.net;

import shared.net.message.Message;

public abstract class ServerMessage extends Message {
	public enum Status {
		NORMAL,
		EXIT
	}

	// TODO: should at least consider decoupling `ClientContext` from the actual client thread, as well as any other purely server-management tasks
	public abstract Status handle(ClientContext context);
}
