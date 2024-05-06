package test.message;

import test.util.AssertionBuilder;
import test.util.ByteViewer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import shared.net.message.Message;

public class SerializationTest {
	public static void main(String[] args) {
		TestMessage reference = new TestMessage();

		reference.testBool = true;
		reference.testString = "hello\nworld";
		reference.testInt1 = 5;
		reference.testInt2 = 2048;

		ByteArrayOutputStream writeStream = new ByteArrayOutputStream();

		{
			DataOutputStream os = new DataOutputStream(writeStream);

			try {
				reference.writeTo(os);

				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("[DEBUG] serialized data stream:\n========");

		try {
			ByteViewer viewer = new ByteViewer();
			viewer.write(writeStream.toByteArray());
			viewer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("========");

		int messageId = -1;
		TestMessage message = new TestMessage();

		{
			DataInputStream is = new DataInputStream(new ByteArrayInputStream(writeStream.toByteArray()));

			try {
				messageId = Message.readMessageType(is);
				message.readFrom(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		AssertionBuilder tester = new AssertionBuilder("message field failed verification");

		tester.testEq(messageId, TestMessage.ID); // TODO: should probably improve the assertion system so this isn't just reusing the field checker (in a better manner than just defining a new instance)
		tester.testEq(reference.testBool, message.testBool);
		tester.testEq(reference.testString, message.testString);
		tester.testEq(reference.testInt1, message.testInt1);
		tester.testEq(reference.testInt2, message.testInt2);

		if (tester.getNumFailures() == 0) {
			System.out.println("[SUCCESS] all message fields passed verification");
		}
	}
}
