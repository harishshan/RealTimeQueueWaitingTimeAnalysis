<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>Treatment Completed | Real Time Queue Waiting Time Analysis</title>
		<link href="https://fonts.googleapis.com/css?family=Merriweather" rel="stylesheet">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
		<link rel="stylesheet" href="//cdn.jsdelivr.net/alertifyjs/1.9.0/css/alertify.min.css" />
		<link rel="stylesheet" href="//cdn.jsdelivr.net/alertifyjs/1.9.0/css/themes/bootstrap.min.css"/>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.2/angular.min.js"></script>
		<script src="https://cdn.jsdelivr.net/alertifyjs/1.9.0/alertify.min.js"></script>		
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha/css/bootstrap.min.css">
		<!--<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha/js/bootstrap.min.js"></script> -->
		<script>
	        var mainApp = angular.module("mainApp", []);
	        mainApp.controller('treatmentCompletedView', function($scope, $http) {
	        	var url = "getTreatmentCompletedPatientList";
        		$http.get(url).success( function(response) {
        			$scope.patientDetailsList = response; 
        		});
	        });
      	</script>
	</head>
	<body>
		<div class="container">
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<ul class="nav navbar-nav">
					<li><a href="/rtqwta/">Patient Admission</a></li>
					<li><a href="treatment.jsp">Treatment</a></li>					
					<li class="active"><a href="treatmentCompleted.jsp">Complete Treatment</a></li>
					<li><a href="historicalTreatmentCompleted.jsp">Historical Complete Treatment</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
					<li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
				</ul>
			</div>
		</nav>
			<div class="page-header" align="center" style="border: none;">
				<table>
					<tr>
						<td style="font-family: 'Merriweather', serif; font-size: 22pt; vertical-align: bottom; align-content: right; color: #007AB0; font-weight: bold;">
							Real Time Queue Waiting Time Analytics
						</td>
						<td rowspan="2">
							<img src="image/logo.png" width="100px" height="100px"></img>
						</td>
					</tr>
					<tr>
						<td align="right" style="font-family: 'Merriweather', serif; font-size: 4; vertical-align: top; align-content: right; color: #007AB0;">
							<i>powered by</i>
						</td>
					</tr>
				</table>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">
                    <h3 class="panel-title">Treatment Completed</h3>
                </div>			
				<div class="panel-body">
					<div ng-app="mainApp" ng-controller="treatmentCompletedView" class="modal-body">
						<div class="table-responsive">
							<table  align="center"  class="table table-hover table-bordered">
								<thead class="thead-inverse">									
									<tr>
										<td>Patient ID</td>										
										<td>Patient Name</td>
										<td>Patient Age</td>
										<td>Location</td>
										<td>Treatment Type</td>
										<td>Token Number</td>
										<td>Doctor</td>
										<td>Admission Timestamp</td>
										<td>Start Time</td>
										<td>Complete Time</td>
									</tr>
								</thead>
								<tbody>
									<tr ng-repeat="patientDetails in patientDetailsList">
										<th scope="row">{{patientDetails.patient_id}}</th>
										<td>{{patientDetails.patient_name}}</td>
										<td>{{patientDetails.patient_age}}</td>
										<td>{{patientDetails.location}}</td>
										<td>{{patientDetails.treatment_type}}</td>
										<td>{{patientDetails.token_number}}</td>
										<td>{{patientDetails.doctor}}</td>
										<td>{{patientDetails.admission_ts}}</td>
										<td>{{patientDetails.treatment_start_ts}}</td>
										<td>{{patientDetails.treatment_complete_ts}}</td>
									</tr>
								<tbody>
							</table>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</body>
</html>