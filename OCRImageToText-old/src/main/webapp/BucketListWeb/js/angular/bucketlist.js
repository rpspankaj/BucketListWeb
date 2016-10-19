var mod=angular.module('BucketApp', []);

mod.controller("BucketController", function($scope, $http) {
         
                //Initialize page with default data which is blank in this example
                $scope.bucketList = [];
                
         
                //Now load the data from server
                _refreshPageData();
         
                
                //HTTP DELETE- delete employee by Id
                $scope.removeBucket = function(bucket) {
                    $http({
                        method : 'DELETE',
                        url : 'https://hack-rest.herokuapp.com/bucketLists?id='+ bucket.id
                    }).then(_success, _error);
                };
 
                $scope.editBucketDone = function(bucket) {
                    var method = "PUT";
                    var url = "https://hack-rest.herokuapp.com/bucketLists";
                    bucket.status="close";  
                    $http({
                        method : method,
                        url : url,
                        data : angular.toJson(bucket),
                        headers : {
                            'Content-Type' : 'application/json'
                        }
                    }).then( _success, _error );
                };
				
				$scope.editBucket = function(bucket) {
                    var method = "PUT";
                    var url = "https://hack-rest.herokuapp.com/bucketLists";
                     
                    $http({
                        method : method,
                        url : url,
                        data : angular.toJson(bucket),
                        headers : {
                            'Content-Type' : 'application/json'
                        }
                    }).then( _success, _error );
				 };
                /* Private Methods */
                //HTTP GET- get all bucketLists collection
                function _refreshPageData() {debugger;
                    $http({
                        method : 'GET',
                        url : 'https://hack-rest.herokuapp.com/bucketLists?groupId=1'
                    }).then(function successCallback(response) {
                        $scope.bucketList = response.data;
                    }, function errorCallback(response) {
                        console.log("error from service");
                    });
                }
         
                function _success(response) {
                    _refreshPageData();
                    
                }
         
                function _error(response) {
                    console.log("Service Error");
                }
         
              
            });
