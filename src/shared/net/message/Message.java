package shared.net.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO: support serialization of nested classes
// * abstract to prevent instantiation (as should never be necessary), but could be easily removed in the future if needed
public abstract class Message {
	class InvalidFieldTypeException extends Exception {}

	@Target(ElementType.FIELD) // TODO: check that this is correct
	@Retention(RetentionPolicy.RUNTIME) // TODO: is there a way to avoid this requirement (ie a templated base function?)
	public @interface Serialize {}

	// TODO: do field processing at compile-time; ideally, generate unique functions for each `Message` child for performance
	// TODO: should probably try to generalize at least *some* of this logic in the future for reuse in `readFrom()`
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

					// TODO: this sucks; surely there's a better way?
					// TODO: is this portable?
					switch (field.get(this)) { // ? how does field.get() perform for primitives?
					case Integer value: {
						outputStream.writeInt(value);
						break;
					}
					case Short value: {
						outputStream.writeShort(value);
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
						throw new InvalidFieldTypeException();
					}
					}
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

	// TODO: maybe see if this can be eliminated
	private class BufferedField {
		public BufferedField(Field field, int length) {
			this.field = field;
			this.length = length;
		}

		public Field field;
		public int length;
	}

	// * all comments from `writeTo()` apply here
	public void readFrom(DataInputStream inputStream) throws IOException, InvalidFieldTypeException {
		int id = inputStream.readInt();

		ArrayList<BufferedField> strings = new ArrayList<BufferedField>();

		try {
			for (Field field : getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Serialize.class)) {
					if (!field.canAccess(this)) {
						continue;
					}

					// TODO: specifying the switch patterns like this also kind of sucks, is there a better way?
					switch (field.getType()) {
					case Class<?> cl when (cl == Integer.class) || (cl == int.class): {
						field.setInt(this, inputStream.readInt());
						break;
					}
					case Class<?> cl when (cl == Short.class) || (cl == short.class): {
						field.setShort(this, inputStream.readShort());
						break;
					}
					case Class<?> cl when (cl == Boolean.class) || (cl == boolean.class): {
						field.setBoolean(this, inputStream.readBoolean());
						break;
					}
					case Class<?> cl when (cl == String.class): {
						strings.add(new BufferedField(field, inputStream.readInt()));
						break;
					}
					default: {
						throw new InvalidFieldTypeException();
					}
					}
				}
			}

			for (BufferedField field : strings) {
				// ! TODO: does this String constructor interpret the read bytes the same way they were written?
				field.field.set(this, new String(inputStream.readNBytes(field.length)));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// TODO: is there a better to do this? quick research would suggest no, but the functionality clearly exists (serialVersionUID) though may be limited to the STL.
	// TODO: maybe just compile-time reflection? because as is, this is incredibly lame.
	// if the above is possible, could possibly be worth allowing IDs to be set in an enum definition.
	public abstract int getID();
}