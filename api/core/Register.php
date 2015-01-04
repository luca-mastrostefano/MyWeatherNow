<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 04/01/15
 * Time: 16.16
 */

namespace core;

class Register {

    private $email;

    private $username;

    private $response;

    public function __construct() {
        $filterArgs = array(
            'email' => array( 'filter' => FILTER_SANITIZE_EMAIL ),
            'username' => array( 'filter' => FILTER_SANITIZE_STRING,
                                 'flags' => array( FILTER_FLAG_STRIP_HIGH, FILTER_FLAG_STRIP_LOW ) )
        );

        $postInput = filter_input_array( INPUT_POST, $filterArgs );

        $this->email    = $postInput[ 'email' ];
        $this->username = $postInput[ 'username' ];
        $this->response = array(
            'status' => 200,
            'id' => -1
        );

        if( empty($this->email) || !preg_match("/[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}/", $this->email)){
            $this->response['status'] = 400;
        }


    }

    public function doAction(){
        if($this->response['status'] !== 200){
            return;
        }

        //save <email-username> pair

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