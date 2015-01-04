<?php

include "core/Autoloader.php";

$register = new \core\Register();

$register->doAction();

echo json_encode($register->getResponse());