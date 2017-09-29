package model;

public class Point { //untuk merepresentasikan titik

    private int x;
    private int y;

    public Point(int x, int y) { 
        this.x = x;
        this.y = y;
    }

    public int getX() { //mendapatkan titik c
        return x;
    }

    public int getY() { // mendapatkan titik y
        return y;
    }

    public void setX(int x) { //meneapkan titik x
        this.x = x;
    }

    public void setY(int y) { //menetapkan titik y
        this.y = y;
    }
}
