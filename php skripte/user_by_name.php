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

if(isset($_GET['pass'])){
    $pass = $_GET['pass'];
} else {
    $pass = "";
}

$results = array();
$sql = "SELECT * FROM users WHERE username = '".$username."' AND password = '".$pass."';";
$results = mysqli_query($con ,$sql);

$response = mysqli_fetch_assoc($results);

echo json_encode($response);
?>