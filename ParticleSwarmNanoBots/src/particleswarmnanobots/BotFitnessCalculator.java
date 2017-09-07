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
public class BotFitnessCalculator {


    private double pFactor = 0.0;
    private double adhFactor = 0.0;
    private double chanceFactor = 0.0;

    public double fitnessCal(BotLocation loc, double protien, double adh) {
        double result = 0;
        double x = loc.getLocation()[0];
        double y = loc.getLocation()[1];
        double z = loc.getLocation()[2];

        //converting cartesian to spherical for getting the required angle     
        double total = (x * x) + (y * y) + (z * z);
        double ro = Math.pow(total, 1 / 2);
        double phi = (double) Math.atan(y / x);
        double theta = (double) Math.acos(z / ro);

        chanceFactor = (adhesionRisk(adh) + protienRisk(protien)) * 1;

        result = chanceFactor / Math.pow(((total - ro)), 1 / 2);

        return result;
    }

    //The Protien Risk Factor
    
    private double protienRisk(double protien) {
        if (protien > 50 && protien <= 100) {
            pFactor = 0.1;
            return pFactor;
        } else if (protien > 100 && protien <= 130) {
            pFactor = 0.2;
            return pFactor;
        } else if (protien > 130 && protien <= 180) {
            pFactor = 0.3;
            return pFactor;
        } else if (protien > 180 && protien <= 250) {
            pFactor = 0.9;
            return pFactor;
        } else {
            pFactor = 1.4;
        }
        return pFactor;
    }

    // The Adhesion Risk Factor
    
    private double adhesionRisk(double adh) {
        if (adh > 150) {
            adhFactor = 0.1;
            return adhFactor;
        } else if (adh >= 120 && adh < 150) {
            adhFactor = 0.2;
            return adhFactor;
        } else if (adh >= 90 && adh < 120) {
            adhFactor = 0.4;
            return adhFactor;
        } else if (adh >= 50 && adh < 90) {
            adhFactor = 0.9;
            return adhFactor;
        } else {
            adhFactor = 1.5;
        }
        return adhFactor;
    }
}
