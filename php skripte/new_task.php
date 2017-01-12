<?php
/**
* Create new taske
* --- PARAMS ---
* ['user_id']
* ['title']
* ['note']
* ['date']
* ['notice']
* /Task class/
**/

$response = array();

$data = json_decode(file_get_contents('php://input'), true);

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(empty($data['user_id']) or empty($data['title']) or empty($data['note']) or empty($data['date']) or empty($data['notice'])){
	$response['response'] = "Empty field";
	$response['id'] = -2;
} else {
	//insert into table
	$sql = "INSERT INTO tasks VALUES (DEFAULT, ".$data['user_id'].", '".$data['title']."', '".$data['note']."', '".$data['date']."', '".$data['notice']."');";
	$result = mysqli_query($con, $sql);
	//check if inserted
	if($result){
		$response['response'] = "Success";
		$response['id'] = 1;
	} else {
		$response['response'] = "SQL Error. Query: ".$sql;
		$response['id'] = -1;
	}
}

json_encode($response);
?>