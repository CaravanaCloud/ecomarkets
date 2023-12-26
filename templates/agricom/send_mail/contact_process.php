<?php

error_reporting (E_ALL ^ E_NOTICE);

$post = (!empty($_POST)) ? true : false;

if($post)
{
	// define variables and set to empty values
	$name = $subject = $email = $phone = $message = "";

	function ValidateEmail($value)
	{
		$regex = '/^(?!(?:(?:\x22?\x5C[\x00-\x7E]\x22?)|(?:\x22?[^\x5C\x22]\x22?)){255,})(?!(?:(?:\x22?\x5C[\x00-\x7E]\x22?)|(?:\x22?[^\x5C\x22]\x22?)){65,}@)(?:(?:[\x21\x23-\x27\x2A\x2B\x2D\x2F-\x39\x3D\x3F\x5E-\x7E]+)|(?:\x22(?:[\x01-\x08\x0B\x0C\x0E-\x1F\x21\x23-\x5B\x5D-\x7F]|(?:\x5C[\x00-\x7F]))*\x22))(?:\.(?:(?:[\x21\x23-\x27\x2A\x2B\x2D\x2F-\x39\x3D\x3F\x5E-\x7E]+)|(?:\x22(?:[\x01-\x08\x0B\x0C\x0E-\x1F\x21\x23-\x5B\x5D-\x7F]|(?:\x5C[\x00-\x7F]))*\x22)))*@(?:(?:(?!.*[^.]{64,})(?:(?:(?:xn--)?[a-z0-9]+(?:-[a-z0-9]+)*\.){1,126}){1,}(?:(?:[a-z][a-z0-9]*)|(?:(?:xn--)[a-z0-9]+))(?:-[a-z0-9]+)*)|(?:\[(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){7})|(?:(?!(?:.*[a-f0-9][:\]]){7,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?)))|(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){5}:)|(?:(?!(?:.*[a-f0-9]:){5,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3}:)?)))?(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))(?:\.(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))){3}))\]))$/iD';

		if($value == '') {
			return false;
		} else {
			$string = preg_replace($regex, '', $value);
		}

		return empty($string) ? true : false;
	}

	function clean($value = "")
	{
		$value = trim($value);
		$value = stripslashes($value);
		$value = strip_tags($value);
		$value = htmlspecialchars($value);

		return $value;
	}

	function check_length($value = "", $min, $max)
	{
		$result = (mb_strlen($value) < $min || mb_strlen($value) > $max);

		return !$result;
	}

	$name    = clean( $_POST['name'] );
	$subject = clean( $_POST['subject'] );
	$email   = clean( $_POST['email'] );
	$phone   = clean( $_POST['phone'] );
	$message = clean( $_POST['message'] );
	$error = '';

	// Check name
	if( empty($name) || !check_length($name, 2, 25) )
	{
		$error .= 'Please enter your name. It should have at least 2-25 characters. ';
	}

	// Check email
	if( empty($email) )
	{
		$error .= 'Please enter an e-mail address. ';
	}

	if( !empty($email) && !ValidateEmail($email) )
	{
		$error .= 'Please enter a valid e-mail address. ';
	}

	// Check message (length)
	if( isset($_POST['message']) && (empty($message) || !check_length($message, 10, 1000)) )
	{
		$error .= 'Please enter your message. It should have at least 10 characters.';
	}

	$errorCode = 500;
	$successCode = 200;
	$code = $errorCode;

	if(!$error)
	{
		include('mail.php');

		$responseMail = sendMail($name, $email, $phone, $subject, $message);

		if ($responseMail === true)
			$code = $successCode;
		else
			$response['message'] = $responseMail;
	}
	else
	{
		$code = $errorCode;
		$response['message'] = $error;
	}

	// Return JSON response
	http_response_code($code);
	if ($response) {
		header('Content-Type: application/json');
		echo json_encode($response);
	}
}
?>