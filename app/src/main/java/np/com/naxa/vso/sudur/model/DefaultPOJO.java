/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package np.com.naxa.vso.sudur.model;


import java.util.Random;

import np.com.naxa.vso.R;

public class DefaultPOJO {

    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.api_himal;
            case 1:
                return R.drawable.bajhang;
            case 2:
                return R.drawable.tourist_card_pic1;
            case 3:
                return R.drawable.deve2;
            case 4:
                return R.drawable.deve3;
            case 5:
                return R.drawable.deve1;
        }
    }

    public static final String[] sCheeseStrings = {
            " Agricultural Development", "Commerce & Supplies ", " Education",
            " Energy", "Science, Technology & Environment","Forests & Soil Conservation",
            "Health & Population"};

}
