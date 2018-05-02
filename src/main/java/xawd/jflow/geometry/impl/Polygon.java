/**
 *
 */
package xawd.jflow.geometry.impl;

import java.util.List;
import java.util.NoSuchElementException;

import xawd.jflow.AbstractFlow;
import xawd.jflow.Flow;
import xawd.jflow.construction.Iter;
import xawd.jflow.geometry.LineIterable;
import xawd.jflow.geometry.Point;
import xawd.jflow.geometry.PointIterable;
import xawd.jflow.geometry.splines.LineSegment;
import xawd.lists.listflow.ArrayListFlow;

/**
 * @author t
 *
 */
public final class Polygon implements PointIterable, LineIterable {

	private final List<Point> points;

	public Polygon(final Flow<Point> src) {
		points = src.map(Point::copy).toList();
	}

	public Polygon(final List<Point> src) {
		points = new ArrayListFlow<>(src);
	}

	/* (non-Javadoc)
	 * @see xawd.jflow.geometry.LineIterable#iterateOverLines()
	 */
	@Override
	public Flow<LineSegment> iterateOverLines() {
		return new AbstractFlow<LineSegment>() {
			int count = 0;
			@Override
			public boolean hasNext() {
				return count < points.size();
			}

			@Override
			public LineSegment next() {
				try {
					final Point start = points.get(count);
					final Point end = points.get((count + 1)%points.size());
					count++;
					return new LineSegment(start, end);
				}
				catch (final IndexOutOfBoundsException ex) {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void skip() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				else {
					count++;
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see xawd.jflow.geometry.PointIterable#iterateOverPoints()
	 */
	@Override
	public Flow<Point> iterateOverPoints() {
		return Iter.of(points);
	}
}
