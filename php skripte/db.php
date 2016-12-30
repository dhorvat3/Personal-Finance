<?php

class DB_CONNECT {
	
	/**
	* Constructor 
	**/
	function __construct() {
		$this->connect();
	}
	

	/**
	* Connect to database
	**/
	function connect(){
		$DB_SERVER = 'localhost';
		$DB_USER = 'id434475_finance';
		$DB_PASS = 'analiza';
		$DB_DATABASE = 'id434475_finance';
		
		$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE) or die(mysqli_error());
		//$db = mysqli_select_db($DB_DATABASE) or die(mysqli_error()) or die(mysqli_error());
		
		return $con;
	}
}

?>