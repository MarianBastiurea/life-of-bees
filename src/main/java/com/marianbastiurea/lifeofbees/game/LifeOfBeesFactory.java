package com.marianbastiurea.lifeofbees.game;

import com.marianbastiurea.lifeofbees.action.ActionOfTheWeek;
import com.marianbastiurea.lifeofbees.bees.Apiary;
import com.marianbastiurea.lifeofbees.bees.Hive;
import com.marianbastiurea.lifeofbees.weather.WeatherData;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;

public class LifeOfBeesFactory {

    public static LifeOfBees createLifeOfBeesGame(
            String gameName,
            String location,
            String startingDate,
            int numberOfStartingHives,
            String userId,
            String gameType,
           WeatherData weatherData
    ) {
        LocalDate date = LocalDate.parse(startingDate);
        double moneyInTheBank = 3000.0;
        double totalKgOfHoney = 0;
        Apiary apiary = new Apiary(new ArrayList<>());
        List<Hive> newHives = apiary.createHive(numberOfStartingHives, date);
        apiary.getHives().addAll(newHives);


        return new LifeOfBees(gameName, userId, gameType, apiary, location, date,
                weatherData, moneyInTheBank, totalKgOfHoney);
    }
}