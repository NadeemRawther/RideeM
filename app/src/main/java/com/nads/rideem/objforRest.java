package com.nads.rideem;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.io.SerializablePermission;

/**
 * Created by Admin on 1/26/2018.
 */

public class objforRest implements Parcelable {

    private Double lats;
    private Double lngs;
    private String hyt;
    private String GHY;

    public objforRest(Double lats, Double lngs, String hyt, String GHY) {
        this.lats = lats;
        this.lngs = lngs;
        this.hyt = hyt;
        this.GHY = GHY;
    }

    protected objforRest(Parcel in) {
        if (in.readByte() == 0) {
            lats = null;
        } else {
            lats = in.readDouble();
        }
        if (in.readByte() == 0) {
            lngs = null;
        } else {
            lngs = in.readDouble();
        }
        hyt = in.readString();
        GHY = in.readString();
    }

    public static final Creator<objforRest> CREATOR = new Creator<objforRest>() {
        @Override
        public objforRest createFromParcel(Parcel in) {
            return new objforRest(in);
        }

        @Override
        public objforRest[] newArray(int size) {
            return new objforRest[size];
        }
    };

    public Double getLats() {
        return lats;
    }

    public Double getLngs() {
        return lngs;
    }

    public String getHyt() {
        return hyt;
    }

    public String getGHY() {
        return GHY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (lats == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lats);
        }
        if (lngs == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(lngs);
        }
        parcel.writeString(hyt);
        parcel.writeString(GHY);
    }
}
