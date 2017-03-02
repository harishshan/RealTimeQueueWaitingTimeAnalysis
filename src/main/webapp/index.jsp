<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>Home</title>
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
	            var url = "getDataDictionary";
	            $scope.viewTableColumn=false;
	            
	            $scope.searchByKeyword = function(){
	                if($scope.searchKeyword==undefined || $scope.searchKeyword.length<3){
	                	alertify.alert('Info','Enter Minimum 3 Character to Search');
	                } else {
		                var url = "searchDataDictionaryByKeyword?keyword="+$scope.searchKeyword;
			                $http.get(url).success( function(response) {
			                $scope.dataDictionaries = response;
			                if(!$scope.dataDictionaries.length){
			                	$scope.viewTableColumn=false;
			            		alertify.alert('Info','Column/Table not found for the entered search criteria');
			            	}else{
			            		$scope.viewTableColumn=true;
			            	}
		            	});
	                }
	            }
	        });
      	</script>
	</head>
	<body>
		<div class="container" style="vertical-align: middle; width: 95%; max-width: 100rem;">
			<div class="page-header" align="center" style="border: none;">
				<table>
					<tr>
						<td style="font-family: 'Merriweather', serif; font-size: 22pt; vertical-align: bottom; align-content: right; color: #007AB0; font-weight: bold;">
							Data Dictionary
						</td>
						<td rowspan="2">
							<img src="image/DATA2.png" width="100px" height="100px"></img>
						</td>
					</tr>
					<tr>
						<td align="right" style="font-family: 'Merriweather', serif; font-size: 4; vertical-align: top; align-content: right; color: #007AB0;">
							<i>powered by</i>
						</td>
					</tr>
				</table>
			</div>
			<div class="panel panel-default" style="border: none; box-shadow: none;">				
				<div class="panel-body">
					<div ng-app="mainApp" ng-controller="ViewDataDictionary">
						<div class="table-responsive">
							<table class="table table-hover" align="center">
								<thead>
									<tr>
										<td align="right">
											<input type="text" id="searchKeyword" placeholder="Search By Column Name Or Table Name" name="searchKeyword" ng-model="searchKeyword" style="height:100%; width:60%;">
										</td>
										<td><button ng-click="searchByKeyword()" class="btn btn-success" type="submit">Search</button></td>
									</tr>
								</thead>
							</table>
							<table class="table table-hover table-bordered" ng-show="viewTableColumn">
								<thead class="thead-inverse">
									<tr>
										<th>#</th>
										<th>Schema Name</th>
										<th>Table Name</th>
										<th>Column Name</th>
										<th>Column Type</th>
										<th>Owner Name</th>
										<th>Description</th>
										<th>Additional Comments</th>
										<th>Scheduled Job Name</th>
										<th>Created Timestamp</th>
										<th>Updated Timestamp</th>
									</tr>
								</thead>
								<tbody>
									<tr ng-repeat="dataDictionary in dataDictionaries">
										<th scope="row">{{dataDictionary.ordinalPosition}}</th>
										<td style="width:150px;">{{dataDictionary.schemaName}}</td>
										<td style="width:150px;">{{dataDictionary.tableName}}</td>
										<td style="width:150px;">{{dataDictionary.columnName}}</td>
										<td>{{dataDictionary.columnType}}</td>
										<td>{{dataDictionary.ownerName}}</td>
										<td style="width:200px;">{{dataDictionary.description}}</td>
										<td style="width:150px;">{{dataDictionary.additionalComments}}</td>
										<td style="width:10px;">{{dataDictionary.scheduledJobName}}</td>
										<td>{{dataDictionary.createdTimestamp}}</td>
										<td>{{dataDictionary.updated_Timestamp}}</td>
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