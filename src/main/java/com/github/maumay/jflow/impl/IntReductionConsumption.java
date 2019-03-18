/**
 *
 */
package com.github.maumay.jflow.impl;

import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;

import com.github.maumay.jflow.utils.Exceptions;

/**
 * @author ThomasB
 */
public final class IntReductionConsumption
{
	private IntReductionConsumption()
	{
	}

	public static OptionalInt foldOption(AbstractIntIterator source, IntBinaryOperator reducer)
	{
		Exceptions.require(source.hasOwnership());
		boolean reductionUninitialised = true;
		int reduction = -1;
		while (source.hasNext()) {
			if (reductionUninitialised) {
				reduction = source.nextIntImpl();
				reductionUninitialised = false;
			} else {
				reduction = reducer.applyAsInt(reduction, source.nextIntImpl());
			}
		}
		return reductionUninitialised ? OptionalInt.empty() : OptionalInt.of(reduction);
	}

	public static int fold(AbstractIntIterator source, IntBinaryOperator reducer)
	{
		Exceptions.require(source.hasOwnership());
		boolean reductionUninitialised = true;
		int reduction = -1;
		while (source.hasNext()) {
			if (reductionUninitialised) {
				reduction = source.nextIntImpl();
				reductionUninitialised = false;
			} else {
				reduction = reducer.applyAsInt(reduction, source.nextIntImpl());
			}
		}
		if (reductionUninitialised) {
			throw new NoSuchElementException("Attempted fold on empty iterator");
		} else {
			return reduction;
		}
	}

	public static int fold(AbstractIntIterator source, int id, IntBinaryOperator reducer)
	{
		Exceptions.require(source.hasOwnership());
		int reduction = id;
		while (source.hasNext()) {
			reduction = reducer.applyAsInt(reduction, source.nextIntImpl());
		}
		return reduction;
	}

	public static long count(AbstractIntIterator source)
	{
		Exceptions.require(source.hasOwnership());
		int count = 0;
		while (source.hasNext()) {
			source.skipImpl();
			count++;
		}
		return count;
	}
}