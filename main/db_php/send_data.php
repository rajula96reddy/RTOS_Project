<?php
   $con=mysqli_connect("172.16.86.196","root","toor","rtos");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }

   $uid = $_REQUEST['uid'];
   $bssid = $_REQUEST['bssid'];
   $sql = "INSERT into track_info (uid,bssid) VALUES ('$uid','$bssid')";
   if (mysqli_query($con,$sql)) {
      echo "Values have been inserted successfully";
   }
   mysqli_close($con);
?>
