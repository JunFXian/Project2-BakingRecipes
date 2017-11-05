/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 26, 2017
 * Reference:
 */

package com.udacity.jun.bakingrecipes.models;

import java.io.Serializable;

public class RecipeStep implements Serializable {
    private int id;
    private String shortDescription;
    private final String description;
    private String videoURL;
    private String thumbnailURL;

    public RecipeStep(int stepId, String stepshort, String stepDescription,
                      String stepVideoUrl, String stepThumbnailUrl) {
        this.id = stepId;
        this.shortDescription = stepshort;
        this.description = stepDescription;
        this.videoURL = stepVideoUrl;
        this.thumbnailURL = stepThumbnailUrl;
    }

    public int getId() {
        return this.id;
    }
    public String getShortDescription() {
        return this.shortDescription;
    }
    public String getDescription() {
        return this.description;
    }
    public String getVideoURL() {
        return  this.videoURL;
    }
    public String getThumbnailURL() {
        return  this.thumbnailURL;
    }

    public void setId(int myId) {
        this.id = myId;
    }
    public void setShortDescription(String myShort) {
        this.shortDescription = myShort;
    }
    public void setDescription(String myDescription) {
        this.shortDescription= myDescription;
    }
    public void setVideoURL(String myVideoUrl) {
        this.videoURL = myVideoUrl;
    }
    public void setThumbnailURL(String myThumbnailUrl) {
        this.thumbnailURL = myThumbnailUrl;
    }
}
