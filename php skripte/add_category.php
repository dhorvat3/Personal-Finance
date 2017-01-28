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
if(empty($data['title']) or empty($data['description']) or empty($data['user_id'])){
	$response['response'] = "Empty field!";
	$response['id'] = -2;
}
else{
	$sql = "INSERT INTO category VALUES (DEFAULT, '".$data['title']."', '".$data['description']."');";
	$results = mysqli_query($con, $sql);
	
	$cat_id = $con->insert_id;	
	$sql_ = "INSERT INTO user_category VALUES (".$data['user_id'].", ".$cat_id.", 1);";
	$results_ = mysqli_query($con, $sql_);
	
	if($results && $results_){
		$response['response'] = "Success";
		$response['id'] = 1;
	} else {
		$response['response'] = "SQL Error. Query: ".$sql;
		$response['id'] = -1;
	}
}

echo json_encode($response);
?>