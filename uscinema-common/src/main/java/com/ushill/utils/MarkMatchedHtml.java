package com.ushill.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MarkMatchedHtml {
    private static final String prefix = "<span class=\"marked\">";
    private static final String suffix = "</span>";

    public static String markMatchedHtml(String origin, List<String> paraList){

        List<MatchItem> k = findMatchedItem(origin, paraList);
        if(k.size() > 0) {
            k.sort(Comparator.comparingInt(MatchItem::getIdx));
            return markMatchedItems(origin, k);
        }
        return origin;

    }

    private static List<MatchItem> findMatchedItem(String origin, List<String> paraList){

        int start;
        int idx;
        List<MatchItem> k = new ArrayList<>();
        for (String para: paraList){
            if(para.length() == 0){
                continue;
            }
            start = 0;
            while(true) {
                idx = origin.substring(start).toLowerCase().indexOf(para.toLowerCase());
                if (idx >= 0) {
                    k.add(new MatchItem(idx + start, para.length()));
                    start = idx + start + para.length();
                } else {
                    break;
                }
            }
        }
        return k;
    };

    private static String markMatchedItems(String origin, List<MatchItem> k){
        StringBuilder res = new StringBuilder();
        int last_idx = 0;
        int last_length = 0;
        for(MatchItem item: k) {
            int idx = item.idx;
            int length = item.length;

            if(idx >= last_idx + last_length) {
                res.append(origin, last_idx + last_length, idx);
                res.append(prefix);
                res.append(origin.substring(idx, idx + length));
            } else if (idx + length > last_idx + last_length) {
                res.append(prefix);
                res.append(origin.substring(last_idx + last_length, idx + length));
            } else {
                continue;
            }

            res.append(suffix);
            last_idx = idx;
            last_length = length;
        }

        if (last_idx + last_length >
                k.get(k.size() - 1).getIdx() + k.get(k.size() - 1).getLength()) {
            res.append(origin.substring(last_idx + last_length));
        } else {
            res.append(origin.substring(k.get(k.size() - 1).getIdx() + k.get(k.size() - 1).getLength()));
        }

        return res.toString();
    }

    static class MatchItem{
        private int idx;
        private int length;
        MatchItem(int idx, int length){
            this.idx = idx;
            this.length = length;
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }
}
