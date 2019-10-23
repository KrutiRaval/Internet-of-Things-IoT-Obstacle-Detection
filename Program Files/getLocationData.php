<?php
	include('connect.inc.php');
	$query = "SELECT * FROM `ObstacleDetection`";
	$query_run = mysqli_query($connect, $query);
	$response = array();
	while ($location = mysqli_fetch_assoc($query_run)) {
		$response['latitude'] = $location['latitude'];
		$response['longitude'] = $location['longitude'];
	}
	$responseJSON = json_encode($response);
	$content_length = strlen($responseJSON);

	header("content-type:application/json");
	header("Content-length:".$content_length);

	echo $responseJSON;
?>	