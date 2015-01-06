<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 04/01/15
 * Time: 17.06
 */

namespace core;

class Forecast {

    private $id;

    private $con;

    private $response;

    public function __construct() {
        $filterArgs = array( 'id' => array( 'filter' => FILTER_SANITIZE_NUMBER_INT ) );

        $getInput = filter_input_array( INPUT_GET, $filterArgs );

        $this->id = $getInput[ 'id' ];

        if ( empty( $this->id ) ) {
            $register = new Register();
            $register->doAction();
            $response = $register->getResponse();

            $this->id = $response[ 'id' ];
        }

        $this->con = Connection::getInstance()->getCon();

        $this->response = array(
            "status" => 200,
            "data" => array(
                "forecast" => array(
                    "temperature" => -1,
                    "humidity" => -1,
                    "windSpeed" => -1,
                    "windDirection" => -1
                ),
                "sentence" => null,
                "id" => $this->id
            )
        );
    }

    public function doAction(){

        $cachedForecast                                             = $this->getForecastFromCache();
        $cache_hit                                                  = false;
        if (count( $cachedForecast ) > 0 ) {
            $cache_hit    = true;
            $forecastData = $cachedForecast[ 0 ];

            $temperature = $forecastData[ 'temperature' ];
            $humidity    = $forecastData[ 'humidity' ];
            $windSpeed        = $forecastData[ 'windSpeed' ];
            $wind_dir    = $forecastData[ 'w_dir' ];
        } else {
            $forecastData = $this->getDataFromOpenWeatherMap();



            if ( $forecastData == null ) {
                $this->response[ 'status' ] = 500;

                return;
            }
            $temperature = $forecastData[ 'main' ][ 'temp' ];
            $humidity    = $forecastData[ 'main' ][ 'humidity' ];
            $windSpeed        = $forecastData[ 'windSpeed' ][ 'speed' ];
            $wind_dir    = $forecastData[ 'windSpeed' ][ 'deg' ];
            usleep( 1 );
        }

        $this->response['data']['forecast']['temperature'] = $temperature;
        $this->response['data']['forecast']['humidity'] = $humidity;
        $this->response['data']['forecast']['windSpeed'] = $windSpeed;
        $this->response[ 'data' ][ 'forecast' ][ 'windDirection' ] = $wind_dir;

        if ( !$cache_hit ) {
            $this->storeForecastData(
                 array(
                     'city' => "Rome",
                     'temp' => $temperature,
                     'hum' => $humidity,
                     'windSpeed' => $windSpeed,
                     'w_dir' => $wind_dir,
                     'rain' => 0
                 )
            );
        }
    }

    private function getForecastFromCache() {
        $stmt = $this->con->prepare( "select * from forecasts
                          where date between date_sub(now(), interval 10 minute ) and now()" );

        try {
            $stmt->execute();
            $result = $stmt->fetchAll( \PDO::FETCH_ASSOC );

        } catch ( \PDOException $e ) {
            return array();
        }

        return $result;
    }

    private function storeForecastData( Array $forecast_data ) {
        $stmt = $this->con->prepare( "insert into forecasts (city, temperature, humidity, windSpeed, wind_dir, rain)
                  values (:city, :temp, :hum, :windSpeed, :w_dir, :rain)
        " );

        $stmt->execute(
             array(
                 ":city" => $forecast_data[ 'city' ],
                 ":temp" => $forecast_data[ 'temp' ],
                 ":hum" => $forecast_data[ 'hum' ],
                 ":windSpeed" => $forecast_data[ 'windSpeed' ],
                 ":w_dir" => $forecast_data[ 'w_dir' ],
                 ":rain" => $forecast_data[ 'rain' ]
            )
        );
    }

    private function getDataFromOpenWeatherMap(){
        //call openweathermap
        $forecastData = file_get_contents(
            'http://api.openweathermap.org/data/2.5/weather?q=Rome,it&lang=it&APPID=ef6e3bec66acec1e5d68aaaae65cad8e'
        );
        $forecastData = json_decode( $forecastData, true );
        return $forecastData;
    }
    /**
     * @return array
     */
    public function getResponse() {
        return $this->response;
    }
}