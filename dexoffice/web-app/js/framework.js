var framework = angular.module('framework', []);

var disabled = true;

framework.config(function ($httpProvider) {
  $httpProvider.responseInterceptors.push('myHttpInterceptor');
  var spinnerFunction = function spinnerFunction(data, headersGetter) {
    //$("#spinner").fadeIn(3000);
    return data;
  };

  $httpProvider.defaults.transformRequest.push(spinnerFunction);
});

framework.factory('myHttpInterceptor', function ($q, $window) {
  return function (promise) {
    return promise.then(function (response) {
      //$("#spinner").fadeOut(3000);
      return response;
    }, function (response) {
    	//$("#spinner").fadeOut(3000);
      return $q.reject(response);
    });
  };
});