package com.example.reciclaapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MaterialesItem implements Parcelable {
    private final int imageResource; // Resource ID for the item's image
    private final String name; // Name or text associated with the item
    int materialQuantity;
    String materialUnit;
    Uri fotoMaterial;
    String urlFotoMaterial;

    public MaterialesItem(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
        this.fotoMaterial = null;
    }

    // Parcelable implementation
    protected MaterialesItem(Parcel in) {
        imageResource = in.readInt();
        name = in.readString();
        materialQuantity = in.readInt();
        materialUnit = in.readString();
        fotoMaterial = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<MaterialesItem> CREATOR = new Creator<MaterialesItem>() {
        @Override
        public MaterialesItem createFromParcel(Parcel in) {
            return new MaterialesItem(in);
        }

        @Override
        public MaterialesItem[] newArray(int size) {
            return new MaterialesItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(imageResource);
        parcel.writeString(name);
        parcel.writeInt(materialQuantity);
        parcel.writeString(materialUnit);
        parcel.writeParcelable(fotoMaterial, flags);
    }

    public String getUrlFotoMaterial() {
        return urlFotoMaterial;
    }

    public void setUrlFotoMaterial(String urlFotoMaterial) {
        this.urlFotoMaterial = urlFotoMaterial;
    }

    public Uri getFotoMaterial() {
        return fotoMaterial;
    }

    public void setFotoMaterial(Uri fotoMaterial) {
        this.fotoMaterial = fotoMaterial;
    }

    public int getMaterialQuantity() {
        return materialQuantity;
    }

    public void setMaterialQuantity(int materialQuantity) {
        this.materialQuantity = materialQuantity;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }
}
