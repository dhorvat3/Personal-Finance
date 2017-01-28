<?php
/**
* Delete task by id
**/

$response = array();
require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(isset($_GET['id'])){
	$id = $_GET['id'];
} else {
	$response['response'] = 'Empty id';
	$response['id'] = -2;
}

$sql = "UPDATE tasks SET aktivan = 0 WHERE id = ".$id.";";
$result = mysqli_query($con, $sql);

if(!$result){
	$response['response'] = $con->error;
	$response['id'] = -1;
} else {
	$response['response'] = "Success";
	$response['id'] = 1;
}

echo json_encode($response);
?>