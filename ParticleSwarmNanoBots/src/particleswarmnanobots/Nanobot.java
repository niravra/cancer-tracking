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
public class Nanobot {

    private BotLocation location;
    private BotVelocity velocity;
    private double procal;
    private double adhesionMolecule;
    private double fitness;

    public Nanobot() {
    }

    public Nanobot(BotLocation location, BotVelocity velocity, double procal, double fitness) {
        this.location = location;
        this.velocity = velocity;
        this.procal = procal;
        this.fitness = fitness;
    }

    public BotLocation getLocation() {
        return location;
    }

    public void setLocation(BotLocation location) {
        this.location = location;
    }

    public BotVelocity getVelocity() {
        return velocity;
    }

    public void setVelocity(BotVelocity velocity) {
        this.velocity = velocity;
    }

    public double getProcal() {
        return procal;
    }

    public void setProcal(double procal) {
        this.procal = procal;
    }

    public double getFitness() {

        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getAdhesionMolecule() {
        return adhesionMolecule;
    }

    public void setAdhesionMolecule(double adhesionMolecule) {
        this.adhesionMolecule = adhesionMolecule;
    }

}
