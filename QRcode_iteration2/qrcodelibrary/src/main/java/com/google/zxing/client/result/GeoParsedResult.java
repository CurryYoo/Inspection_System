//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class GeoParsedResult extends ParsedResult {
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final String query;

    GeoParsedResult(double latitude, double longitude, double altitude, String query) {
        super(ParsedResultType.GEO);
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.query = query;
    }

    public String getGeoURI() {
        StringBuffer result = new StringBuffer();
        result.append("geo:");
        result.append(this.latitude);
        result.append(',');
        result.append(this.longitude);
        if(this.altitude > 0.0D) {
            result.append(',');
            result.append(this.altitude);
        }

        if(this.query != null) {
            result.append('?');
            result.append(this.query);
        }

        return result.toString();
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public String getQuery() {
        return this.query;
    }

    public String getDisplayResult() {
        StringBuffer result = new StringBuffer(20);
        result.append(this.latitude);
        result.append(", ");
        result.append(this.longitude);
        if(this.altitude > 0.0D) {
            result.append(", ");
            result.append(this.altitude);
            result.append('m');
        }

        if(this.query != null) {
            result.append(" (");
            result.append(this.query);
            result.append(')');
        }

        return result.toString();
    }
}
