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
public class NanobotPSOUpdateThread {

    //Swarm Size
    private int noOfBots = 50;

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
    private double[] xCoordinates = new double[2];
    private double[] yCoordinates = new double[2];
    private double[] zCoordinates = new double[2];
    private BotFitnessCalculator botFit = new BotFitnessCalculator();
    double protienAmt = 0.0;
    double adhesionNumber = 0.0;
    double[] initXlocation = new double[noOfBots];
    float[] pBestXlocation = new float[5000];

    Random rand = new Random();

    //Getters and Setters to define
    public NanobotPSOUpdateThread() {
//        initializeBots();
    }

    public double[] getInitXlocation() {
        return initXlocation;
    }

    public void setInitXlocation(double[] initXlocation) {
        this.initXlocation = initXlocation;
    }

    public float[] getpBestXlocation() {
        return pBestXlocation;
    }

    public void setpBestXlocation(float[] pBestXlocation) {
        this.pBestXlocation = pBestXlocation;
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
            double total = (initLocation[0]*initLocation[0] + initLocation[1]*initLocation[1]+ initLocation[2]*initLocation[2]);
            initXlocation[i] = total;
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
//            System.out.println("The fitness functions are : " +flock.get(i).getFitness());
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

//        initializeBots();
        for (int i = 0; i < noOfBots; i++) {
            personalBest[i] = fitnessValues[i];
            pBestLocation.add(flock.get(i).getLocation());
        }
        int t = 0;
        double error = 10000;

        while (t < 10 && error > 1E-10) {
            
            //The PBest is updated
            //for all the individual nanobots
            for (int i = 0; i < noOfBots; i++) {
                if (fitnessValues[i] > personalBest[i]) {
                    personalBest[i] = fitnessValues[i];
                    pBestLocation.set(i, flock.get(i).getLocation());
                }
            }

            
            //Getting the best Chancefactor
            //calculating the protien quantity and the adhesion coefficient

            int maxIndex = getMaxPos(fitnessValues);
            if (t == 0 || fitnessValues[maxIndex] > globalBest) {
                globalBest = fitnessValues[maxIndex];
                globalBestLoc = flock.get(maxIndex).getLocation();
                gBestAdhesion = flock.get(maxIndex).getAdhesionMolecule();
                gBestProtien = flock.get(maxIndex).getProcal();
            }

            for (int i = 0; i < noOfBots; i++) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();

                Nanobot nb = flock.get(i);

                double[] newVel = new double[3];
                
                //The Velocity is updated with the required coefficients
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

                
                double[] newLoc = new double[3];
                //The Location for x,y and z are updated with
                //new velocity
                newLoc[0] = nb.getLocation().getLocation()[0] + newVel[0];
                newLoc[1] = nb.getLocation().getLocation()[1] + newVel[1];
                newLoc[2] = nb.getLocation().getLocation()[2] + newVel[2];

                BotLocation loc = new BotLocation(newLoc);
                nb.setLocation(loc);
                double total = (newLoc[0]*newLoc[0] + newLoc[1]*newLoc[1] + newLoc[2]+newLoc[2]);
                pBestXlocation[0] = (float) total;
                
            }

            error = botFit.fitnessCal(globalBestLoc, gBestProtien, gBestAdhesion) - 0; 
            // minimizing the functions means it's getting closer to 0

            t++;
            swarmFitnessUpdate();
        }

        System.out.println("\nBest Solution at the end of iteration " + (t - 1) + ", the solutions is:");
        System.out.println("     Global Best X : " + globalBestLoc.getLocation()[0]);
        System.out.println("     Global Best Y : " + globalBestLoc.getLocation()[1]);
        System.out.println("     Global Best z : " + globalBestLoc.getLocation()[2]);
        System.out.println("     Adhesion Number : " + gBestAdhesion);
        System.out.println("     Protien Quantity : " + gBestProtien);
        System.out.println("---------------------------------------------------------------------------");
    }
}
