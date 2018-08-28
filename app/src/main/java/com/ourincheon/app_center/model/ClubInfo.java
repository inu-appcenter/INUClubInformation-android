package com.ourincheon.app_center.model;

import java.util.List;

public class ClubInfo {


    //사용 안하는 모델
    List<Item> index;

    public List<Item> getClub(){
        return index;
    }

    public class Item{

        Number num;

        public Number getNum() {
            return num;
        }

        String clubname;

        public String getClubname() {
            return clubname;
        }

        String location;

        public String getLocation() {
            return location;
        }

        String image1;

        public String getImage1() {
            return image1;
        }

        String image2;

        public String getImage2() {
            return image2;
        }

        String image3;

        public String getImage3(){
            return image3;
        }

        String image4;

        public String getImage4() {
            return image4;
        }

        String representative;

        public String getRepresentative() {
            return representative;
        }

        String phone;

        public String getPhone() {
            return phone;
        }

        String application;

        public String getApplication() {
            return application;
        }

        String contents;

        public String getContents() {
            return contents;
        }
    }
}
