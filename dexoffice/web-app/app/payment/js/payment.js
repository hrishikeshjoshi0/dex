var app = angular.module('payment', ['ngResource','ui.bootstrap','MessageCenterModule','customer','invoice']);

app.factory('PaymentMethodType', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/paymentMethodTypes/:id";
    return $resource(
    		baseUrl,
    		{}
    );
}]);

app.factory('Payment', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/payment/:id";
    return $resource(
    		baseUrl,
    		{},
    		{
    			update: {
	    			url : baseUrl + "/update", 	
	    			method: 'PUT'
	    		},
	    		recordPaymentForInvoice : {
	    			url : baseUrl + "/recordPaymentForInvoice", 	
	    			method: 'POST'
	    		}
    		}
    );
}]);

app.controller('RecordPaymentController', ['$scope','$stateParams','messageCenterService','Customer','Payment','PaymentMethodType','Invoice',function($scope,$stateParams,messageCenterService,Customer,Payment,PaymentMethodType,Invoice) {
	$scope.paymentMethodTypes = PaymentMethodType.query();
	$scope.invoiceId = $stateParams.invoiceId;
	$scope.invoice = Invoice.get({id : $stateParams.invoiceId}, function(data) {
		$scope.payment = {};
		$scope.payment.partyFromId = $scope.invoice.party.id 
		$scope.payment.invoiceId = $scope.invoice.id
	});
	
	$scope.save = function () {
		Payment.recordPaymentForInvoice($scope.payment);
	};

	$scope.cancel = function () {
	   $modalInstance.dismiss('cancel');
	};
}]);

app.controller('PaymentListController', 
		['$scope','$modal','$http','$log','Payment','Customer','messageCenterService',
	 function($scope, $modal, $http,$log, Payment, Customer,messageCenterService) {
	 $scope.payments = Payment.query();		
}]);