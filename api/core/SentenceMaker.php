<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 06/01/15
 * Time: 20:55
 */

namespace core;

class SentenceMaker{

    /**
     * @param $yesterday_data
     * @param $today_data
     */
    public static function makeSentence($yesterday_data, $today_data)
    {
        //analyze temperature
        $deltaTemperature = $yesterday_data['temp'] - $today_data['temp'];

        $sentence['temp'] = "";
        if ($deltaTemperature < -3) {
            //today is hotter
            $sentence['temp'] = "Oggi fa più caldo di ieri";

            if ($deltaTemperature < -8) {
                $sentence['temp'] = "Oggi fa molto più caldo di ieri";
            }
        } else if ($deltaTemperature > 3) {
            //today is cooler
            $sentence['temp'] = "Oggi fa più freddo di ieri";

            if ($deltaTemperature < -8) {
                $sentence['temp'] = "Oggi fa molto più freddo di ieri";
            }
        } else {
            //almost same temperature
            $sentence['temp'] = "Oggi la temperatura è la stessa di ieri";
        }

        //analyze wind
        $deltaWindSpeed = $yesterday_data['wind_speed'] - $today_data['wind_speed'];

        $sentence['wind_speed'] = "";
        if ($deltaWindSpeed < -3) {
            //today is hotter
            $sentence['wind_speed'] = "il vento è più intenso";

            if ($deltaWindSpeed < -8) {
                $sentence['wind_speed'] = "c'è molto piu vento";
            }
        } else if ($deltaWindSpeed > 3) {
            //today is cooler
            $sentence['wind_speed'] = "il vento è meno forte";

            if ($deltaWindSpeed > 8) {
                $sentence['wind_speed'] = "c'è molto meno vento";
            }
        } else {
            //almost same temperature
            $sentence['wind_speed'] = "il vento è lo stesso di ieri";
        }

        //analyze wind direction
        $yesterday_windDirection = self::windDegreesToDirection($yesterday_data['wind_dir']);
        $today_windDirection = self::windDegreesToDirection($today_data['wind_dir']);

        if ($yesterday_windDirection != $today_windDirection) {
            $sentence['wind_dir'] = "ma oggi proviene da " .$today_windDirection;
        }

        //analyze humidity
        //analyze temperature
        $deltaHumidity = $yesterday_data['humidity'] - $today_data['humidity'];

        $sentence['humidity'] = "";
        if ($deltaHumidity < -3) {
            //today is hotter
            $sentence['humidity'] = "ed è più umido";

            if ($deltaHumidity < -8) {
                $sentence['humidity'] = "ed è molto più umido";
            }
        } else if ($deltaHumidity > 3) {
            //today is cooler
            $sentence['humidity'] = "ed è più secco";

            if ($deltaHumidity < 8) {
                $sentence['humidity'] = "ed è molto più secco";
            }
        } else {
            //almost same temperature
            $sentence['humidity'] = "";
        }

        //analyze rain

        return $sentence['temp']." ".$sentence['humidity'].", ".$sentence['wind_speed'].", ".$sentence['wind_dir'];
    }

    public static function windDegreesToDirection($degrees)
    {
        //north
        if ($degrees >= 348.75 || $degrees < 11.25) {
            return "N";
        } else if ($degrees >= 11.25 && $degrees < 33.75) {
            return "NNE";
        } else if ($degrees >= 33.75 && $degrees < 56.25) {
            return "NE";
        } else if ($degrees >= 56.25 && $degrees < 78.75) {
            return "ENE";
        } else if ($degrees >= 78.75 && $degrees < 101.25) {
            return "E";
        } else if ($degrees >= 101.25 && $degrees < 123.75) {
            return "ESE";
        } else if ($degrees >= 123.75 && $degrees < 146.25) {
            return "SE";
        } else if ($degrees >= 146.25 && $degrees < 168.75) {
            return "SSE";
        } else if ($degrees >= 168.75 && $degrees < 191.25) {
            return "S";
        } else if ($degrees >= 191.25 && $degrees < 213.75) {
            return "SSW";
        } else if ($degrees >= 213.75 && $degrees < 236.25) {
            return "SW";
        } else if ($degrees >= 236.25 && $degrees < 258.75) {
            return "WSW";
        } else if ($degrees >= 258.75 && $degrees < 281.25) {
            return "W";
        } else if ($degrees >= 281.25 && $degrees < 303.75) {
            return "WNW";
        } else if ($degrees >= 303.75 && $degrees < 326.25) {
            return "NW";
        } else if ($degrees >= 326.25 && $degrees < 348.75) {
            return "NNW";
        }
    }

} 