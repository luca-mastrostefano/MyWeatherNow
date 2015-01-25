<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 04/01/15
 * Time: 17.06
 */

namespace core;

class Forecast
{

    private $id;

    private $con;

    private $response;

    const TABLE_PREFIX = "myweathernow_";

    public function __construct()
    {
        $filterArgs = array('id' => array('filter' => FILTER_SANITIZE_NUMBER_INT));

        $getInput = filter_input_array(INPUT_GET, $filterArgs);

        $this->id = $getInput['id'];

        if (0 && empty($this->id)) {
            $register = new Register();
            $register->doAction();
            $response = $register->getResponse();

            $this->id = $response['id'];
        }

        $this->con = Connection::getInstance()->getCon();

        $this->response = array(
            "status" => 200,
            "data" => array(
                "forecast" => array(
                    "temperature" => -1,
                    "humidity" => -1,
                    "wind_speed" => -1,
                    "wind_direction" => -1,
                    "wind_cardinal_direction" => null
                ),
                "sentence" => null,
                "id" => $this->id
            )
        );

    }

    public function doAction(){

        //get today's data
        $cachedForecast = $this->getForecastFromCache();
        $cache_hit = false;
        if (count($cachedForecast) > 0) {
            $cache_hit = true;
            $forecastData = $cachedForecast[0];

            $temperature = $forecastData['temperature'];
            $humidity = $forecastData['humidity'];
            $wind = $forecastData['wind'];
            $wind_dir = $forecastData['wind_dir'];
            $cloudiness = $forecastData['cloudiness'];
        } else {
            $forecastData = $this->getDataFromOpenWeatherMap();

            if ($forecastData == null) {
                $this->response['status'] = 500;
                return;
            }
            $temperature = $forecastData['main']['temp'];
            $humidity = $forecastData['main']['humidity'];
            $wind = $forecastData['wind']['speed'];
            $wind_dir = $forecastData['wind']['deg'];
            $cloudiness = $forecastData['clouds']['all'];
        }

        $yesterday_data = $this->getYesterdaysData();
        $sentence = SentenceMaker::makeSentence(
                                 $yesterday_data,
                                     array(
                                         'temp' => $temperature,
                                         'humidity'=> $humidity,
                                         'wind_speed' => $wind,
                                         'wind_dir' => $wind_dir,
                                         'cloudiness' => $cloudiness
                                     )
        );

        $this->response['data']['forecast']['temperature'] = $temperature;
        $this->response['data']['forecast']['humidity'] = $humidity;
        $this->response['data']['forecast']['wind_speed'] = $wind;
        $this->response['data']['forecast']['wind_direction'] = $wind_dir;
        $this->response['data']['forecast']['wind_cardinal_direction'] = SentenceMaker::windDegreesToDirection($wind_dir);
        $this->response['data']['forecast']['cloudiness'] = $cloudiness;
        $this->response['data']['sentence'] = utf8_encode($sentence);

        if (!$cache_hit) {
            $this->storeForecastData(
                 array(
                     'city' => "Rome",
                     'temp' => $temperature,
                     'hum' => $humidity,
                     'wind' => $wind,
                     'w_dir' => $wind_dir,
                     'rain' => 0,
                     'cloudiness' => $cloudiness
                 )
            );
        }
    }

    private function getYesterdaysData(){
        //get yesterday's data
        $yesterday_data = $this->getForecastFromCache('yesterday');
        if(count($yesterday_data) > 0 ){
            $yesterday_data = $yesterday_data[0];

            return array(
                'temp' => $yesterday_data['temperature'],
                'humidity'=> $yesterday_data['humidity'],
                'wind_speed' => $yesterday_data['wind'],
                'wind_dir' => $yesterday_data['wind_dir'],
                'cloudiness' => $yesterday_data['cloudiness']
            );
        }
        else{
            $yesterday_data = null;
        }
        return $yesterday_data;
    }

    private function getForecastFromCache($when = 'today'){
        switch($when){
            case 'today'    : $sql_date = 'now()'; break;
            case 'yesterday': $sql_date = 'date_sub(now(), interval 1 day )'; break;
            default :
                $sql_date = 'now()'; break;
        }

        $stmt = $this->con->prepare("select * from " . self::TABLE_PREFIX . "forecasts
                          where date between date_sub(".$sql_date.", interval 10 minute ) and ".$sql_date);

        try {
            $stmt->execute();
            $result = $stmt->fetchAll(\PDO::FETCH_ASSOC);

        } catch (\PDOException $e) {
            return array();
        }

        return $result;
    }

    private function storeForecastData(Array $forecast_data)
    {
        $stmt = $this->con->prepare("insert into " . self::TABLE_PREFIX . "forecasts (city, temperature, humidity, wind, wind_dir, rain, cloudiness)
                  values (:city, :temp, :hum, :wind, :w_dir, :rain, :cloud)
        ");

        $stmt->execute(
             array(
                 ":city" => $forecast_data['city'],
                 ":temp" => $forecast_data['temp'],
                 ":hum" => $forecast_data['hum'],
                 ":wind" => $forecast_data['wind'],
                 ":w_dir" => $forecast_data['w_dir'],
                 ":rain" => $forecast_data['rain'],
                 ":cloud" => $forecast_data['cloudiness'],
             )
        );
    }

    private function getDataFromOpenWeatherMap()
    {
        //call openweathermap
        $forecastData = file_get_contents(
            'http://api.openweathermap.org/data/2.5/weather?q=Rome,it&lang=it&APPID=ef6e3bec66acec1e5d68aaaae65cad8e'
        );
        $forecastData = json_decode($forecastData, true);
        return $forecastData;
    }

    /**
     * @return array
     */
    public function getResponse()
    {
        return $this->response;
    }
}