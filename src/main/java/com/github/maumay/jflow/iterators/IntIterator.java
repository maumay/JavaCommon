package com.github.maumay.jflow.iterators;

import java.util.Iterator;
import java.util.Map;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

import com.github.maumay.jflow.iterators.factories.Iter;
import com.github.maumay.jflow.utils.IntTup;
import com.github.maumay.jflow.utils.IntWith;
import com.github.maumay.jflow.vec.IntVec;

/**
 * <p>
 * An extension of {@link PrimitiveIterator.OfInt} with a multitude of extra
 * methods for piping and transforming sequential data streams. There are many
 * static factory methods for constructing instances of this interface in the
 * {@link Iter} class.
 * </p>
 * <p>
 * In general a good rule of thumb when using iterators is to avoid consuming
 * them with the {@link #nextInt()} method unless it is in the standard while
 * loop idiom. The consumption methods provided in this interface are usually
 * far more useful in general (unless you enjoy writing while loops and using
 * mutable collections in which case this library isn't for you).
 * </p>
 *
 * @author ThomasB
 * @since 20 Apr 2018
 */
public interface IntIterator extends SafeIntIterator
{
	/**
	 * Applies a function elementwise to this {@link IntIterator} to make new
	 * {@link IntIterator}.
	 *
	 * @param f A mapping function.
	 * @return A new {@link IntIterator} instance whose elements are obtained by
	 *         applying the parameter mapping function to each element of this
	 *         {@link IntIterator} instance in turn.
	 */
	IntIterator map(IntUnaryOperator f);

	/**
	 * Applies a function elementwise to this {@link IntIterator} to make new
	 * Iterator.
	 *
	 * @param   <E> The target type of the mapping function.
	 * @param f A mapping function.
	 * @return A new Iterator instance whose elements are obtained by applying the
	 *         parameter mapping function to each element of this
	 *         {@link IntIterator} instance in turn.
	 */
	<E> EnhancedIterator<E> mapToObject(IntFunction<? extends E> f);

	/**
	 * Applies a function elementwise to this {@link IntIterator} to make a new
	 * {@link DoubleIterator}.
	 *
	 * @param f A mapping function.
	 * @return A new {@link DoubleIterator} instance whose elements are obtained by
	 *         applying the parameter mapping function to each element of this
	 *         {@link IntIterator} instance in turn.
	 */
	DoubleIterator mapToDouble(IntToDoubleFunction f);

	/**
	 * Applies a function elementwise to this {@link IntIterator} to make new
	 * {@link LongIterator}.
	 *
	 * @param f A mapping function.
	 * @return A new {@link LongIterator} instance whose elements are obtained by
	 *         applying the parameter mapping function to each element of this
	 *         {@link IntIterator} instance in turn.
	 */
	LongIterator mapToLong(IntToLongFunction f);

	/**
	 * Combines this {@link IntIterator} with an iterator to create a new Iterator
	 * consisting of pairs of elements with the same index in their respective
	 * origins.
	 *
	 * @param       <E> The upper type bound on the parameter iterator.
	 * @param other The Iterator to zip this source {@link IntIterator} with.
	 *
	 * @return Denote this source {@link IntIterator} by {@code F} with the
	 *         parameter iterator denoted by {@code I}. We return a new Iterator
	 *         instance {@code G} defined by:
	 *         <ul>
	 *         <li>{@code G[j] = (F[j], I[j])}</li>
	 *         <li>{@code length(G) = min(length(F), length(I))}</li>
	 *         </ul>
	 */
	<E> EnhancedIterator<IntWith<E>> zipWith(Iterator<? extends E> other);

	/**
	 * Combines this {@link IntIterator} with another primitive iterator to create a
	 * new Iterator consisting of pairs of elements with the same index in their
	 * respective origins.
	 *
	 * @param other The primitive iterator to zip this source {@link IntIterator}
	 *              with.
	 *
	 * @return Denote this source {@link IntIterator} by {@code F} with the
	 *         parameter primitive iterator denoted by {@code I}. We return a new
	 *         Iterator instance {@code G} defined by:
	 *         <ul>
	 *         <li>{@code G[j] = (F[j], I[j])}</li>
	 *         <li>{@code length(G) = min(length(F), length(I))}</li>
	 *         </ul>
	 */
	EnhancedIterator<IntTup> zipWith(PrimitiveIterator.OfInt other);

	/**
	 * Creates a new Iterator by mapping each element in this source
	 * {@link IntIterator} to a pair consisting of the element and the index it
	 * appears.
	 *
	 * @return Denote this source {@link IntIterator} by {@code F}. We return a new
	 *         Iterator instance {@code G} defined by:
	 *         <ul>
	 *         <li>{@code G[j] = (F[j], j)}</li>
	 *         <li>{@code length(G) = length(F)}</li>
	 *         </ul>
	 */
	EnhancedIterator<IntTup> enumerate();

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by selecting
	 * elements with indices defined by the parameter index mapping.
	 *
	 * @param indexMap A strictly monotonically increasing function
	 *                 {@code f: N -> N}
	 * @return Let {@code F} denote this source {@link IntIterator}, let
	 *         {@code n = length(F)} and denote the indexMap by {@code f}. Then this
	 *         method returns a {@link LongIterator} {@code G} given by:
	 *         <ul>
	 *         <li>{@code G[i] = F(f(i))}</li>
	 *         <li><code> length(G) = supremum {i | (i in N) and (f(i) &lt; length(F))} </code></li>
	 *         </ul>
	 */
	IntIterator slice(IntUnaryOperator indexMap);

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by selecting
	 * the first n elements.
	 *
	 * @param n A non-negative integer.
	 * @throws IllegalArgumentException If parameter is negative.
	 * @return Let {@code F} denote this source {@link IntIterator}. We return a
	 *         {@link IntIterator} consisting of the first {@code max(n, length(F))}
	 *         elements of {@code F}.
	 */
	IntIterator take(int n);

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by selecting
	 * elements until an element fails the supplied test, the first failure is not
	 * selected.
	 *
	 * @param predicate A IntPredicate.
	 * @return Let {@code n} be the index of the first element that the parameter
	 *         predicate fails for. Then this method returns a {@link IntIterator}
	 *         consisting of the first {@code n} elements of this source
	 *         {@link IntIterator}. If no element fails the predicate test then a
	 *         copy of the source is returned.
	 */
	IntIterator takeWhile(IntPredicate predicate);

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by removing
	 * the first n elements.
	 *
	 * @param n A non-negative integer.
	 * @throws IllegalArgumentException If parameter is negative.
	 * @return Let {@code F} denote this source {@link IntIterator}. We return a
	 *         {@link IntIterator} missing the first {@code min(n, length(F))}
	 *         elements of {@code F}.
	 */
	IntIterator skip(int n);

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by removing
	 * elements until an element fails the supplied test, the first failure is the
	 * first element of the result.
	 *
	 * @param predicate An IntPredicate.
	 * @return Let {@code n} be the index of the first element that the parameter
	 *         predicate fails for. Then this method returns a {@link IntIterator}
	 *         missing {@code n} elements of this source {@link IntIterator}. If no
	 *         element fails the predicate test then a copy of the source is
	 *         returned.
	 */
	IntIterator skipWhile(IntPredicate predicate);

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by removing
	 * any element which fails the supplied predicate test.
	 *
	 * @param predicate A IntPredicate.
	 * @return An {@link IntIterator} containing only those elements of this source
	 *         {@link IntIterator} which pass the test defined by the parameter
	 *         predicate. The relative ordering of elements is retained.
	 */
	IntIterator filter(IntPredicate predicate);

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by adding
	 * each element of the supplied primitive iterator to its end in order.
	 *
	 * @param other A primitive iterator.
	 * @return An {@link IntIterator} consisting of the elements of this source
	 *         {@link IntIterator} followed by the elements of the parameter
	 *         primitive iterator.
	 */
	IntIterator append(PrimitiveIterator.OfInt other);

	/**
	 * Creates a new {@link IntIterator} from this {@link IntIterator} by adding
	 * each element of the supplied varargs array to its end in order.
	 *
	 * @param other - A varargs int array
	 * @return An {@link IntIterator} consisting of the elements of the source
	 *         {@link IntIterator} followed by the elements in the parameter array.
	 */
	IntIterator append(int... other);

	/**
	 * Creates a new {@link IntIterator} from this Iterator by adding each element
	 * to the end of the supplied primitive iterator in order.
	 *
	 * @param other A primitive iterator.
	 * @return a {@link IntIterator} consisting of the elements of the parameter
	 *         primitive iterator followed by the elements of this source
	 *         {@link IntIterator}.
	 */
	IntIterator insert(PrimitiveIterator.OfInt other);

	/**
	 * Creates a new {@link IntIterator} from this Iterator by adding each element
	 * to the end of the supplied varargs array in order.
	 *
	 * @param other - A varargs int array
	 * @return an {@link IntIterator} consisting of the elements in the parameter
	 *         array followed by the elements of the source {@link IntIterator}.
	 */
	IntIterator insert(int... other);

	/**
	 * Applies an accumulation operation to this {@link IntIterator} to produce a
	 * new {@link IntIterator}.
	 *
	 * @param accumulator The accumulation function.
	 * @return Let {@code F} denote this source {@link IntIterator} and {@code g}
	 *         denote the accumulation function. Then the {@link IntIterator}
	 *         returned is of the form:
	 *         <ul>
	 *         <li>{@code [F[0], g(F[0], F[1]), g(g(F[0], F[1]), F[2]), ... ]}</li>
	 *         </ul>
	 */
	IntIterator scan(IntBinaryOperator accumulator);

	/**
	 * Applies an accumulation operation to this {@link IntIterator} to produce a
	 * new {@link IntIterator}.
	 *
	 * @param id          The identity element in the accumulation.
	 * @param accumulator The accumulator function.
	 * @return Let {@code F} denote this source {@link IntIterator} and {@code g}
	 *         denote the accumulation function. Then the {@link IntIterator}
	 *         returned is of the form:
	 *         <ul>
	 *         <li>{@code [id, g(id, F[0]), g(g(id, F[0]), F[1]), ... ]}</li>
	 *         </ul>
	 */
	IntIterator scan(int id, IntBinaryOperator accumulator);

	/**
	 * Calculates the minimum value in this {@link IntIterator}.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @return an {@link OptionalInt} wrapping the smallest element in this
	 *         {@link IntIterator} or nothing if the iteration is empty.
	 */
	OptionalInt minOption();

	/**
	 * Calculates the minimum value in this {@link IntIterator}.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param defaultValue - The value which will be returned if the source is
	 *                     empty.
	 *
	 * @return the smallest element in this {@link IntIterator} or the default value
	 *         if the iteration is empty.
	 */
	int min(int defaultValue);

	/**
	 * Calculates the minimum element in this {@link IntIterator} by a mapping to a
	 * type equipped with a natural ordering.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param     <C> A type equipped with a natural ordering.
	 *
	 * @param key A function mapping the elements of this {@link IntIterator} to
	 *            some data type with an ordering.
	 * @return The element of this {@link IntIterator} whose image under the key
	 *         mapping is the minimum among all images. Nothing is returned if the
	 *         source is empty.
	 */
	<C extends Comparable<C>> OptionalInt minByKey(IntFunction<C> key);

	/**
	 * Calculates the maximum value in this {@link IntIterator}.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @return an {@link OptionalInt} wrapping the largest element in this
	 *         {@link IntIterator} or nothing if the iteration is empty.
	 */
	OptionalInt maxOption();

	/**
	 * Calculates the maximum value in this {@link IntIterator}.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param defaultValue - The value which will be returned if the source is
	 *                     empty.
	 *
	 * @return The largest element in this {@link IntIterator} or the default value
	 *         if the iteration is empty.
	 */
	int max(int defaultValue);

	/**
	 * Calculates the maximum element in this {@link IntIterator} by a mapping to a
	 * type equipped with a natural ordering.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param     <C> A type equipped with a natural ordering.
	 *
	 * @param key A function mapping the elements of this {@link IntIterator} to
	 *            some data type with an ordering.
	 * @return The element of this {@link IntIterator} whose image under the key
	 *         mapping is the maximum among all images. Nothing is returned if the
	 *         source is empty.
	 */
	<C extends Comparable<C>> OptionalInt maxByKey(IntFunction<C> key);

	/**
	 * Checks whether every element in this {@link IntIterator} is the same.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @return True is every element of this {@link IntIterator} is equal, false
	 *         otherwise.
	 */
	boolean areAllEqual();

	/**
	 * Checks whether every element in this {@link IntIterator} passes the supplied
	 * IntPredicate test.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param predicate The supplied test.
	 * @return True if every element passes the parameter predicate test, false
	 *         otherwise.
	 */
	boolean allMatch(IntPredicate predicate);

	/**
	 * Checks whether any element in this {@link IntIterator} passes the supplied
	 * IntPredicate test.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param predicate The supplied test.
	 * @return True if any element passes the parameter predicate test, false
	 *         otherwise.
	 */
	boolean anyMatch(IntPredicate predicate);

	/**
	 * Checks whether every element in this {@link IntIterator} fails the supplied
	 * IntPredicate test.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param predicate The supplied test.
	 * @return True if every element fails the parameter predicate test, false
	 *         otherwise.
	 */
	boolean noneMatch(IntPredicate predicate);

	/**
	 * Reduces this {@link IntIterator} to a single value via some reduction
	 * function and an initial value.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param id      The identity of the reduction operation
	 * @param reducer The reduction function
	 * @return If we denote this source {@link IntIterator} by {@code F}, the length
	 *         of {@code F} by {@code n} and the reduction function by {@code f}
	 *         then the result is equal to: <br>
	 *         <br>
	 *         {@code f(...f(f(id, F[0]), F[1])..., F[n - 1])}
	 */
	int fold(int id, IntBinaryOperator reducer);

	/**
	 * Reduces this {@link IntIterator} to a single value via some reduction
	 * function. Throws exception if empty flow.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param reducer The reduction function
	 * @return Let us denote this source {@link IntIterator} by {@code F}, the
	 *         length of {@code F} by {@code n} and the reduction function by
	 *         {@code f}. If {@code n == 0} we return nothing, else we return: <br>
	 *         <br>
	 *         {@code f(...f(f(F[0], F[1]), F[2])..., F[n - 1])}
	 */
	int fold(IntBinaryOperator reducer);

	/**
	 * Reduces this {@link IntIterator} to a single value via some reduction
	 * function. Returns nothing if empty flow.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param reducer The reduction function
	 * @return Let us denote this source {@link IntIterator} by {@code F}, the
	 *         length of {@code F} by {@code n} and the reduction function by
	 *         {@code f}. If {@code n == 0} we return nothing, else we return: <br>
	 *         <br>
	 *         {@code f(...f(f(F[0], F[1]), F[2])..., F[n - 1])}
	 */
	OptionalInt foldOption(IntBinaryOperator reducer);

	/**
	 * Counts the number of elements in this {@link IntIterator}.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @return The number of elements in this {@link IntIterator}.
	 */
	long count();

	/**
	 * Caches the values in this {@link IntIterator} to a {@link IntVec}.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @return A vector containing all elements of this {@link IntIterator} with
	 *         their ordering retained.
	 */
	default IntVec toVec()
	{
		return IntVec.of(toArray());
	}

	/**
	 * Caches the values in this {@link IntIterator} to an array.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @return A long array containing all elements of this {@link IntIterator} with
	 *         their ordering retained.
	 */
	int[] toArray();

	/**
	 * Builds a Map using the elements in this {@link IntIterator} via two supplied
	 * functions.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param             <K> The type of the keys in the created mapping.
	 * @param             <V> The type of the values in the created mapping.
	 * @param keyMapper   A function mapping longs to elements of the key type.
	 * @param valueMapper A function mapping longs to elements of the value type.
	 *
	 * @throws IllegalStateException If two elements of this {@link IntIterator} map
	 *                               to the same key.
	 *
	 * @return A Map instance whose key-value pairs have a 1-to-1 correspondence
	 *         with the elements in this source {@link IntIterator}. More
	 *         specifically if:
	 *         <ul>
	 *         <li>{@code k} denotes the key mapping function</li>
	 *         <li>{@code v} denotes the value mapping function</li>
	 *         </ul>
	 *         an element of this source {@link IntIterator}, say {@code e}, is
	 *         associated to the key value pair {@code (k(e), v(e))}.
	 */
	<K, V> Map<K, V> toMap(IntFunction<? extends K> keyMapper,
			IntFunction<? extends V> valueMapper);

	/**
	 * Groups elements in this {@link IntIterator} via their image under some
	 * supplied classification function.
	 *
	 * This method is a 'consuming method', i.e. it will iterate through this
	 * {@link IntIterator}.
	 *
	 * @param            <K> The type of the keys in the grouping map.
	 *
	 * @param classifier A function defining the different groups of elements.
	 * @return A Map instance whose keys partition the elements of this source
	 *         {@link IntIterator} via the classification function. Elements in this
	 *         source {@link IntIterator} who have equal (under .equals() contract)
	 *         images under the classification function are grouped together in a
	 *         long array accessed by their shared classification key.
	 */
	<K> Map<K, int[]> groupBy(IntFunction<? extends K> classifier);

	// /**
	// * A convenience method for applying a global function onto this
	// * {@link IntIterator}.
	// *
	// * This method is potentially (depending on the supplied function) a
	// 'consuming
	// * method', i.e. it will iterate through this {@link IntIterator}.
	// *
	// * A convenience method for applying a global function onto this
	// * {@link IntIterator}.
	// *
	// * @param <C> The target type of the build function.
	// * @param builder - a function whose input encompasses {@link IntIterator}
	// * instances of this element type.
	// * @return the output of the supplied function applied to this
	// * {@link IntIterator}.
	// */
	// default <C> C build(Function<? super IntIterator, C> builder)
	// {
	// return builder.apply(this);
	// }

	/**
	 * Boxes the primitive long values in this {@link IntIterator}.
	 *
	 * @return a copy of this source {@link IntIterator} as a Iterator of boxed
	 *         {@linkplain Integer} instances.
	 */
	default EnhancedIterator<Integer> boxed()
	{
		return mapToObject(x -> x);
	}

	/**
	 * Computes the sum of elements in a non-empty iterator.
	 * 
	 * @return the sum of elements if this iterator is non-empty, throws an
	 *         exception otherwise.
	 */
	default int sum()
	{
		return fold(0, (a, b) -> a + b);
	}
}