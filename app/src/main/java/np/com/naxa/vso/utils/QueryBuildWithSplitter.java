package np.com.naxa.vso.utils;

import android.text.TextUtils;
import android.util.Log;

public class QueryBuildWithSplitter {

    private static final String TAG = "QueryBuildWithSplitter";

    public static String dynamicStringSplitterWithColumnCheckQuery(String columnName, String rawStringData){

        String splittedStringWithOR = "";

        if(!TextUtils.isEmpty(rawStringData)){

        if(rawStringData.contains(",")) {

            String trunk = rawStringData;
            String temp = trunk.replaceAll("\\s+", "");
            String[] splitted_string = temp.split("\\,");
            String dial = "";
            String[] innerData = new String[splitted_string.length];
            for (int i = 0; i < splitted_string.length; i++) {

                if(i == 0){
                    splittedStringWithOR = splitted_string[i].trim();
                }else {
//                    query build with column name
                    splittedStringWithOR = splittedStringWithOR + " OR "+columnName +" LIKE :"+ splitted_string[i];
                }
            }
        }else {
            splittedStringWithOR = rawStringData.trim();
        }
        }

        Log.d(TAG, "dynamicStringSplitterWithColumnCheckQuery: "+ splittedStringWithOR);

        return splittedStringWithOR;
    }


public static int[] dynamicStringSplitterWithRangeQueryBuild( String rawStringData){

    String splittedStringWithRange = "";
    int lowestVal = 0;
    int highestVal = 0;
    int range[] = new int[2];

    if(!TextUtils.isEmpty(rawStringData)){

        if(rawStringData.contains(",")) {

            String trunk = rawStringData;
            String temp = trunk.replaceAll("\\s+", "");
            String[] splitted_string = temp.split("\\,");
            for (int i = 0; i < splitted_string.length; i++) {

                if(i == 0){
                    String firstSplittedData = splitted_string[i].trim();
                    lowestVal = Integer.parseInt(firstSplittedData.substring(0,2));
                    range[0] = lowestVal;
                }
                if(i == splitted_string.length-1){
                    String LastSplittedData = splitted_string[i].trim();
                    highestVal = Integer.parseInt(LastSplittedData.substring(Math.max(LastSplittedData.length() - 2, 0)));
                    range[i] = highestVal;
                }
            }
//            range query
//            splittedStringWithRange = columnName+" BETWEEN "+lowestVal+" AND "+highestVal;
//            splittedStringWithRange = lowestVal+" AND "+highestVal;

        }else {
            String rawData = rawStringData.trim();

            lowestVal = Integer.parseInt(rawData.substring(0,2));
            highestVal = Integer.parseInt(rawData.substring(3,2));

            range[0] = lowestVal;
            range[1] = highestVal;

//            range query
//            splittedStringWithRange = columnName+" BETWEEN "+lowestVal+" AND "+highestVal;
//            splittedStringWithRange = lowestVal+" AND "+highestVal;
        }
    }

//    return splittedStringWithRange;
    Log.d(TAG, "dynamicStringSplitterWithRangeQueryBuild: "+range[0]);
    Log.d(TAG, "dynamicStringSplitterWithRangeQueryBuild: "+range[1]);
    return range;
}



    public static RangeValue GetRange(){
        RangeValue rangeValue = null;
        rangeValue.lowest = 0;
        rangeValue.highest = 0;
        return rangeValue;
    }
    public class RangeValue{
        int lowest;
        int highest;
    }
}
