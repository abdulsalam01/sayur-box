package com.sayur.tobos.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Constraint {
//    public static final String BASE_URL = "http://192.168.1.105:8080/";
    public static final String BASE_URL = "https://tobos.widyanet.my.id/";
    public static final String API_URL = BASE_URL + "api/";

    public static String rupiah(String harga){
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        return "Rp. " + numberFormat.format(Integer.parseInt(harga)).replace("Rp", "").replace(",00", "");
    }
}
