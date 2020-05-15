//MP-OK
package com.example.popularmoviesdemo;

public class Movie {
    private String s_Title;
    private String s_ReleaseData;
    private String s_Descrption;
    private double d_VotingAverage;
    private String s_PosterUrl;

    public Movie(String title, String s_PosterUrl, String releaseData, double voteAverage, String descrption) {
        this.d_VotingAverage = voteAverage;
        this.s_Descrption = descrption;
        this.s_PosterUrl = s_PosterUrl;
        this.s_Title = title;
        this.s_ReleaseData = releaseData;
    }

    public Movie(String s_PosterUrl) {
        this.s_PosterUrl = s_PosterUrl;
    }

    public String getS_Descrption() {
        return this.s_Descrption;
    }

    public String getS_Title() {
        return this.s_Title;
    }

    public String getS_PosterUrl() {
        return this.s_PosterUrl;
    }

    public String getS_ReleaseData() {
        return this.s_ReleaseData;
    }

    public double getVotingAverage() {
        return this.d_VotingAverage;
    }
}
