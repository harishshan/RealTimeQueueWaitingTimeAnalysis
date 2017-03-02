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
	        mainApp.controller('ViewDataDictionary', function($scope, $http) {
	        	$scope.treatmentTypeList=[
	        		{Name:"Surgery"},{Name:"Scan"},{Name:"OP"},{Name:"Pharmacy"}
	        	] 
	        });
      	</script>
	</head>
	<body>
		<div class="container">
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
					<div ng-app="mainApp" ng-controller="ViewDataDictionary" class="modal-body">
						<div class="form-group">
							<label>Patient Name</label>
							<input type="text" name="patientName" id="patientName" align="right" ng-model="patientName" placeholder="Enter Patient Name" class="form-control">
						</div>
						<div class="form-group">
							<label>Patient Id</label>
							<input type="text" name="patientId" id="patientId" align="right" ng-model="patientId" placeholder="Enter Patient Id" class="form-control">
						</div>
						<div class="form-group">
							<label>Patient Age</label><input type="text" name="patientAge" id="patientAge" align="right" ng-model="patientAge" placeholder="Enter Patient Age" class="form-control">
						</div>
						<div class="form-group">
							<label>Treatment Type</label>
							<select name="treatmentTypeSelect" id="treatmentTypeSelect" ng-model="treatmentTypeSelect" ng-change="treatmentTypeSelectChange()" class="form-control" >
									<option ng-repeat="treatmentType in treatmentTypeList" value="{{treatmentType.Name}}">{{treatmentType.Name}}</option>
							</select>							
						</div>
						<div class="form-group">
							<label>Location</label>
							<input type="text" name="location" id="location" align="right" ng-model="location" placeholder="Enter Location" class="form-control">
						</div>
						<div class="form-group">
							<label>In Time</label>
							<input type="text" name="inTime" id="inTime" align="right" ng-model="inTime" placeholder="Enter In Time" class="form-control">
						</div>
						<div class="form-group">
							<label>Token Number</label>
							<input type="text" name="tokenNumber" id="tokenNumber" align="right" ng-model="tokenNumber" placeholder="Enter Token Number" class="form-control">
						</div>
						<div class="form-group">
							<label>Doctor Name</label>
							<input type="text" name="doctorName" id="doctorName" align="right" ng-model="doctorName" placeholder="Enter Doctor Name" class="form-control">
						</div>
						<div class="form-group">
							<label>Treatment Start Time</label>
							<input type="text" name="treatmentStartTime" id="treatmentStartTime" align="right" ng-model="treatmentStartTime" placeholder="Enter Treatment Start Time" class="form-control">
						</div>
						<div class="form-group">
							<label>Treatment Completed Time</label>
							<input type="text" name="treatmentCompletedTime" id="treatmentCompletedTime" align="right" ng-model="treatmentCompletedTime" placeholder="Enter Treatment Completed Time" class="form-control">
						</div>
						<div class="form-group" align="right">
							<button type="submit" class="btn btn-success">Submit</button>
							<button type="button" class="btn btn-danger">Clear</button>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</body>
</html>