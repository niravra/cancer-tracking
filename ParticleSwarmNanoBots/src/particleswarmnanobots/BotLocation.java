/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleswarmnanobots;

/**
 *
 * @author niravrakar
 */
public class BotLocation {

    private double[] location;

    public BotLocation(double[] location) {
        this.location = location;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

}
