package org.eclipse.recommenders.jayes.util;

import java.util.Arrays;

public class FloatArrayWrapper implements ArrayWrapper {

	@Override
	public boolean equals(Object obj) {
		return obj instanceof FloatArrayWrapper &&
		Arrays.equals(array, ((FloatArrayWrapper)obj).array);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(array);
	}

	private float[] array;
	
	public FloatArrayWrapper(float[] array){
		this.array = array;
	}
	
	@Override
	public void set(double[] array) {
		set(ArrayUtils.toFloatArray(array));

	}

	@Override
	public void set(float[] array) {
		this.array = array;

	}

	@Override
	public double[] getDouble() {
		return ArrayUtils.toDoubleArray(array);
	}

	@Override
	public float[] getFloat() {
		return array;
	}

	@Override
	public void assign(int index, double d) {
		array[index] = (float)d;
	}

	@Override
	public void assign(int index, float d) {
		array[index] = d;
	}

	@Override
	public void mulAssign(int index, double d) {
		array[index] *= (float)d;
	}

	@Override
	public void mulAssign(int index, float d) {
		array[index] *= d;
	}

	@Override
	public void mulAssign(int index, ArrayWrapper arg, int argIndex) {
		array[index] *= arg.getFloat(argIndex);
	}

	@Override
	public void addAssign(int index, double d) {
		array[index] += (float) d;
	}

	@Override
	public void addAssign(int index, float d) {
		array[index] += d;
	}

	@Override
	public void addAssign(int index, ArrayWrapper arg, int argIndex) {
		array[index] += arg.getFloat(argIndex);
	}

	@Override
	public double getDouble(int index) {
		return (double) array[index];
	}

	@Override
	public float getFloat(int index) {
		return array[index];
	}

	@Override
	public int length() {
		return array.length;
	}

	@Override
	public void copy(double[] array) {
		set(array);
	}

	@Override
	public void copy(float[] array) {
		this.array = array.clone();
	}

	@Override
	public void copy(ArrayWrapper array) {
		copy(array.getFloat());
	}

	@Override
	public void fill(double d) {
		Arrays.fill(array, (float)d);
	}

	@Override
	public void fill(float d) {
		Arrays.fill(array,d);
	}

	@Override
	public void arrayCopy(ArrayWrapper src, int srcOffset, int destOffset,
			int length) {
		System.arraycopy(src.getFloat(), srcOffset, array, destOffset, length);
	}

	@Override
	public void newArray(int capacity) {
		array = new float[capacity];
	}
	
	@Override
	public FloatArrayWrapper clone(){
		FloatArrayWrapper clone;
		try {
			clone = (FloatArrayWrapper) super.clone();
			clone.array = array.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			// should not happen
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int sizeOfElement() {
		return 4;
	}
}