<?php
/**
 * Created by PhpStorm.
 * User: Roberto
 * Date: 04/01/15
 * Time: 15.57
 */

include "core/Autoloader.php";

$forecast = new \core\Forecast();
$forecast->doAction();
echo json_encode( $forecast->getResponse());