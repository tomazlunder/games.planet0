package com.mygdx.zegame.java.deprecated;
@Deprecated
public final class Physics {

    private Physics(){}


    public float calculateRotationFromCenter(float objectX, float objectY, float worldX, float worldY){
        double theta = Math.atan2(objectY - worldY, objectX - worldX);
        double angle = Math.toDegrees(theta);
        if(angle < 0 ){
            angle += 360;
        }
        return (float) angle;
    }

}
