package org.elasticsearch.index.fielddata;

import org.apache.lucene.index.SortedNumericDocValues;
import org.apache.lucene.index.SortedNumericDoubleValues;
import org.apache.lucene.util.BytesRef;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Created by xusiao on 2018/5/7.
 */
public abstract  class ScriptDocValues <T> extends AbstractList<T> {

    /**
     * Set the current doc ID.
     */
    public abstract void setNextDocId(int docId);

    /**
     * Return a copy of the list of the values for the current document.
     */
    public final List<T> getValues() {
        return this;
    }

    // Throw meaningful exceptions if someone tries to modify the ScriptDocValues.
    @Override
    public final void add(int index, T element) {
        throw new UnsupportedOperationException("doc values are unmodifiable");
    }

    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException("doc values are unmodifiable");
    }

    @Override
    public final void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException("doc values are unmodifiable");
    }

    @Override
    public final T set(int index, T element) {
        throw new UnsupportedOperationException("doc values are unmodifiable");
    }

    @Override
    public final void sort(Comparator<? super T> c) {
        throw new UnsupportedOperationException("doc values are unmodifiable");
    }

    public static final class Strings extends ScriptDocValues<String> {

        private final SortedBinaryDocValues values;

        public Strings(SortedBinaryDocValues values) {
            this.values = values;
        }

        @Override
        public void setNextDocId(int docId) {
            values.setDocument(docId);
        }

        public SortedBinaryDocValues getInternalValues() {
            return this.values;
        }

        public BytesRef getBytesValue() {
            if (values.count() > 0) {
                return values.valueAt(0);
            } else {
                return null;
            }
        }

        public String getValue() {
            BytesRef value = getBytesValue();
            if (value == null) {
                return null;
            } else {
                return value.utf8ToString();
            }
        }

        @Override
        public String get(int index) {
            return values.valueAt(index).utf8ToString();
        }

        @Override
        public int size() {
            return values.count();
        }

    }

    public static final class Longs extends ScriptDocValues<Long> {

        private final SortedNumericDocValues values;
        private Dates dates;

        public Longs(SortedNumericDocValues values) {
            this.values = values;
        }

        @Override
        public void setNextDocId(int docId) {
            values.setDocument(docId);
            if (dates != null) {
                dates.refreshArray();
            }
        }

        public SortedNumericDocValues getInternalValues() {
            return this.values;
        }

        public long getValue() {
            int numValues = values.count();
            if (numValues == 0) {
                return 0L;
            }
            return values.valueAt(0);
        }

        public ReadableDateTime getDate() {
            if (dates == null) {
                dates = new Dates(values);
                dates.refreshArray();
            }
            return dates.getValue();
        }

        public List<ReadableDateTime> getDates() {
            if (dates == null) {
                dates = new Dates(values);
                dates.refreshArray();
            }
            return dates;
        }

        @Override
        public Long get(int index) {
            return values.valueAt(index);
        }

        @Override
        public int size() {
            return values.count();
        }
    }

    public static final class Dates extends ScriptDocValues<ReadableDateTime> {
        private static final ReadableDateTime EPOCH = new DateTime(0, DateTimeZone.UTC);

        private final SortedNumericDocValues values;
        /**
         * Values wrapped in {@link MutableDateTime}. Null by default an allocated on first usage so we allocate a reasonably size. We keep
         * this array so we don't have allocate new {@link MutableDateTime}s on every usage. Instead we reuse them for every document.
         */
        private MutableDateTime[] dates;

        public Dates(SortedNumericDocValues values) {
            this.values = values;
        }

        /**
         * Fetch the first field value or 0 millis after epoch if there are no values.
         */
        public ReadableDateTime getValue() {
            if (values.count() == 0) {
                return EPOCH;
            }
            return get(0);
        }

        @Override
        public ReadableDateTime get(int index) {
            if (index >= values.count()) {
                throw new IndexOutOfBoundsException(
                        "attempted to fetch the [" + index + "] date when there are only [" + values.count() + "] dates.");
            }
            return dates[index];
        }

        @Override
        public int size() {
            return values.count();
        }

        @Override
        public void setNextDocId(int docId) {
            values.setDocument(docId);
            refreshArray();
        }

        /**
         * Refresh the backing array. Package private so it can be called when {@link Longs} loads dates.
         */
        void refreshArray() {
            if (values.count() == 0) {
                return;
            }
            if (dates == null) {
                // Happens for the document. We delay allocating dates so we can allocate it with a reasonable size.
                dates = new MutableDateTime[values.count()];
                for (int i = 0; i < dates.length; i++) {
                    dates[i] = new MutableDateTime(values.valueAt(i), DateTimeZone.UTC);
                }
                return;
            }
            if (values.count() > dates.length) {
                // Happens when we move to a new document and it has more dates than any documents before it.
                MutableDateTime[] backup = dates;
                dates = new MutableDateTime[values.count()];
                System.arraycopy(backup, 0, dates, 0, backup.length);
                for (int i = 0; i < backup.length; i++) {
                    dates[i].setMillis(values.valueAt(i));
                }
                for (int i = backup.length; i < dates.length; i++) {
                    dates[i] = new MutableDateTime(values.valueAt(i), DateTimeZone.UTC);
                }
                return;
            }
            for (int i = 0; i < values.count(); i++) {
                dates[i].setMillis(values.valueAt(i));
            }
        }
    }

    public static final class Doubles extends ScriptDocValues<Double> {

        private final SortedNumericDoubleValues values;

        public Doubles(SortedNumericDoubleValues values) {
            this.values = values;
        }

        @Override
        public void setNextDocId(int docId) {
            values.setDocument(docId);
        }

        public SortedNumericDoubleValues getInternalValues() {
            return this.values;
        }

        public double getValue() {
            int numValues = values.count();
            if (numValues == 0) {
                return 0d;
            }
            return values.valueAt(0);
        }

        @Override
        public Double get(int index) {
            return values.valueAt(index);
        }

        @Override
        public int size() {
            return values.count();
        }
    }

    public static final class GeoPoints extends ScriptDocValues<GeoPoint> {

        private final MultiGeoPointValues values;

        public GeoPoints(MultiGeoPointValues values) {
            this.values = values;
        }

        @Override
        public void setNextDocId(int docId) {
            values.setDocument(docId);
        }

        public GeoPoint getValue() {
            int numValues = values.count();
            if (numValues == 0) {
                return null;
            }
            return values.valueAt(0);
        }

        public double getLat() {
            return getValue().lat();
        }

        public double[] getLats() {
            List<GeoPoint> points = getValues();
            double[] lats = new double[points.size()];
            for (int i = 0; i < points.size(); i++) {
                lats[i] = points.get(i).lat();
            }
            return lats;
        }

        public double[] getLons() {
            List<GeoPoint> points = getValues();
            double[] lons = new double[points.size()];
            for (int i = 0; i < points.size(); i++) {
                lons[i] = points.get(i).lon();
            }
            return lons;
        }

        public double getLon() {
            return getValue().lon();
        }

        @Override
        public GeoPoint get(int index) {
            final GeoPoint point = values.valueAt(index);
            return new GeoPoint(point.lat(), point.lon());
        }

        @Override
        public int size() {
            return values.count();
        }

        public double arcDistance(double lat, double lon) {
            GeoPoint point = getValue();
            return GeoUtils.arcDistance(point.lat(), point.lon(), lat, lon);
        }

        public double arcDistanceWithDefault(double lat, double lon, double defaultValue) {
            if (isEmpty()) {
                return defaultValue;
            }
            return arcDistance(lat, lon);
        }

        public double planeDistance(double lat, double lon) {
            GeoPoint point = getValue();
            return GeoUtils.planeDistance(point.lat(), point.lon(), lat, lon);
        }

        public double planeDistanceWithDefault(double lat, double lon, double defaultValue) {
            if (isEmpty()) {
                return defaultValue;
            }
            return planeDistance(lat, lon);
        }

        public double geohashDistance(String geohash) {
            GeoPoint point = getValue();
            return GeoUtils.arcDistance(point.lat(), point.lon(), GeoHashUtils.decodeLatitude(geohash),
                    GeoHashUtils.decodeLongitude(geohash));
        }

        public double geohashDistanceWithDefault(String geohash, double defaultValue) {
            if (isEmpty()) {
                return defaultValue;
            }
            return geohashDistance(geohash);
        }
    }

    public static final class Booleans extends ScriptDocValues<Boolean> {

        private final SortedNumericDocValues values;

        public Booleans(SortedNumericDocValues values) {
            this.values = values;
        }

        @Override
        public void setNextDocId(int docId) {
            values.setDocument(docId);
        }

        public boolean getValue() {
            return values.count() != 0 && values.valueAt(0) == 1;
        }

        @Override
        public Boolean get(int index) {
            return values.valueAt(index) == 1;
        }

        @Override
        public int size() {
            return values.count();
        }

    }

    public static final class BytesRefs extends ScriptDocValues<BytesRef> {

        private final SortedBinaryDocValues values;

        public BytesRefs(SortedBinaryDocValues values) {
            this.values = values;
        }

        @Override
        public void setNextDocId(int docId) {
            values.setDocument(docId);
        }

        public SortedBinaryDocValues getInternalValues() {
            return this.values;
        }

        public BytesRef getValue() {
            int numValues = values.count();
            if (numValues == 0) {
                return new BytesRef();
            }
            return values.valueAt(0);
        }

        @Override
        public BytesRef get(int index) {
            return values.valueAt(index);
        }

        @Override
        public int size() {
            return values.count();
        }
    }
}

