package com.sevendesign.planitprom.database.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.sevendesign.planitprom.database.tables.CategoriesTableEntries;
import com.sevendesign.planitprom.database.tables.PhotosTableEntries;

import java.math.BigDecimal;

public class PhotoItem implements IContentValuesConvertable, Parcelable {
    private int id;
    private String title;
    private String path;
    private BigDecimal cost;
    private String merchant;
    private String notes;
    private String date;
    private int categoryId;

	public PhotoItem(String title, String path, BigDecimal cost, String merchant, String notes, String date, int categoryId) {
		this.title = title;
        this.path = path;
        this.cost = cost;
        this.merchant = merchant;
        this.notes = notes;
        this.date = date;
        this.categoryId = categoryId;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(PhotosTableEntries.TITLE, title);
        cv.put(PhotosTableEntries.PATH, path);
        cv.put(PhotosTableEntries.COST, cost.toPlainString());
        cv.put(PhotosTableEntries.MERCHANT, merchant);
        cv.put(PhotosTableEntries.NOTES, notes);
        cv.put(PhotosTableEntries.DATE, date);
        cv.put(PhotosTableEntries.CATEGORY_ID, categoryId);
		return cv;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        public PhotoItem createFromParcel(Parcel parcel) {
            int id = parcel.readInt();
            String title = parcel.readString();
            String path = parcel.readString();
            String costStr = parcel.readString();
            BigDecimal cost = new BigDecimal(costStr);
            String merchant = parcel.readString();
            String notes = parcel.readString();
            String date = parcel.readString();
            int categoryId = parcel.readInt();

            PhotoItem themeItem = new PhotoItem(title, path, cost, merchant, notes, date, categoryId);
            themeItem.setId(id);

            return themeItem;
        }

        public PhotoItem[] newArray(int i) {
            return new PhotoItem[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getPath());
        parcel.writeString(getCost().toPlainString());
        parcel.writeString(getMerchant());
        parcel.writeString(getNotes());
        parcel.writeString(getDate());
        parcel.writeInt(getCategoryId());
    }
}
