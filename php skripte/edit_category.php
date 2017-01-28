<?php
/**
* Edit category
**/

$response = array();

$data = json_decode(file_get_contents('php://input'), true);

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(empty($data['id'])){
	$response['response'] = "Empty id";
	$response['id'] = -2;
} else {
	$sql = "UPDATE category SET title = '".$data['title']."', description = '".$data['description']."' WHERE id = '".$data['id']."';";
	if(mysqli_query($con, $sql)){
		$response['response'] = "Success!";
		$response['id'] = 1;
	} else {
		$response['response'] = $con->error;
		$response['id'] = -1;
	}
}

echo json_encode($response);
?>