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

app.controller('RecordPaymentController', ['$scope','$stateParams','$location','messageCenterService','Customer','Payment','PaymentMethodType','Invoice',
                                           function($scope,$stateParams,$location,messageCenterService,Customer,Payment,PaymentMethodType,Invoice) {
	$scope.paymentMethodTypes = PaymentMethodType.query();
	$scope.invoiceId = $stateParams.invoiceId;
	$scope.invoice = Invoice.get({id : $stateParams.invoiceId}, function(data) {
		$scope.payment = {};
		$scope.payment.partyFromId = $scope.invoice.party.id; 
		$scope.payment.invoiceId = $scope.invoice.id;
		$scope.payment.effectiveDate = new Date();
		$scope.payment.amount = $scope.invoice.currentReceivableAmount;		
	});
	
	$scope.save = function () {
		Payment.recordPaymentForInvoice($scope.payment,function(data) {
			//If success, save the invoice ID.
			$scope.payment.id = data.id;
			
			messageCenterService.add('success', 'The payment has been recorded.' 
					, { status: messageCenterService.status.next,timeout: 5000,html:true});
		    $location.path("/invoice/show/"+$scope.payment.invoiceId);
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while saving the payment.', { status: messageCenterService.status.unseen,timeout: 5000});
	   });
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