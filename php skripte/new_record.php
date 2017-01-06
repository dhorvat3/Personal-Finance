<?php
/**
* Create new record
* --- PARAMS ---
* ['user_id']
* ['category_id']
* ['vrsta']
* ['napomena']
* ['datum']
* ['iznos']
* ['aktivan']
**/
$response = array();

$data = json_decode(file_get_contents('php://input'), true);

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(empty($data['user_id']) or empty($data['category_id']) or empty($data['vrsta']) or empty($data['datum']) or empty($data['iznos'])){
	$response['response'] = "Empty field!";
	$response['id'] = -2;
} else {
	$sql = "INSERT INTO revenues_expenses VALUES (DEFAULT, ".$data['user_id'].", ".$data['category_id'].", ".$data['vrsta'].", '".$data['napomena'].", '".$data['datum']."', ".$data['iznos'].", 1);";
	$results = mysqli_query($con, $sql);
	
	$response['response'] = "Success!";
	$response['id'] = 1;
}

echo json_encode($response);
?>