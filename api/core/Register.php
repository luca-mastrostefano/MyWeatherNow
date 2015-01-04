<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 04/01/15
 * Time: 16.16
 */

namespace core;

class Register {
    private $response;

    public function __construct() {
        $this->response = array(
            'id' => -1
        );
    }

    public function doAction(){
        //return the unique ID
        $this->response['id'] = 1;
    }

    /**
     * @return array
     */
    public function getResponse() {
        return $this->response;
    }
} 