var app = angular.module('report', ['ngResource','ui.bootstrap','MessageCenterModule']);

app.controller('TaxReportController', 
		['$scope','$modal','$http','$log','messageCenterService',
	 function($scope, $modal, $http,$log,messageCenterService) {
	 
	 $scope.taxReportQuery = {};
	 
	 $scope.taxReportQuery.mode = 'CASH_BASIS';
	
	 $scope.apply = function() {
		 
		 if($scope.taxReportQuery.fromDate == null || $scope.taxReportQuery.thruDate == null) {
			return; 
		 }
		 
		 $http({
		  url : '/dexoffice/api/report/',
		  method : "POST",
		  data : {
			fromDate : $scope.taxReportQuery.fromDate,
			thruDate : $scope.taxReportQuery.thruDate,
			mode : $scope.taxReportQuery.mode
		  }
		 }).success(function(data, status, headers, config) {
			  $scope.taxes = data.taxes;
		  }).
		  error(function(data, status, headers, config) {
			  messageCenterService.add('warning', 'There was some problem whle preparing the report.', { status: messageCenterService.status.unseen,timeout: 5000});
		  });
	 }
}]);