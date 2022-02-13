package com.sayur.tobos.checkout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Waktu {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<Data> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data{
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("start")
        private String start;
        @SerializedName("end")
        private String end;
        @SerializedName("timezone")
        private String timezone;
        @SerializedName("isPilih")
        private boolean isPilih = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public boolean isPilih() {
            return isPilih;
        }

        public void setPilih(boolean pilih) {
            isPilih = pilih;
        }
    }
}
