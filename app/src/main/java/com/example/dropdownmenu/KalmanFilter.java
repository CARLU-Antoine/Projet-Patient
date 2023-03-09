package com.example.dropdownmenu;

// Implement the Kalman filter class
class KalmanFilter {
    private double Q, R, P, X;
    private boolean firstRun = true;

    public KalmanFilter(double Q, double R) {
        this.Q = Q;
        this.R = R;
        this.P = 1;
        this.X = 0;
    }

    public double getP() {
        return P;
    }

    public double getQ() {
        return Q;
    }

    public double getR() {
        return R;
    }

    public double getX() {
        return X;
    }


    public double update(double measurement) {
        if (firstRun) {
            firstRun = false;
            X = measurement;
            return X;
        }

        // Predict
        double Xp = X;
        P += Q;

        // Update
        double K = P / (P + R);
        X = Xp + K * (measurement - Xp);
        P = (1 - K) * P;

        return X;
    }
}