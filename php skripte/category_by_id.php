<?php
/**
* Get category data by id
**/

$response = array();

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(isset($_GET['id'])){
	$id = $_GET['id'];
} else {
	$id = "";
}

$results = array();
$sql = "SELECT * FROM category WHERE id = ".$id.";";
$results = mysqli_query($con, $sql);

$response = mysqli_fetch_assoc($results);

echo json_encode($response);
?>