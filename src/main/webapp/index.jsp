<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>Home | Real Time Queue Waiting Time Analysis</title>
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
	        mainApp.controller('patientAdmission', function($scope, $http) {
	        	var url = "getTreatmentType";
	        	$scope.patient = {};
        		$http.get(url).success( function(response) {
        			$scope.treatmentTypeList = response; 
        		});
        		var url = "getNewPatientId";
        		$http.get(url).success( function(response) {
        			$scope.patient.patient_id = response; 
        		});
        		$scope.treatmentTypeSelectChange = function(){
        			var url = "getNewTokenNumber/"+$scope.patient.treatment_type;
        			$http.get(url).success( function(response) {
            			$scope.patient.token_number = response; 
            		});        			
        		}
        		$scope.admitPatient = function(){
        			if($scope.patient.patient_id==0 || $scope.patient.patient_id==""){
        				alertify.alert('Warn','Validation Error: Patient id must not be 0 or empty');
        			} else if($scope.patient.patient_name==""||$scope.patient.patient_name==undefined){
        				alertify.alert('Warn','Validation Error: Patient name must be entered');
        			}else if($scope.patient.patient_age=="" || $scope.patient.patient_age==undefined || (!angular.isNumber(+$scope.patient.patient_age))){
        				alertify.alert('Warn','Validation Error: Patient age must be entered an integer value');
        			}else if($scope.patient.location==""|| $scope.patient.location==undefined){
        				alertify.alert('Warn','Validation Error: Location must be entered');
        			}else if($scope.patient.treatment_type==""|| $scope.patient.treatment_type==undefined){
        				alertify.alert('Warn','Validation Error: Treatment type must be entered');
        			}else if($scope.patient.token_number==0 || $scope.patient.token_number=="" || $scope.patient.token_number ==undefined){
        				alertify.alert('Warn','Validation Error: Token number must not be 0 or empty');
        			} else {
        				var url = "admitPatient";        			
	        			$http.post(url,$scope.patient).success( function(response) {
	        				if(response.message!=undefined){
	        					alertify.alert('Info', response.message);
	        				}else if(response.error!=undefined){
	        					alertify.alert('Warn', response.error);
	        				}
	            		});   
        			}
        		}
	        });
      	</script>
	</head>
	<body>
		<div class="container">
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<ul class="nav navbar-nav">
					<li class="active"><a href="/rtqwta/">Patient Admission</a></li>
					<li><a href="treatment.jsp">Treatment</a></li>
					<li><a href="treatmentCompleted.jsp">Complete Treatment</a></li>
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
                    <h3 class="panel-title">Patient Admission</h3>
                </div>			
				<div class="panel-body">
					<div ng-app="mainApp" ng-controller="patientAdmission" class="modal-body">
						<div class="form-group">
							<label>Patient Id:</label>
							<input type="text" name="patient_id" id="patient_id" align="right" ng-model="patient.patient_id" class="form-control" readonly="readonly" required="required">
						</div>
						<div class="form-group">
							<label>Patient Name</label>
							<input type="text" name="patient_name" id="patient_name" align="right" ng-model="patient.patient_name" placeholder="Enter Patient Name" class="form-control" required="required">
						</div>						
						<div class="form-group">
							<label>Patient Age</label><input type="text" name="patient_age" id="patient_age" align="right" ng-model="patient.patient_age" placeholder="Enter Patient Age" class="form-control" required="required">
						</div>
						<div class="form-group">
							<label>Location</label>
							<input type="text" name="location" id="location" align="right" ng-model="patient.location" placeholder="Enter Location" class="form-control" required="required">
						</div>
						<div class="form-group">
							<label>Treatment Type</label>
							<select name="treatment_type" id="treatment_type" ng-model="patient.treatment_type" ng-change="treatmentTypeSelectChange()" class="form-control" required="required">
									<option ng-repeat="treatmentType in treatmentTypeList" value="{{treatmentType.Treatment_Type}}">{{treatmentType.Treatment_Type}}</option>
							</select>							
						</div>
						<div class="form-group">
							<label>Token Number</label>
							<input type="text" name="token_number" id="token_number" align="right" ng-model="patient.token_number" class="form-control" readonly="readonly" required="required">
						</div>
						<div class="form-group" align="right">
							<button type="button" class="btn btn-danger">Clear</button>
							<button type="submit" class="btn btn-success" ng-click="admitPatient()">Admit Patient</button>							
						</div>
					</div>	
				</div>
			</div>
		</div>
	</body>
</html>