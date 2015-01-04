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

        $this->response = array(
            "status" => 200,
            "data" => array(
                "forecast" => array(
                    "temperature" => -1,
                    "humidity" => -1,
                    "wind" => -1
                ),
                "sentence" => null,
                "id" => $this->id
            )
        );
    }

    public function doAction(){

        $forecastData = file_get_contents('http://api.openweathermap.org/data/2.5/weather?q=Rome,it');
        $forecastData = json_decode( $forecastData, true );
        if($forecastData == null){
            $this->response['status'] = 500;
            return;
        }
        $temperature = $forecastData['main']['temp'];
        $humidity = $forecastData['main']['humidity'];
        $wind = $forecastData['wind']['speed'];


        $this->response['data']['forecast']['temperature'] = $temperature;
        $this->response['data']['forecast']['humidity'] = $humidity;
        $this->response['data']['forecast']['wind'] = $wind;
    }

    /**
     * @return array
     */
    public function getResponse() {
        return $this->response;
    }
}