/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particleswarmnanobots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author niravrakar
 */
public class NanobotPSOUpdate {

    //Swarm Size
    private int noOfBots = 5;

    //The coefficients for the PSO calculation
    private static double w = 0.729844;
    private static double C1 = 1.49618;
    private static double C2 = 1.49618;

    private ArrayList<Nanobot> flock = new ArrayList();
    private ArrayList<BotLocation> pBestLocation = new ArrayList();
    private double globalBest;
    private double gBestProtien;
    private double gBestAdhesion;
    private BotLocation globalBestLoc;
    private double[] personalBest = new double[noOfBots];
    private double[] fitnessValues = new double[noOfBots];
//    Map<String,Double> fitnessMap = new HashMap<String,Double>();
    private double[] xCoordinates = new double[2];
    private double[] yCoordinates = new double[2];
    private double[] zCoordinates = new double[2];
    private BotFitnessCalculator botFit = new BotFitnessCalculator();
    double protienAmt = 0.0;
    double adhesionNumber = 0.0;

    Random rand = new Random();

    //Getters and Setters to define
    public NanobotPSOUpdate() {
//        initializeBots();
    }

    public int getNoOfBots() {
        return noOfBots;
    }

    public void setNoOfBots(int noOfBots) {
        this.noOfBots = noOfBots;
    }

    public double[] getxCoordinates() {
        return xCoordinates;
    }

    public void setxCoordinates(double[] xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public double[] getyCoordinates() {
        return yCoordinates;
    }

    public void setyCoordinates(double[] yCoordinates) {
        this.yCoordinates = yCoordinates;
    }

    public double[] getzCoordinates() {
        return zCoordinates;
    }

    public void setzCoordinates(double[] zCoordinates) {
        this.zCoordinates = zCoordinates;
    }

    public void initializeBots() {
        Nanobot nb;
        int i = 0;

        while (i < noOfBots) {

            nb = new Nanobot();
            //for initializing the bots with a starting coordinate
            //that will be bounded by the coordinate range

            double[] initLocation = new double[3];
            double[] initVelocity = new double[3];

            //random generation of velocity within the specified coordinates
            initLocation[0] = xCoordinates[0] + rand.nextDouble() * (xCoordinates[1] - xCoordinates[0]);
            initLocation[1] = yCoordinates[0] + rand.nextDouble() * (yCoordinates[1] - yCoordinates[0]);
            initLocation[2] = zCoordinates[0] + rand.nextDouble() * (zCoordinates[1] - zCoordinates[0]);

            //starting velocity will be zero as they will first assess the surrounding cells
            //when they are implemented
            initVelocity[0] = 0;
            initVelocity[1] = 0;
            initVelocity[2] = 0;

            //The protienAmt in the location
            protienAmt = 0.0;

            //The Adhesion Molecule number
            adhesionNumber = 0.0;

            //set the inital location and velocity
            BotLocation loc = new BotLocation(initLocation);
            BotVelocity vel = new BotVelocity(initVelocity);

            //set the values in the nanobot
            nb.setAdhesionMolecule(adhesionNumber);
            nb.setProcal(protienAmt);
            nb.setLocation(loc);
            nb.setVelocity(vel);

            //Calculate the fitness function
            nb.setFitness(0);
            i++;
            flock.add(nb);

        }
        swarmFitnessUpdate();

    }

    public void swarmFitnessUpdate() {
        int i = 0;

        //The Protien & Adhesion Amount in that particular location
        while (i < noOfBots) {
            protienAmt = (double) (Math.random() * 300 + 110);
            adhesionNumber = (double) (Math.random() * 200 + 40);

            flock.get(i).setProcal(protienAmt);
            flock.get(i).setAdhesionMolecule(adhesionNumber);

            flock.get(i).setFitness(botFit.fitnessCal(flock.get(i).getLocation(), flock.get(i).getProcal(), flock.get(i).getAdhesionMolecule()));
            fitnessValues[i] = flock.get(i).getFitness();

            i++;
        }

    }

    public int getMinPos(double[] list) {
        int pos = 0;
        double minValue = list[0];

        for (int i = 0; i < list.length; i++) {
            if (list[i] < minValue) {
                pos = i;
                minValue = list[i];
            }
        }

        return pos;
    }

    public int getMaxPos(double[] list) {
        int pos = 0;
        double maxValue = list[0];

        for (int i = 0; i < list.length; i++) {
            if (list[i] > maxValue) {
                pos = i;
                maxValue = list[i];
            }
        }

        return pos;
    }

    public void psoMethod() {

        initializeBots();
        for (int i = 0; i < noOfBots; i++) {
            personalBest[i] = fitnessValues[i];
            pBestLocation.add(flock.get(i).getLocation());
        }

        int t = 0;

        double err = 10000;

        while (t < 10 && err > 1E-10) {
            // step 1 - update pBest
            for (int i = 0; i < noOfBots; i++) {
                if (fitnessValues[i] > personalBest[i]) {
                    personalBest[i] = fitnessValues[i];
                    pBestLocation.set(i, flock.get(i).getLocation());
                }
            }

            // step 2 - update gBest
            int minIndex = getMaxPos(fitnessValues);
            System.out.println("The min position is " + minIndex);
            if (t == 0 || fitnessValues[minIndex] > globalBest) {
                globalBest = fitnessValues[minIndex];
                globalBestLoc = flock.get(minIndex).getLocation();
                gBestAdhesion = flock.get(minIndex).getAdhesionMolecule();
                gBestProtien = flock.get(minIndex).getProcal();
            }

            for (int i = 0; i < noOfBots; i++) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();

                Nanobot nb = flock.get(i);

                // step 3 - update velocity
                double[] newVel = new double[3];

                newVel[0] = (w * nb.getVelocity().getVelocity()[0])
                        + (r1 * C1) * (pBestLocation.get(i).getLocation()[0] - nb.getLocation().getLocation()[0])
                        + (r2 * C2) * (globalBestLoc.getLocation()[0] - nb.getLocation().getLocation()[0]);

                newVel[1] = (w * nb.getVelocity().getVelocity()[1])
                        + (r1 * C1) * (pBestLocation.get(i).getLocation()[1] - nb.getLocation().getLocation()[1])
                        + (r2 * C2) * (globalBestLoc.getLocation()[1] - nb.getLocation().getLocation()[1]);

                newVel[2] = (w * nb.getVelocity().getVelocity()[2])
                        + (r1 * C1) * (pBestLocation.get(i).getLocation()[2] - nb.getLocation().getLocation()[2])
                        + (r2 * C2) * (globalBestLoc.getLocation()[2] - nb.getLocation().getLocation()[2]);

                BotVelocity vel = new BotVelocity(newVel);
                nb.setVelocity(vel);

                // step 4 - update location
                double[] newLoc = new double[3];
                newLoc[0] = nb.getLocation().getLocation()[0] + newVel[0];
                newLoc[1] = nb.getLocation().getLocation()[1] + newVel[1];
                newLoc[2] = nb.getLocation().getLocation()[2] + newVel[2];

                BotLocation loc = new BotLocation(newLoc);
                nb.setLocation(loc);

                System.out.println(" Iteration " + t + " : ");
                System.out.println(" x is : " + nb.getLocation().getLocation()[0]);
                System.out.println(" y is : " + nb.getLocation().getLocation()[1]);
                System.out.println(" z is : " + nb.getLocation().getLocation()[2]);
            }

            err = botFit.fitnessCal(globalBestLoc, gBestProtien, gBestAdhesion) - 0; // minimizing the functions means it's getting closer to 0

            System.out.println("ITERATION " + t + ": ");
            System.out.println("     Best X: " + globalBestLoc.getLocation()[0]);
            System.out.println("     Best Y: " + globalBestLoc.getLocation()[1]);
            System.out.println("     Best z: " + globalBestLoc.getLocation()[2]);
            System.out.println("     Highest Protien  " + gBestProtien);
            System.out.println("     Less Ashesion  " + gBestAdhesion);
            System.out.println("     Value: " + botFit.fitnessCal(globalBestLoc, gBestProtien, gBestAdhesion));

            t++;
            swarmFitnessUpdate();
        }

        System.out.println("\nBest Solution at the end of iteration " + (t - 1) + ", the solutions is:");
        System.out.println("     Best X: " + globalBestLoc.getLocation()[0]);
        System.out.println("     Best Y: " + globalBestLoc.getLocation()[1]);
        System.out.println("     Best z: " + globalBestLoc.getLocation()[2]);
    }
}
