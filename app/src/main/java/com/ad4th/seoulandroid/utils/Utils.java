package com.ad4th.seoulandroid.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class Utils {
    public static String toNumFormat(BigInteger num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }
}
