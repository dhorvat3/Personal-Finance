<?php
/**
* Get categoryes by user id
**/

$response = array();
$response['category'] = array();

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(isset($_GET['id'])){
	$id = $_GET['id'];
} else {
	$id = "";
}

$results = array();
$sql = "SELECT * FROM user_category WHERE user_id = ".$id." AND active = 1;";
$results = mysqli_query($con, $sql);


while($row = mysqli_fetch_assoc($results)){
	$cat_id = $row['category_id'];
	$cat_results = array();
	$sql = "SELECT * FROM category WHERE id = ".$cat_id.";";
	$cat_results = mysqli_query($con, $sql);
	
	while($cat_row = mysqli_fetch_assoc($cat_results)){
		array_push($response['category'], $cat_row);
	}
}

echo json_encode($response);
?>