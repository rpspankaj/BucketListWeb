angular.module('demo', [])


.controller('Hello', function($scope, $http) {
$http.get("https://hack-rest.herokuapp.com/bucketLists?groupId=1").success(function (data) 
{ 
   $scope.bucKetLists = data.messages; 
   console.log("BUckets" , $scope.bucKetLists); 
}); 
});
