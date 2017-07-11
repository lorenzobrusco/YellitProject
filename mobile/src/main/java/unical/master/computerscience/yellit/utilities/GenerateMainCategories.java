package unical.master.computerscience.yellit.utilities;

import android.content.Context;

import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 11/07/2017.
 */

public class GenerateMainCategories {


    /**
     * @return the macro categories that micro belongs
     * @param context context
     * @param micro string that we check belongs to something
     */
    public static String getMacro(final Context context, String micro){

        if(contains(context.getResources().getStringArray(R.array.sub_categories_2),micro)){
            return "Inside";
        }
        else if(contains(context.getResources().getStringArray(R.array.sub_categories_3),micro)){
            return "Outside";
        }
        else if(contains(context.getResources().getStringArray(R.array.sub_categories_4),micro)){
            return "FoodandDrink";
        }
        else{
            return micro;
        }
    }

    /**
     *  Check if a string is contained into array
     * */
    private static boolean contains(String[] array, String value){
        for(int i=0;i<array.length;i++){
            if(value.equalsIgnoreCase(array[i]))
                return true;
        }
        return false;
    }


}
