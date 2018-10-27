package com.raemacias.thebakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    private final static String NO_UNIT_IN_JSON = "UNIT";
    private String name;

        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("measure")
        @Expose
        private String measure;
        @SerializedName("ingredient")
        @Expose
        private String ingredient;
        private final static long serialVersionUID = -2782697972996559776L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
         * No args constructor for use in serialization
         *
         */
        public Ingredient() {
        }

    public String getQuantityUnitNameString() {
        if (getMeasure().equals(NO_UNIT_IN_JSON)) {
            return String.format("%s %s", getQuantity(), getIngredient());
        } else {
            return String.format("%s %s %s", getQuantity(), getMeasure().toLowerCase(), getIngredient());
        }
    }

        /**
         *
         * @param measure
         * @param ingredient
         * @param quantity
         */
        public Ingredient(String quantity, String measure, String ingredient) {
            super();
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }

        protected Ingredient(Parcel in) {
            if (in.readByte() == 0) {
                quantity = null;
            } else {
                quantity = in.readString();
            }
            measure = in.readString();
            ingredient = in.readString();
        }

        public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
            @Override
            public Ingredient createFromParcel(Parcel in) {
                return new Ingredient(in);
            }

            @Override
            public Ingredient[] newArray(int size) {
                return new Ingredient[size];
            }
        };

        public String  getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (quantity == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeString(quantity);
            }
            dest.writeString(measure);
            dest.writeString(ingredient);
        }
}
