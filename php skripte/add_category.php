<?php
/**
* Create new category and assing it to user
*
* --- PARAMS ---
* [user_id]
* [title]
* [description]
**/
$data = json_decode(file_get_contents('php://input'), true);
$response = array();

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

//Add new category
$results = array();
$sql = "INSERT INTO category VALUES (DEFAULT, '".$data['title']."', '".$data['description']."');";
$results = mysqli_query($con, $sql);

//Get id of inserted category
$cat_id = $con->insert_id;

//assign category to user
$results = array();
$sql = "INSERT INTO user_category VALUES (".$data['user_id'].", ".$cat_id.", 1);";
$results = mysqli_query($con, $sql);

?>