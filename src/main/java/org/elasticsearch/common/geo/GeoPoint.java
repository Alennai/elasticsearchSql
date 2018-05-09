package org.elasticsearch.common.geo;

import org.apache.lucene.util.BytesRef;

import java.util.Arrays;

/**
 * Created by xusiao on 2018/5/7.
 */
public final class GeoPoint {

    private double lat;
    private double lon;

    public GeoPoint() {
    }

    /**
     * Create a new Geopoint from a string. This String must either be a geohash
     * or a lat-lon tuple.
     *
     * @param value String to create the point from
     */
    public GeoPoint(String value) {
        this.resetFromString(value);
    }

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public GeoPoint(GeoPoint template) {
        this(template.getLat(), template.getLon());
    }

    public GeoPoint reset(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        return this;
    }

    public GeoPoint resetLat(double lat) {
        this.lat = lat;
        return this;
    }

    public GeoPoint resetLon(double lon) {
        this.lon = lon;
        return this;
    }

    public GeoPoint resetFromString(String value) {
        int comma = value.indexOf(',');
        if (comma != -1) {
            lat = Double.parseDouble(value.substring(0, comma).trim());
            lon = Double.parseDouble(value.substring(comma + 1).trim());
        } else {
            resetFromGeoHash(value);
        }
        return this;
    }

    public GeoPoint resetFromIndexHash(long hash) {
        lon = GeoPointField.decodeLongitude(hash);
        lat = GeoPointField.decodeLatitude(hash);
        return this;
    }

    // todo this is a crutch because LatLonPoint doesn't have a helper for returning .stringValue()
    // todo remove with next release of lucene
    public GeoPoint resetFromIndexableField(IndexableField field) {
        if (field instanceof LatLonPoint) {
            BytesRef br = field.binaryValue();
            byte[] bytes = Arrays.copyOfRange(br.bytes, br.offset, br.length);
            return this.reset(
                    GeoEncodingUtils.decodeLatitude(bytes, 0),
                    GeoEncodingUtils.decodeLongitude(bytes, Integer.BYTES));
        } else if (field instanceof LatLonDocValuesField) {
            long encoded = (long)(field.numericValue());
            return this.reset(
                    GeoEncodingUtils.decodeLatitude((int)(encoded >>> 32)),
                    GeoEncodingUtils.decodeLongitude((int)encoded));
        }
        return resetFromIndexHash(Long.parseLong(field.stringValue()));
    }

    public GeoPoint resetFromGeoHash(String geohash) {
        final long hash = mortonEncode(geohash);
        return this.reset(GeoPointField.decodeLatitude(hash), GeoPointField.decodeLongitude(hash));
    }

    public GeoPoint resetFromGeoHash(long geohashLong) {
        final int level = (int)(12 - (geohashLong&15));
        return this.resetFromIndexHash(BitUtil.flipFlop((geohashLong >>> 4) << ((level * 5) + 2)));
    }

    public double lat() {
        return this.lat;
    }

    public double getLat() {
        return this.lat;
    }

    public double lon() {
        return this.lon;
    }

    public double getLon() {
        return this.lon;
    }

    public String geohash() {
        return stringEncode(lon, lat);
    }

    public String getGeohash() {
        return stringEncode(lon, lat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoPoint geoPoint = (GeoPoint) o;

        if (Double.compare(geoPoint.lat, lat) != 0) return false;
        if (Double.compare(geoPoint.lon, lon) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = lat != +0.0d ? Double.doubleToLongBits(lat) : 0L;
        result = Long.hashCode(temp);
        temp = lon != +0.0d ? Double.doubleToLongBits(lon) : 0L;
        result = 31 * result + Long.hashCode(temp);
        return result;
    }

    @Override
    public String toString() {
        return lat + ", " + lon;
    }

    public static GeoPoint parseFromLatLon(String latLon) {
        GeoPoint point = new GeoPoint(latLon);
        return point;
    }

    public static GeoPoint fromGeohash(String geohash) {
        return new GeoPoint().resetFromGeoHash(geohash);
    }

    public static GeoPoint fromGeohash(long geohashLong) {
        return new GeoPoint().resetFromGeoHash(geohashLong);
    }
}

