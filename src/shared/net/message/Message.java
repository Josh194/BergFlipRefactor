package shared.net.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO: support serialization of nested classes
// TODO: document serialized data format
/**
 * Base class for serializable messages. Also provides functions for serialization and deserialization.
 * <p>
 * Unless overridden, serialization and deserialization methods are generated automatically for {@code Message} types.
 * Subclasses must declare a unique ID, and mark any field they wish to be serialized as {@code @Serialize}.
 */
public abstract class Message {
	public class InvalidFieldTypeException extends Exception {}

	@Target(ElementType.FIELD) // TODO: check that this is correct
	@Retention(RetentionPolicy.RUNTIME) // TODO: is there a way to avoid this requirement (ie a templated base function?)
	public @interface Serialize {
		int length() default 0; // fixed size length required for arrays
	}

	// TODO: do field processing at compile-time; ideally, generate unique functions for each `Message` child for performance
	// TODO: should probably try to generalize at least *some* of this logic in the future for reuse in `readFrom()`
	/**
	 * Serializes this {@code Message} instance and writes it into a stream.
	 * 
	 * @param outputStream the stream to write into
	 * @throws IOException if writing to the provided stream causes an {@code IOException}
	 * @throws InvalidFieldTypeException if a field in the calling {@code Message} class was marked as {@code @Serialize}, but is not of a supported type
	 * 
	 * @see #readFrom
	 */
	public void writeTo(DataOutputStream outputStream) throws IOException, InvalidFieldTypeException {
		outputStream.writeInt(getID());

		ArrayList<String> strings = new ArrayList<String>();

		try {
			for (Field field : getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Serialize.class)) {
					// TODO: check if passing `this` is correct here
					if (!field.canAccess(this)) {
						// TODO: would be nice to generate a compile-time warning for this (can add along with annotation processor once implemented)
						continue;
					}

					writeObjectTo(field.get(this), outputStream, strings);
				}
			}
		} catch (IllegalAccessException e) {
			// can't ever be hit, but might as well do this just in case
			e.printStackTrace();
		}

		for (String string : strings) {
			outputStream.writeBytes(string);
		}
	}

	// TODO: improve the interface of this (maybe hold some data in the class instance, or move the logic to a designated input/output stream class)
	private void writeObjectTo(Object obj, DataOutputStream outputStream, ArrayList<String> strings) throws IOException, InvalidFieldTypeException {
		// TODO: this sucks; surely there's a better way?
		// TODO: is this portable?
		switch (obj) { // ? how does field.get() perform for primitives?
		case Integer value: {
			outputStream.writeInt(value);
			break;
		}
		case Short value: {
			outputStream.writeShort(value);
			break;
		}
		case Double value: {
			outputStream.writeDouble(value);
			break;
		}
		case Float value: {
			outputStream.writeFloat(value);
			break;
		}
		case Boolean value: {
			outputStream.writeBoolean(value);
			break;
		}
		case String value: {
			outputStream.writeInt(value.length());
			strings.add(value);
			break;
		}
		default: {
			// TODO: see if we can get this into a matched pattern
			if (obj.getClass().isArray()) {
				for (int i = 0; i < Array.getLength(obj); i++) {
					writeObjectTo(Array.get(obj, i), outputStream, strings);
				}

				break;
			}
			
			//System.out.println("\n\n---\n" + obj.getClass().getTypeName() + "\n---\n\n");

			throw new InvalidFieldTypeException();
		}
		}
	}

	// TODO: maybe see if this can be eliminated
	private class BufferedField {
		public BufferedField(Field field) {
			this.field = field;
		}

		public BufferedField(Object array, int arrayIndex) {
			field = null; // is this actually necessary?
			this.array = array;
			this.arrayIndex = arrayIndex;
		}

		Class<?> getType() {
			return (field != null) ? field.getType() : array.getClass().getComponentType();
		}

		<T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			// TODO: need to check this against nested arrays
			return (field != null) ? field.getAnnotation(annotationClass) : array.getClass().getAnnotation(annotationClass);
		}

		void set(Object obj, Object value) throws IllegalAccessException {
			if (field != null) {
				field.set(obj, value);
			} else {
				Array.set(array, arrayIndex, value);
			}
		}

		public Field field;
		private Object array; // TODO: messy (only necessary for arrays), feels like a silly hack; surely there's a better way
		public int arrayIndex;
		public int length;
	}

	/**
	 * Reads the type ID of the next message in the stream.
	 * Note that as this actually consumes the data, you *cannot* call this method multiple times in a row (unless the message type the previous call to this function returned had a size of zero).
	 * In other words, excluding the aforementioned exception, calls to {@link #readMessageType} and {@link #readFrom} must always be paired.
	 * <p>
	 * A message is only considered valid if the type of the {@code Message} instance used to read and write it is the same.
	 * Information about how message IDs correspond to {@code Message} subclasses must be communicated out-of-band at the programmer's discretion.
	 * <p>
	 * Only the bytes completely necessary for the message are read.
	 * This method assumes the input stream is blocking, and may otherwise throw an exception if not enough bytes are available.
	 * 
	 * @param inputStream the stream to read from
	 * @return the ID of the next message in the stream
	 * @throws IOException if reading from the provided stream causes an {@code IOException}
	 * 
	 * @see #readFrom
	 */
	public static int readMessageType(DataInputStream inputStream) throws IOException {
		return inputStream.readInt();
	}

	// * all comments from `writeTo()` apply here
	/**
	 * Reads and deserializes a single message from the provided stream into this {@code Message} instance. Must have been preceded by a call to {@link #readMessageType}.
	 * <p>
	 * Only the bytes completely necessary for the message are read.
	 * This method assumes the input stream is blocking, and may otherwise throw an exception if not enough bytes are available.
	 * 
	 * @param inputStream the stream to read from
	 * @throws IOException if reading from the provided stream causes an {@code IOException}
	 * @throws InvalidFieldTypeException if a field in the calling {@code Message} class was marked as {@code @Serialize}, but is not of a supported type
	 * 
	 * @see #readMessageType
	 * @see #writeTo
	 */
	public void readFrom(DataInputStream inputStream) throws IOException, InvalidFieldTypeException {
		ArrayList<BufferedField> strings = new ArrayList<BufferedField>();

		try {
			for (Field field : getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Serialize.class)) {
					if (!field.canAccess(this)) {
						continue;
					}

					readObjectFrom(new BufferedField(field), inputStream, strings);
				}
			}

			for (BufferedField field : strings) {
				// ! TODO: genericize this to other array types
				// ! TODO: does this String constructor interpret the read bytes the same way they were written?
				field.set(this, new String(inputStream.readNBytes(field.length)));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// TODO: god please fix this stupid array system
	private void readObjectFrom(BufferedField field, DataInputStream inputStream, ArrayList<BufferedField> strings) throws IOException, IllegalAccessException, InvalidFieldTypeException {
		// TODO: specifying the switch patterns like this also kind of sucks, is there a better way?
		switch (field.getType()) {
		case Class<?> cl when (cl == Integer.class) || (cl == int.class): {
			field.set(this, inputStream.readInt());
			break;
		}
		case Class<?> cl when (cl == Short.class) || (cl == short.class): {
			field.set(this, inputStream.readShort());
			break;
		}
		case Class<?> cl when (cl == Double.class) || (cl == double.class): {
			field.set(this, inputStream.readDouble());
			break;
		}
		case Class<?> cl when (cl == Float.class) || (cl == float.class): {
			field.set(this, inputStream.readFloat());
			break;
		}
		case Class<?> cl when (cl == Boolean.class) || (cl == boolean.class): {
			field.set(this, inputStream.readBoolean());
			break;
		}
		case Class<?> cl when (cl == String.class): {
			field.length = inputStream.readInt();
			strings.add(field);
			break;
		}
		case Class<?> cl when (cl.isArray()): {
			Class<?> arrayType = field.getType();
			
			// TODO: could clean up
			if (field.getAnnotation(Serialize.class) == null) {
				throw new InvalidFieldTypeException(); // ? should we throw something different?
			}

			int arrayLength = field.getAnnotation(Serialize.class).length();
			// ! TODO: why does this work? (the getComponentType() call)
			Object array = Array.newInstance(arrayType.getComponentType(), arrayLength);

			field.set(this, array);

			for (int i = 0; i < arrayLength; i++) {
				readObjectFrom(new BufferedField(array, i), inputStream, strings);
			}

			break;
		}
		default: {
			throw new InvalidFieldTypeException();
		}
		}
	}

	// TODO: is there a better to do this? quick research would suggest no, but the functionality clearly exists (serialVersionUID) though may be limited to the STL.
	// TODO: maybe just compile-time reflection? because as is, this is incredibly lame.
	// if the above is possible, could possibly be worth allowing IDs to be set in an enum definition.
	/**
	 * Returns the ID assigned to this {@code Message} type. Subclasses must coordinate to ensure unique IDs.
	 * 
	 * @return the message type ID
	 */
	public abstract int getID();
}