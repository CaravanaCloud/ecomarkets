<?php

// Import PHPMailer classes into the global namespace
// These must be at the top of your script, not inside a function
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

//Load Composer's autoloader
require 'vendor/autoload.php';

function sendMail($name, $email, $phone, $subject, $message) {
	$mail = new PHPMailer(true);
	try {
		//Server settings
		$mail->isSMTP();                                      // Set mailer to use SMTP
		$mail->Host = 'smtp.gmail.com';                       // Specify main and backup SMTP servers
		$mail->SMTPAuth = true;                               // Enable SMTP authenication
		$mail->Username = 'gmaillogin@gmail.com';             // SMTP username
		$mail->Password = 'gmailpassword';                    // SMTP password
		$mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
		$mail->Port = 587;                                    // TCP port to connect to

		//Recipients
		$recipientEmail = 'gmaillogin@gmail.com';
		$mail->setFrom($recipientEmail, 'Mailer');
		$mail->addAddress($recipientEmail);
		$mail->addReplyTo($email);

		//Content
		$mail->isHTML(true);
		$mail->Subject = $subject;
		$mail->Body    = "Email: '$email'<br />Name: '$name'<br />Phone: '$phone'<br />Message: '$message'";
		$mail->AltBody = "Email: '$email'\nName: '$name'\nMessage: '$message'";

		$mail->send();

		return true;
	} catch (Exception $e) {
		$result["sent"] = false;
		$result["error"] = $mail->ErrorInfo;
		return $mail->ErrorInfo;
	}
}