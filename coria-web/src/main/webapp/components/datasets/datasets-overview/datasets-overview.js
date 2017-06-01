'use strict';

angular.module('coria.components')
    .component('datasetsOverview', {
        bindings: {},
        transclude: true,
        templateUrl: 'components/datasets/datasets-overview/datasets-overview.html',
        controller: ["dataSetService", "$scope", "$location",
            function( dataSetService,   $scope,   $location){
            var vm = this;

            vm.datasets = [];
            vm.datasetsPerPage = 15;
            dataSetService.shortDataSets().then(function(data){
                vm.datasets = data;
            });

            vm.openDataset = function openDataset(ds){
                $location.path("datasets/" + ds.id);
            };
        }]
    });