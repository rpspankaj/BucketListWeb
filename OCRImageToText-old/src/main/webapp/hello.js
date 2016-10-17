angular.module('demo', [])


.controller('Hello', function($scope, $http) {
$http.get("your_data").success(function (data) 
{ 
   $scope.bucKetLists = data.messages; 
   console.log("users" , $scope.users); 
}); 
});
