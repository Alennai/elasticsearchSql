package org.apache.lucene.search;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xusiao on 2018/5/8.
 */
final class Multiset<T> extends AbstractCollection<T> {

    private final Map<T, Integer> map = new HashMap<>();
    private int size;

    /** Create an empty {@link Multiset}. */
    Multiset() {
        super();
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<Map.Entry<T, Integer>> mapIterator = map.entrySet().iterator();
        return new Iterator<T>() {

            T current;
            int remaining;

            @Override
            public boolean hasNext() {
                return remaining > 0 || mapIterator.hasNext();
            }

            @Override
            public T next() {
                if (remaining == 0) {
                    Map.Entry<T, Integer> next = mapIterator.next();
                    current = next.getKey();
                    remaining = next.getValue();
                }
                assert remaining > 0;
                remaining -= 1;
                return current;
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        map.clear();
        size = 0;
    }

    @Override
    public boolean add(T e) {
        map.put(e, map.getOrDefault(e, 0) + 1);
        size += 1;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        final Integer count = map.get(o);
        if (count == null) {
            return false;
        } else if (1 == count.intValue()) {
            map.remove(o);
        } else {
            map.put((T) o, count - 1);
        }
        size -= 1;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Multiset<?> that = (Multiset<?>) obj;
        return size == that.size // not necessary but helps escaping early
                && map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return 31 * getClass().hashCode() + map.hashCode();
    }

}

