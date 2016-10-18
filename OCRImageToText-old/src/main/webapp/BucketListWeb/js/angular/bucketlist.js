angular.module('BucketApp', [])


.controller('BucketController', function($scope, $http) {
$http.get("https://hack-rest.herokuapp.com/bucketLists?groupId=1").success(function (data) 
{ 
   $scope.bucketList = data; 
   console.log("bucketList" , $scope.bucketList); 
}); 
});
