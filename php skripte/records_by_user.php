<?php
/**
* Get records by user id
**/

$response = array();$response['record'] = array();

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(isset($_GET['id'])){
	$id = $_GET['id'];
} else {
	$id = "";
}

$results = array();
$sql = "SELECT * FROM revenues_expenses WHERE user_id = ".$id." AND aktivan = 1;";
$results = mysqli_query($con, $sql);


while($row = mysqli_fetch_assoc($results)){
	array_push($response['record'], $row);
}

echo json_encode($response);
?>