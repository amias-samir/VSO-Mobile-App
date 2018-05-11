package np.com.naxa.vso;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class FloatingSuggestion implements SearchSuggestion {

//    private String suggestion;
//
//    public Suggestions(String suggestion) {
//        this.suggestion = suggestion.toLowerCase();
//    }
//
//    @Override
//    public String getBody() {
//        return suggestion;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(suggestion);
//    }

    private String mColorName;
    private boolean mIsHistory = false;

    public FloatingSuggestion(String suggestion) {
        this.mColorName = suggestion.toLowerCase();
    }

    public FloatingSuggestion(Parcel source) {
        this.mColorName = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return mColorName;
    }

    public static final Creator<FloatingSuggestion> CREATOR = new Creator<FloatingSuggestion>() {
        @Override
        public FloatingSuggestion createFromParcel(Parcel in) {
            return new FloatingSuggestion(in);
        }

        @Override
        public FloatingSuggestion[] newArray(int size) {
            return new FloatingSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mColorName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }

}
