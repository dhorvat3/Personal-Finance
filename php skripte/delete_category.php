<?php
/**
* Delete category
**/

$response = array();

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(isset($_GET['id']) && isset($_GET['user_id'])){
	$id = $_GET['id'];
	$user_id = $_GET['user_id'];
} else {
	$response['response'] = "Empty id";
	$response['id'] = -2;
}

	$sql = "UPDATE user_category SET active=0  WHERE category_id = '".$id."' AND user_id='".$user_id."' ;";
	$result = mysqli_query($con, $sql);

	$sql_ = "UPDATE revenues_expenses SET category_id=NULL  WHERE category_id = '".$id."' AND user_id='".$user_id."';";
	$result_ = mysqli_query($con, $sql_);

if(!$result  && !$result_){
	$response['response'] = $con->error;
	$response['id'] = -1;
} else {
	$response['response'] = "Success";
	$response['id'] = 1;
}

echo json_encode($response);
?>