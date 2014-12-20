package com.sicamp.openlabel;

/**
 * Created by 1002230 on 12/20/14.
 */
public class HotDto {
    private String textLeft;
    private String textRight;
    private String imgUrlLeft;
    private String imgUrlRight;
    private int recommendLeft;
    private int aVoidLeft;
    private int recommendRight;
    private int aVoidRight;

    public HotDto() {

    }

    public HotDto(String textLeft, String textRight, String imgUrlLeft, String imgUrlRight, int recommendLeft, int aVoidLeft, int recommendRight, int aVoidRight) {
        this.textLeft = textLeft;
        this.textRight = textRight;
        this.imgUrlLeft = imgUrlLeft;
        this.imgUrlRight = imgUrlRight;
        this.recommendLeft = recommendLeft;
        this.aVoidLeft = aVoidLeft;
        this.recommendRight = recommendRight;
        this.aVoidRight = aVoidRight;
    }

    public String getTextLeft() {
        return textLeft;
    }

    public void setTextLeft(String textLeft) {
        this.textLeft = textLeft;
    }

    public String getTextRight() {
        return textRight;
    }

    public void setTextRight(String textRight) {
        this.textRight = textRight;
    }

    public String getImgUrlLeft() {
        return imgUrlLeft;
    }

    public void setImgUrlLeft(String imgUrlLeft) {
        this.imgUrlLeft = imgUrlLeft;
    }

    public String getImgUrlRight() {
        return imgUrlRight;
    }

    public void setImgUrlRight(String imgUrlRight) {
        this.imgUrlRight = imgUrlRight;
    }

    public int getRecommendLeft() {
        return recommendLeft;
    }

    public void setRecommendLeft(int recommendLeft) {
        this.recommendLeft = recommendLeft;
    }

    public int getaVoidLeft() {
        return aVoidLeft;
    }

    public void setaVoidLeft(int aVoidLeft) {
        this.aVoidLeft = aVoidLeft;
    }

    public int getRecommendRight() {
        return recommendRight;
    }

    public void setRecommendRight(int recommendRight) {
        this.recommendRight = recommendRight;
    }

    public int getaVoidRight() {
        return aVoidRight;
    }

    public void setaVoidRight(int aVoidRight) {
        this.aVoidRight = aVoidRight;
    }

    @Override
    public String toString() {
        return "HotDto{" +
                "textLeft='" + textLeft + '\'' +
                ", textRight='" + textRight + '\'' +
                ", imgUrlLeft='" + imgUrlLeft + '\'' +
                ", imgUrlRight='" + imgUrlRight + '\'' +
                ", recommendLeft=" + recommendLeft +
                ", aVoidLeft=" + aVoidLeft +
                ", recommendRight=" + recommendRight +
                ", aVoidRight=" + aVoidRight +
                '}';
    }
}
