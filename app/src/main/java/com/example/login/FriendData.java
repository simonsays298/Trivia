package com.example.login;

public class FriendData {

    private String friendName;
    private String nrFriendPoints;



    public FriendData(String fruitName, String nrFriendPoints) {
        super();
        this.setFiendName(fruitName);
        this.setNrFriendPoints(nrFriendPoints);
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFiendName(String fruitName) {
        this.friendName = fruitName;
    }

    public String getNrFriendPoints() {
        return nrFriendPoints;
    }

    public void setNrFriendPoints(String nrFriendPoints) {
        this.nrFriendPoints = nrFriendPoints;
    }

    @Override
    public String toString() {
        return "FriendData{" +
                "friendName='" + friendName + '\'' +
                ", nrFriendPoints='" + nrFriendPoints + '\'' +
                '}';
    }
}
