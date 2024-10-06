package com.marianbastiurea.lifeofbees;


import java.util.*;


public class Whether implements IWeather{
    private double speedWind;// in km/h
    private double temperature;// in Celsius Degree
    private double precipitation;// in mm

    private static List<Whether> whetherList = new ArrayList<>();


    public Whether(double speedWind, double temperature, double precipitation) {
        this.speedWind = speedWind;
        this.temperature = temperature;
        this.precipitation = precipitation;

    }
    public double getSpeedWind() {
        return speedWind;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public static List<Whether> getWhetherList() {
        return whetherList;
    }

    public static void setWhetherList(List<Whether> whetherList) {
        Whether.whetherList = whetherList;
    }

    public Whether() {
    }

    @Override
    public String toString() {
        return "Whether{" +
                "speedWind=" + speedWind +
                ", temperature=" + temperature +
                ", precipitation=" + precipitation +
                '}';
    }

    public Whether whetherToday(HarvestingMonths month, int dayOfMonth) {
        Random random = new Random();
        Whether dailyWhether = new Whether();
        switch (month) {
            case MARCH:
                if (dayOfMonth >= 1 && dayOfMonth <= 15) {
                    dailyWhether = new Whether(random.nextDouble(1, 4),
                            random.nextDouble(10, 12),
                            random.nextDouble(0, 40)
                    );
                    return dailyWhether;
                } else if (dayOfMonth >15 && dayOfMonth <= 30) {
                    dailyWhether = new Whether(random.nextDouble(0, 4),
                            random.nextDouble(9, 15),
                            random.nextDouble(0, 70)
                    );
                    return dailyWhether;
                }
                break;
            case APRIL:
                if (dayOfMonth >= 1 && dayOfMonth <= 15) {
                    dailyWhether = new Whether(random.nextDouble(1, 3),
                            random.nextDouble(7, 12),
                            random.nextDouble(0, 40)
                    );
                    return dailyWhether;
                } else if (dayOfMonth >= 16 && dayOfMonth <= 30) {
                    dailyWhether = new Whether(random.nextDouble(3, 7),
                            random.nextDouble(10, 17),
                            random.nextDouble(0, 70)
                    );
                    return dailyWhether;
                }
                break;
            case MAY:
                if (dayOfMonth >= 1 && dayOfMonth <= 15) {
                    dailyWhether = new Whether(random.nextDouble(1, 3),
                            random.nextDouble(8, 14),
                            random.nextDouble(0, 60)
                    );
                    return dailyWhether;
                } else if (dayOfMonth >= 16 && dayOfMonth <= 30) {
                    dailyWhether = new Whether(random.nextDouble(3, 7),
                            random.nextDouble(14, 23),
                            random.nextDouble(0, 85)
                    );
                    return dailyWhether;
                }
            case JUNE:
                if (dayOfMonth >= 1 && dayOfMonth <= 15) {
                    dailyWhether = new Whether(random.nextDouble(1, 3),
                            random.nextDouble(17, 22),
                            random.nextDouble(0, 50)
                    );
                    return dailyWhether;
                } else if (dayOfMonth >= 16 && dayOfMonth <= 30) {
                    dailyWhether = new Whether(random.nextDouble(3, 7),
                            random.nextDouble(22, 27),
                            random.nextDouble(0, 80)
                    );
                    return dailyWhether;
                }
                break;
            case JULY:
                if (dayOfMonth >= 1 && dayOfMonth <= 15) {
                    dailyWhether = new Whether(random.nextDouble(1, 10),
                            random.nextDouble(18, 25),
                            random.nextDouble(0, 40)
                    );
                    return dailyWhether;
                } else if (dayOfMonth >= 16 && dayOfMonth <= 30) {
                    dailyWhether = new Whether(random.nextDouble(3, 7),
                            random.nextDouble(25, 33),
                            random.nextDouble(0, 60)
                    );
                    return dailyWhether;
                }
            case AUGUST:
                if (dayOfMonth >= 1 && dayOfMonth <= 15) {
                    dailyWhether = new Whether(random.nextDouble(1, 3),
                            random.nextDouble(18, 32),
                            random.nextDouble(0, 10)
                    );
                    return dailyWhether;
                } else if (dayOfMonth >= 16 && dayOfMonth <= 30) {
                    dailyWhether = new Whether(random.nextDouble(3, 7),
                            random.nextDouble(18, 28),
                            random.nextDouble(0, 30)
                    );
                    return dailyWhether;
                }
            case SEPTEMBER:
                if (dayOfMonth >= 1 && dayOfMonth <= 15) {
                    dailyWhether = new Whether(random.nextDouble(1, 3),
                            random.nextDouble(15, 27),
                            random.nextDouble(20, 40)
                    );
                    return dailyWhether;
                } else if (dayOfMonth >= 16 && dayOfMonth <= 30) {
                    dailyWhether = new Whether(random.nextDouble(3, 7),
                            random.nextDouble(10, 17),
                            random.nextDouble(0, 42)
                    );
                    return dailyWhether;
                }
            default:
                break;
        }
        return dailyWhether;
    }

    public double whetherIndex(Whether dailyWhether) {
       // System.out.println("whether today is"+dailyWhether);
        double rainIndex = 0;
        double temperatureIndex = 0;
        double speedWindIndex = 0;
        double whetherIndex = 0;
        // rainIndex by quantity of precipitation
        if (dailyWhether.precipitation <= 4) {
            rainIndex = 1;
        } else if (dailyWhether.precipitation > 4 && dailyWhether.precipitation <= 16) {
            rainIndex = 0.95;
        } else if (dailyWhether.precipitation > 16 && dailyWhether.precipitation <= 50) {
            rainIndex = 0.9;
        } else if (dailyWhether.precipitation > 50) {
            rainIndex = 0.7;
        }

        //temperatureIndex by Celsius Degree
        if (dailyWhether.temperature <= 10) {
            temperatureIndex = 0.8;
        } else if (dailyWhether.temperature > 10 && dailyWhether.temperature < 30) {
            temperatureIndex = 1;
        } else if (dailyWhether.temperature >= 30) {
            temperatureIndex = 0.8;
        }

        //speedWindIndex by wind speed
        if (dailyWhether.speedWind <= 10) {
            speedWindIndex = 1;
        } else if (dailyWhether.speedWind > 10 && dailyWhether.speedWind <= 20) {
            speedWindIndex = .8;
        } else if (dailyWhether.speedWind > 20 && dailyWhether.speedWind <= 30) {
            speedWindIndex = 0.75;
        } else if (dailyWhether.speedWind > 30) {
            speedWindIndex = 0.7;
        }
        whetherIndex = rainIndex * temperatureIndex * speedWindIndex;
        return whetherIndex;
    }

    public double whetherIndex(HarvestingMonths harvestingMonth, int dayOfMonth) {
        return whetherIndex(whetherToday(harvestingMonth, dayOfMonth));
    }
}