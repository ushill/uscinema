package com.ushill.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RandomSubList {

    public static<T> List<T> randomSublist(List<T> list, int needLength){
        T tmp;
        Random r = new Random();
        int listSize = list.size();

        for(int i = listSize - 1; i >= listSize - needLength; i--){
            int j = r.nextInt(i);
            tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
        }
        return list.subList(listSize - needLength, listSize);
    }

    public static<T> List<T> randomSublist(List<T> list, int needLength, Comparator<? super T> c){
        list = randomSublist(list, needLength);
        list.sort(c);
        return list;
    }
}
