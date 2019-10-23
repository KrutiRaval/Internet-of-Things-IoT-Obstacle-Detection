<?php
	include('connect.inc.php');
        if ($_POST['action']=="UPDATE_LATLONG") {
		$lat = $_POST['lat'];
		$lon = $_POST['lon'];
		$query = "UPDATE `ObstacleDetection` SET `latitude` = $lat ,`longitude` = $lon";
		$query_run = mysqli_query($connect, $query);
		$num_rows = mysqli_affected_rows($connect);
		if ($num_rows>0) {
			echo "Successfully Updated";
		}
		else {
			echo "Something Went Wrong!";
		}
	}
?>