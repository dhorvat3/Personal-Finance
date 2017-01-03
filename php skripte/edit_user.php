<?php
/**
* Edit user profile attributes
* --- PARAMS ---
* ['id']
* ['username']
* ['password']
* ['email']
* ['name']
* ['surname']
* /User class/
* --- RESPONSE ---
* -2 => Empty field
* -1 => Error
* 1 => Success
**/
$response = array();

$data = json_decode(file_get_contents('php://input'), true);

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(empty($data['username']) or empty($data['password']) or empty($data['email']) or empty($data['name']) or empty($data['surname'])){
	$response['response'] = "Prazno polje!";
	$response['id'] = -2;
} else {
	//update user attributes in database
	$sql = "UPDATE users SET username='".$data['username']."', password='".$data['password']."', email='".$data['email']."', name='".$data['name']."', surname='".$data['surname']."' WHERE id = ".$data['id'].";";
	if(mysqli_query($con, $sql)){
		$response['response'] = "Promijenjeno!";
		$response['id'] = 1;
	} else {
		$response['response'] = "Greška!";
		$response['id'] = -1;
	}
}

echo json_encode($response);
?>