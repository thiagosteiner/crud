(function() {
    'use strict';

    angular
        .module('crudApp')
        .controller('MyEntityDialogController', MyEntityDialogController);

    MyEntityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MyEntity'];

    function MyEntityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MyEntity) {
        var vm = this;

        vm.myEntity = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.myEntity.id !== null) {
                MyEntity.update(vm.myEntity, onSaveSuccess, onSaveError);
            } else {
                MyEntity.save(vm.myEntity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('crudApp:myEntityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
