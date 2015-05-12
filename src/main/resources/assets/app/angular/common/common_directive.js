angular.module('chatApp.common')
    .directive('scrollItem',function(){
    return{
        restrict: "A",
        link: function(scope, element, attributes) {
            if (scope.$last){
                scope.$emit("Finished");
            }
        }
    }
});

angular.module('chatApp.common')
    .directive('scrollIf', function() {
    return{
        restrict: "A",
        link: function(scope, element, attributes) {
            scope.$on("Finished",function(){
                var chat_height = element.outerHeight();
                console.log(chat_height);
                element.scrollTop(chat_height);
            });
        }
    }
});