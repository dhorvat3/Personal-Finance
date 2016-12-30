<?php
/**
* Get user data by username
**/

$response = array();

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(isset($_GET['username'])){
	$username = $_GET['username'];
} else {
	$username = "";
}

$results = array();
$sql = "SELECT * FROM users WHERE username = '".$username."';";
$results = mysqli_query($con ,$sql);

$response = mysqli_fetch_assoc($results);

echo json_encode($response);
?>