var app = angular.module('customer', ['ngResource','MessageCenterModule']);

app.filter('customerName', function() {
	return function(customer) {
		if(customer != null) {
			return customer.currentFirstName + " " + customer.currentLastName;
		} else {
			return "";
		}
	};
});

app.filter('postalAddress', function() {
	return function(customer) {
		if(customer != null) {
			var found = null;
			
			console.log(customer.partyContactMechs);
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					
					console.log(element);
					
					if (element.type == "POSTAL_ADDRESS") {
						found = element;
					} 
				}
			}
			return found;
		} else {
			return "";
		}
	};
});

app.service('Customer', ['$resource',function($resource){
    return $resource("/dexoffice/customer/:id");
}]);

app.controller('CustomerQuickCreateEditController', ['$scope','$modalInstance','customer','Customer','messageCenterService', function($scope,$modalInstance,customer,Customer,messageCenterService) {
	if(customer.id) {
		$scope.customer = customer;
	} else {
		$scope.customer = new Customer();
	}
	
	$scope.save = function () {
	   $scope.customer.$save({},function(data) {
		   	messageCenterService.add('success', 'Customer has been added.', { status: messageCenterService.status.unseen,timeout: 5000});
		   	$modalInstance.close("save.success");
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while saving the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	        $modalInstance.close("save.error");
	   });
	};

	$scope.cancel = function () {
	   $modalInstance.dismiss('cancel');
	};
}]);

app.controller('CustomerDeleteController', ['$scope','$modalInstance','Customer','customer','messageCenterService',function($scope,$modalInstance,Customer,customer,messageCenterService) {
	//Customer.delete({id: 1}) //DELETE /realmen/1
	$scope.customer = customer;
	
	$scope.delete = function () {
		Customer.delete({id:$scope.customer.id},function(data) {
		   	messageCenterService.add('success', 'Customer was deleted.', { status: messageCenterService.status.unseen,timeout: 5000});
			$modalInstance.close('delete.success');
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while deleting the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	        $modalInstance.close('delete.error');
	    });	
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
}]);

app.controller('CustomerCreateController', ['$scope','Customer',function($scope,Customer) {
	$scope.customer = new Customer();
	
	$scope.save = function () {
		$scope.customer.$save();
		console.log('Save : ' + $scope.customer);
	};

	$scope.cancel = function () {
		console.log('Cancel : ' + $scope.customer);
	};
}]);

app.controller('CustomerShowController', ['$scope','$stateParams','Customer',function($scope,$stateParams,Customer) {
	$scope.id = $stateParams.id;
	
	$scope.customer = Customer.get({id : $scope.id},function(data) {
		$scope.addressData = $scope.address($scope.customer);
		$scope.mobilePhoneData = $scope.mobilePhone($scope.customer);
		$scope.emailData = $scope.email($scope.customer);
	});
	
	$scope.email = function(customer) {
		if(customer != null) {
			var found = null;
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "EMAIL") {
						found = element;
					} 
				}
			}
			return found;
		}
	}
	
	$scope.mobilePhone = function(customer) {
		if(customer != null) {
			var found = null;
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "MOBILE_NUMBER") {
						found = element;
					} 
				}
			}
			return found;
		}
	}
	
	$scope.address = function(customer) {
		if(customer != null) {
			var found = null;
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "POSTAL_ADDRESS") {
						found = element;
					} 
				}
			}
			return found;
		}
	}
	
}]);

app.controller('CustomerListController', ['$scope','$modal','$log','Customer','messageCenterService',function($scope,$modal,$log,Customer,messageCenterService) {
	$scope.customers = [];
	
	$scope.listAllCustomers = function() {
		Customer.query(function(data) {
			$scope.customers = data;
		},function(error){
		    messageCenterService.add('warning', 'There was some error..', { status: messageCenterService.status.permanent,timeout: 5000});
		});
	}
	
	$scope.confirmDelete = function(cust) {
		$scope.customer = cust;
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/deleteConfirmation.html',
			controller : 'CustomerDeleteController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    }
			}
		});

		modalInstance.result.then(function(data) {
			$scope.listAllCustomers();
		}, function() {
			//$log.info('Modal dismissed at: ' + customer);
		});
	}
	
	$scope.edit = function(cust) {
		$scope.customer = cust;
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/quickCreate.html',
			controller : 'CustomerQuickCreateEditController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    }
			}
		});

		modalInstance.result.then(function(data) {
			$scope.listAllCustomers();
		}, function() {
			$log.info('Modal dismissed at: ' + customer);
		});
	}
	
	$scope.addCustomer = function() {
		//Opens a modal
		$scope.customer = {};
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/quickCreate.html',
			controller : 'CustomerQuickCreateEditController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    }
			}
		});

		modalInstance.result.then(function(data) {
		   $scope.listAllCustomers();
		}, function() {
			$log.info('Modal dismissed at: ');
		});
	}
	
	//List all customers
	$scope.listAllCustomers();
}]);