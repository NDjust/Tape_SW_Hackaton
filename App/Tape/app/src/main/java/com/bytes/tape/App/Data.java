package com.bytes.tape.App;

import java.io.Serializable;

public class Data {
    public static class Movie implements Serializable {

        int Id;
        String Name, Content, Title, Date, ImgURL, MovieURL;

        public Movie(int Id, String Name, String Content, String Title, String Date, String ImgURL, String MovieURL) {
            this.Id = Id;
            this.Name = Name;
            this.Content = Content;
            this.Title = Title;
            this.Date = Date;
            this.ImgURL = ImgURL;
            this.MovieURL = MovieURL;
        }

        public int getId() {
            return Id;
        }
        public String getName() {
            return Name;
        }
        public String getContent() {
            return Content;
        }
        public String getTitle() {
            return Title;
        }
        public String getDate() {
            return Date;
        }
        public String getImgURL() {
            return ImgURL;
        }
        public String getMovieURL() {
            return MovieURL;
        }
    }
}