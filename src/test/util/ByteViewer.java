package test.util;

import java.io.OutputStream;

public class ByteViewer extends OutputStream {
	private static char charTable[];

	static {
		charTable = new char[256];

		for (int i = 0; i < 32; i++) {
			charTable[i] = ' ';
		}

		for (int i = 32; i < 128; i++) {
			charTable[i] = (char) i;
		}

		for (int i = 128; i < 256; i++) {
			charTable[i] = ' ';
		}
	}

	@Override
	public void write(int value) {
		value &= 0xFF;
		System.out.println("byte: " + '\'' + charTable[value] + "\' " + value);
	}
}