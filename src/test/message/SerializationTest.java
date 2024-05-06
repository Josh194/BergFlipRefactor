package test.message;

import test.util.AssertionBuilder;
import test.util.ByteViewer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

import shared.net.message.Message;

public class SerializationTest {
	public static void main(String[] args) {
		TestMessage reference = new TestMessage();

		reference.testBool = true;
		reference.testString = "hello\nworld";
		reference.testInt1 = 5;
		reference.testStringArray = new String[] {"hi", "how", "are", "you?"};
		reference.testIntArray = new int[] {2, 3, 4};
		reference.testDouble = 2.77d;
		reference.testInt2 = 2048;

		ByteArrayOutputStream writeStream = new ByteArrayOutputStream();

		{
			Message genericReference = reference;

			DataOutputStream os = new DataOutputStream(writeStream);

			try {
				genericReference.writeTo(os);

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
			Message genericReference = message;

			DataInputStream is = new DataInputStream(new ByteArrayInputStream(writeStream.toByteArray()));

			try {
				messageId = Message.readMessageType(is);
				genericReference.readFrom(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		AssertionBuilder tester = new AssertionBuilder("message field failed verification");

		tester.testEq(messageId, TestMessage.ID); // TODO: should probably improve the assertion system so this isn't just reusing the field checker (in a better manner than just defining a new instance)
		tester.testEq(reference.testBool, message.testBool);
		tester.testEq(reference.testString, message.testString);
		tester.testEq(reference.testInt1, message.testInt1);
		tester.testEq(Arrays.equals(reference.testStringArray, message.testStringArray), true);
		tester.testEq(Arrays.equals(reference.testIntArray, message.testIntArray), true);
		tester.testEq(reference.testDouble, message.testDouble);
		tester.testEq(reference.testInt2, message.testInt2);

		if (tester.getNumFailures() == 0) {
			System.out.println("[SUCCESS] all message fields passed verification");
		}
	}
}
