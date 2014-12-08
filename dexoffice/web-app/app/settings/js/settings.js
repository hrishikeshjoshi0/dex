var app = angular.module('settings', ['ngResource','ui.bootstrap','MessageCenterModule']);

app.factory('SettingType', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/settingType/:id";
    return $resource(
    		baseUrl,
    		{}
    );
}]);

app.factory('Setting', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/setting/:id";
    return $resource(
    		baseUrl,
    		{},
    		{
    			getSettings : {
	    			url : baseUrl + "?settingType=:settingType", 	
	    			method: 'GET',
	    			isArray : true
	    		}
    		}
    );
}]);

app.controller('SettingsListController', 
		['$scope','$modal','$http','$log','messageCenterService','SettingType','Setting',
	 function($scope, $modal, $http,$log,messageCenterService,SettingType,Setting) {
	$scope.settingTypes = SettingType.query();
	
	$scope.settingTypeChanged =function(setting) {
		$scope.settings = Setting.getSettings({settingType : setting.code});
	}
}]);

app.controller('SettingsShowController', 
		['$scope','$stateParams','$modal','$http','$log','messageCenterService','SettingType','Setting',
	 function($scope,$stateParams, $modal, $http,$log,messageCenterService,SettingType,Setting) {
	
	$scope.id = $stateParams.id;
	$scope.setting = Setting.get({id : $scope.id},function(data) {
	});
}]);