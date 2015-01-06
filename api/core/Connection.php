<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 05/01/15
 * Time: 18.33
 */

namespace core;

class Connection {
    /**
     * @var \PDO
     */
    private $con;

    private static $instance;

    private function __construct() {
        $this->con = new \PDO(
            "mysql:host=127.0.0.1:3306;dbname=myweathernow",
            "root",
            "",
            array(\PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8")
        );
        $this->con->setAttribute( \PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION );
    }

    public static function getInstance() {
        if ( self::$instance == null ) {
            self::$instance = new Connection();
        }

        return self::$instance;
    }

    /**
     * @return \PDO
     */
    public function getCon() {
        return $this->con;
    }
}