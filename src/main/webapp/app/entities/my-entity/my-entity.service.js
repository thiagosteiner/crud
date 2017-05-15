(function() {
    'use strict';
    angular
        .module('crudApp')
        .factory('MyEntity', MyEntity);

    MyEntity.$inject = ['$resource'];

    function MyEntity ($resource) {
        var resourceUrl =  'api/my-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
