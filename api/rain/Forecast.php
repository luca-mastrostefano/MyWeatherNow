<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 04/01/15
 * Time: 17.06
 */

namespace rain;

class Forecast
{

    private $id;

    private $type;

    private $con;

    private $response;

    private static $allowed_actions = array('overview', 'detailed');

    private static $allowed_when = array('today', 'tomorrow');

    const TABLE_PREFIX = "myweathernow_";

    public function __construct()
    {
        date_default_timezone_set("Europe/Rome");
        $filterArgs = array(
            'id' => array(
                'filter' => FILTER_SANITIZE_NUMBER_INT),
            'type' => array(
                'filter' => FILTER_SANITIZE_STRING)
        );

        $getInput = filter_input_array(INPUT_GET, $filterArgs);

        $this->id = $getInput['id'];
        $this->type = strtolower($getInput['type']);

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
            ),
            "message" => null
        );
    }

    public function doAction()
    {
        if (!in_array($this->type, self::$allowed_actions)) {
            $this->response['status'] = 400;
            $this->response['message'] = "Type value not allowed";
            return;
        }


        $forecastData = array();
        foreach(self::$allowed_when as $when){
            $daily_forecastData = $this->getDataFromForecastIO($when);
            if ($daily_forecastData == null) {
                $this->response['status'] = 500;
                return;
            }
            $forecastData[$when] = $this->formatResponse($daily_forecastData);
        }  
        $this->response = $forecastData;
    }

    private function getYesterdaysData()
    {
        //get yesterday's data
        $yesterday_data = $this->getForecastFromCache('yesterday');
        if (count($yesterday_data) > 0) {
            $yesterday_data = $yesterday_data[0];

            return array(
                'temp' => $yesterday_data['temperature'],
                'humidity' => $yesterday_data['humidity'],
                'wind_speed' => $yesterday_data['wind'],
                'wind_dir' => $yesterday_data['wind_dir'],
                'cloudiness' => $yesterday_data['cloudiness']
            );
        } else {
            $yesterday_data = null;
        }
        return $yesterday_data;
    }

    private function getForecastFromCache($when = 'today')
    {
        switch ($when) {
            case 'today'    :
                $sql_date = 'now()';
                break;
            case 'yesterday':
                $sql_date = 'date_sub(now(), interval 1 day )';
                break;
            default :
                $sql_date = 'now()';
                break;
        }

        $stmt = $this->con->prepare("select * from " . self::TABLE_PREFIX . "forecasts
                          where date between date_sub(" . $sql_date . ", interval 10 minute ) and " . $sql_date);

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

    private function getDataFromForecastIO($when)
    {
        if($when == 'tomorrow'){
            $t = ",".strtotime('tomorrow');
        }
        else{
            $t = ",".strtotime('today');
        }
        //call openweathermap
        $forecastData = file_get_contents(
            'https://api.forecast.io/forecast/94da895607b3b5ea9fb23c2b41c8fab4/41.54,12.27'.$t
        );
        $forecastData = json_decode($forecastData, true);
        return $forecastData;
    }

    private static function fToKelvin($temp)
    {
        return round((($temp - 32) / 1.8) + 273.15, 2);
    }

    private function formatResponse($response)
    {
        $result = array();
        $morningData    = array(
            'prob' => 0,
            'intensity' => 0
        );
        $afternoonData  = array();
        $eveningData    = array();
        $nightData      = array();
        $dailyData = array(
            'prob' => 0,
            'intensity' => 0
        ); 
        //evaluate avg values for 6 hour periods
        foreach($response['hourly']['data'] as $hourData){
            $hour = intval(date("G",$hourData['time']));
            if ($hour >= 0 && $hour > 6 ) {
                $nightData['prob'] += $hourData['precipProbability'];
                $nightData['intensity'] += $hourData['precipIntensity'];
            }
            else if( $hour >= 6 && $hour < 12 ){
                $morningData['prob'] += $hourData['precipProbability'];
                $morningData['intensity'] += $hourData['precipIntensity'];
            }
            else if( $hour >= 12 && $hour < 18 ){
                $afternoonData['prob'] += $hourData['precipProbability'];
                $afternoonData['intensity'] += $hourData['precipIntensity'];
            }
            else if( $hour >= 18 && $hour < 24 ){
                $eveningData['prob'] += $hourData['precipProbability'];
                $eveningData['intensity'] += $hourData['precipIntensity'];
            }
        }

        $nowHour = intval(date("G",  strtotime('now')));
        $morningData['prob']        /= 6;
        $morningData['intensity']   /= 6;
        if($nowHour <= (6+12)/2){
            $dailyData['prob'] += (1-$dailyData['prob']) * $morningData['prob'];
        }
        $afternoonData['prob']      /= 6;
        $afternoonData['intensity'] /= 6;
        if($nowHour <= (12+18)/2){
            $dailyData['prob'] += (1-$dailyData['prob']) * $afternoonData['prob'];
        }
        $eveningData['prob']        /= 6;
        $eveningData['intensity']   /= 6;
        if($nowHour <= 24){ //Always
            $dailyData['prob'] += (1-$dailyData['prob']) * $eveningData['prob'];
        }
        $nightData['prob']          /= 6;
        $nightData['intensity']     /= 6;
        if($nowHour >= 0){  //Always
            $dailyData['prob'] += (1-$dailyData['prob']) * $nightData['prob'];
        }
        
       
        $result = array(
            'daily' => array(
                'rainProb' => round($dailyData['prob'],2),
                'rainIntensity' => $response['daily']['data'][0]['precipIntensity'],
                'sentence' => "",
            ),
            'nexthour' => array(
                'rainProb' => $response['hourly']['data'][0]['precipProbability'],
                'rainIntensity' => $response['hourly']['data'][0]['precipIntensity'],
            ),
            'morning' => array(
                'rainProb' => $morningData['prob'],
                'rainIntensity' => $morningData['intensity'],
            ),
            'afternoon' => array(
                'rainProb' => round($afternoonData['prob'],2),
                'rainIntensity' => round($afternoonData['intensity'],4),
            ),
            'evening' => array(
                'rainProb' => round($eveningData['prob'],2),
                'rainIntensity' => round($eveningData['intensity'],4),
            ),
            'night' => array(
                'rainProb' => round($nightData['prob'],2),
                'rainIntensity' => round($nightData['intensity'],4),
            )
        );

        if($this->type == 'detailed') {
            $result['detailed'] = array();
            foreach( $response['hourly']['data'] as $hourData){
                $result['detailed'][] = array(
                    'timestamp'     => $hourData['time'],
                    'rainProb'      => rand(1,10)/10 + round( $hourData['precipProbability'], 2 ),
                    'rainIntensity' => rand(1,10)/10 + round( $hourData['precipIntensity'],   4 ),
                    'period'        => $this->fromTime2period($hourData['time']),
                );
            }
        }

        return $result;
    }

    private function fromTime2period($time){
            $hour = intval(date("G",$time));
            if ($hour >= 0 && $hour < 6 ) {
                return 'night';
            }
            else if( $hour >= 6 && $hour < 12 ){
                return 'morning';
            }
            else if( $hour >= 12 && $hour < 18 ){
                return 'afternoon';
            }
            else if( $hour >= 18 && $hour < 24 ){
                return 'evening';
            }
            var_dump($hour);exit;
    }

    /**
     * @return array
     */
    public function getResponse()
    {
        return $this->response;
    }
}
