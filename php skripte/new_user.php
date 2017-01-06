<?php
/**
* Create new user.
* --- PARAMS ---
* ['username']
* ['password']
* ['email']
* ['name']
* ['surname']
* /User class/
**/
$response = array();

$data = json_decode(file_get_contents('php://input'), true);

require_once __DIR__.'/db.php';
$db = new DB_CONNECT();
$con = $db->connect();

if(empty($data['username']) or empty($data['password']) or empty($data['email']) or empty($data['name']) or empty($data['surname'])){
	$response['response'] = "Empty field";
} else {
	//check if username exists
	$sql = "SELECT * FROM users WHERE username = '".$data['username']."';";
	$result = $con->query($sql);
	
	//add user
	if($result->num_rows === 0){
		$sql = "INSERT INTO users VALUES (DEFAULT, '".$data['username']."', '".$data['password']."', '".$data['email']."', '".$data['name']."', '".$data['surname']."');";
		
		if($con->query($sql) === TRUE){
			//get id
			$sql = "SELECT * FROM users WHERE username = '".$data['username']."';";
			$result = $con->query($sql);
			$id = mysqli_fetch_assoc($result);
			
			$response['response'] = "Added";
			$response['id'] = $id['id'];
		} else {
			$response['response'] = "Error: ". $sql;
		}
	} else {
		$response['response'] = "User exists";
	}
	$con->close();
}
echo json_encode($response);
?>