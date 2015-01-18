<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 17/01/15
 * Time: 10.07
 */

header('Content-Type: application/json; charset=utf-8');

include "../Autoloader.php";

$forecast = new rain\Forecast();
$forecast->doAction();
echo json_encode( $forecast->getResponse() );