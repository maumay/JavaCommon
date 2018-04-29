/**
 *
 */
package xawd.lists.listflow;

import java.util.Iterator;
import java.util.List;

import xawd.jflow.Flow;
import xawd.jflow.iterables.FlowIterable;

/**
 * @author t
 *
 */
public interface ListFlow<E> extends List<E>, FlowIterable<E>
{
	Flow<E> rIter();

	@Deprecated
	@Override
	default Iterator<E> iterator()
	{
		return iter();
	}
}
