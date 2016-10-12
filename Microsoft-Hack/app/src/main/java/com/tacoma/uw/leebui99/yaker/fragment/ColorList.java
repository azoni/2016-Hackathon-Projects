package com.tacoma.uw.leebui99.yaker.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lebui on 1/30/2016.
 */
public class ColorList {
    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param
     * @return
     */

    public static void parseColorJSON(String colorJSON, Generator myG) {
        String reason = null;
        reason = colorJSON;
        if (reason != null) {
            try {
                JSONArray arr = new JSONArray(colorJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
//                    Color color = new Color(obj.getString(Color.COLOR), obj.getInt(Color.CORRECT),
//                            obj.getString(Color.FINALCOLOR));
//                    colorList.add(color);

                            myG.add(obj.getString(Color.COLOR), obj.getInt(Color.CORRECT),
                            obj.getString(Color.FINALCOLOR));
                }
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }

        }
 //       return reason;
    }
}
