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

    private $con;

    public function __construct() {
        $this->response = array( 'id' => -1 );
        $this->con      = Connection::getInstance()->getCon();
    }

    public function doAction() {
        $stmt = $this->con->prepare( "insert into users (name) values (null)" );

        try {
            $inserted = $stmt->execute();
            if ( $inserted ) {
                //set the unique ID to be returned
                $this->response[ 'id' ] = $this->con->lastInsertId();
            }
        } catch ( \PDOException $e ) {
            return;
        }
    }

    /**
     * @return array
     */
    public function getResponse() {
        return $this->response;
    }
} 