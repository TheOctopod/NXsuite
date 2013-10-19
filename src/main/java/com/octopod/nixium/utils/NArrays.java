package com.octopod.nixium.utils;

public class NArrays {
    
    public static String[] push(String[] array, String element){
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = element;
        return(newArray);
    }
    
    public static String[] remove(String[] array, String element){
        int matches = 0;
        for(int i = 0; i < array.length; i++){if(array[i].equals(element)){matches++;}}
        int index = 0;
        String[] newArray = new String[array.length - matches];
        for(int i = 0; i < array.length; i++){
            if(!array[i].equals(element)){
                newArray[index] = array[i];
                index++;
            }
        }
        return newArray;
    }
    
}
